package org.libreframe.app

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : Activity() {
    private val projects by lazy { ProjectStore(applicationContext) }
    private val worker = Executors.newSingleThreadExecutor()
    private var pendingExport: EditProject? = null
    private var exportCancellation: AtomicBoolean? = null
    private var exportStatus: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProjects()
    }

    override fun onDestroy() {
        exportCancellation?.set(true)
        worker.shutdownNow()
        super.onDestroy()
    }

    @Deprecated("Use the Activity Result API when AndroidX Activity is introduced.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        val uri = data?.data ?: return

        when (requestCode) {
            REQUEST_IMPORT -> importVideo(uri, data.flags)
            REQUEST_EXPORT -> pendingExport?.let { exportProject(it, uri) }
        }
    }

    private fun showProjects() {
        val content = column()
        content.addView(text("LibreFrame", 28f))
        content.addView(text("Local video projects. Original media stays untouched.", 16f))
        content.addView(button("Create video project") { chooseVideo() })

        val savedProjects = projects.list()
        if (savedProjects.isEmpty()) {
            content.addView(text("No saved projects yet.", 16f))
        } else {
            content.addView(text("Recent projects", 20f))
            savedProjects.forEach { project ->
                content.addView(button(project.name) { showEditor(project) })
                content.addView(text(project.sourceDisplayName, 14f))
            }
        }
        setContentView(scroll(content))
    }

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        startActivityForResult(intent, REQUEST_IMPORT)
    }

    private fun importVideo(uri: Uri, intentFlags: Int) {
        try {
            val readPermission = intentFlags and Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (readPermission != 0) contentResolver.takePersistableUriPermission(uri, readPermission)

            val metadata = MediaMetadataRetriever()
            val durationUs = try {
                metadata.setDataSource(this, uri)
                val durationMs = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull() ?: error("The selected video duration could not be read.")
                durationMs * 1_000L
            } finally {
                metadata.release()
            }
            require(durationUs > 0) { "The selected video has no usable duration." }
            val name = displayName(uri)
            val project = EditProject.create(name, uri.toString(), name, durationUs)
            projects.save(project)
            showEditor(project)
        } catch (error: Exception) {
            showMessage(error.message ?: "The selected video could not be opened.")
        }
    }

    private fun showEditor(project: EditProject) {
        val content = column()
        content.addView(button("Back to projects") { showProjects() })
        content.addView(text(project.name, 24f))
        content.addView(text("Source: ${project.sourceDisplayName}", 14f))

        val preview = VideoView(this)
        preview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(240),
        )
        preview.setVideoURI(Uri.parse(project.sourceUri))
        preview.setMediaController(MediaController(this).also { it.setAnchorView(preview) })
        preview.setOnErrorListener { _, _, _ ->
            showMessage("The source is unavailable or uses an unsupported format. Relink it by creating a new project.")
            true
        }
        content.addView(preview)

        content.addView(text("Trim range (milliseconds)", 18f))
        val startInput = numberInput(project.trimStartUs / 1_000L)
        val endInput = numberInput(project.trimEndUs / 1_000L)
        content.addView(text("Start", 14f))
        content.addView(startInput)
        content.addView(text("End", 14f))
        content.addView(endInput)
        content.addView(
            text(
                "This first export path copies encoded samples and may begin at the preceding video keyframe. " +
                    "It does not mix audio or render text/effects yet.",
                14f,
            ),
        )
        content.addView(button("Save trim") {
            readAndSaveTrim(project, startInput, endInput)?.let(::showEditor)
        })
        content.addView(button("Export trimmed MP4") {
            readAndSaveTrim(project, startInput, endInput)?.let { saved ->
                pendingExport = saved
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "video/mp4"
                    putExtra(Intent.EXTRA_TITLE, "${saved.name}.mp4")
                }
                startActivityForResult(intent, REQUEST_EXPORT)
            }
        })
        exportStatus = TextView(this).also { content.addView(it) }
        setContentView(scroll(content))
    }

    private fun readAndSaveTrim(
        project: EditProject,
        startInput: EditText,
        endInput: EditText,
    ): EditProject? {
        return try {
            val startUs = Math.multiplyExact(
                startInput.text.toString().toLongOrNull() ?: error("Enter a valid trim start."),
                1_000L,
            )
            val endUs = Math.multiplyExact(
                endInput.text.toString().toLongOrNull() ?: error("Enter a valid trim end."),
                1_000L,
            )
            val saved = project.withTrim(startUs, endUs)
            projects.save(saved)
            saved
        } catch (error: Exception) {
            showMessage(error.message ?: "The trim range is invalid.")
            null
        }
    }

    private fun exportProject(project: EditProject, destination: Uri) {
        val cancellation = AtomicBoolean(false)
        exportCancellation = cancellation
        exportStatus?.text = "Exporting. This screen shows no estimated percentage."
        val cancelButton = button("Cancel export") { cancellation.set(true) }
        (exportStatus?.parent as? LinearLayout)?.addView(cancelButton)
        worker.execute {
            val result = runCatching {
                TrimExporter(this).export(project, destination, cancellation::get)
            }
            runOnUiThread {
                exportCancellation = null
                exportStatus?.text = result.fold(
                    onSuccess = { "Export complete: ${destination.lastPathSegment ?: "MP4"}" },
                    onFailure = { error ->
                        if (cancellation.get()) "Export cancelled; incomplete output was removed."
                        else "Export failed: ${error.message ?: "unsupported media or destination"}"
                    },
                )
            }
        }
    }

    private fun displayName(uri: Uri): String {
        contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) return cursor.getString(index)
            }
        }
        return uri.lastPathSegment ?: "Video"
    }

    private fun column() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(12), dp(16), dp(24))
    }

    private fun scroll(content: LinearLayout) = ScrollView(this).apply {
        addView(content)
    }

    private fun text(value: String, sizeSp: Float) = TextView(this).apply {
        text = value
        textSize = sizeSp
        setPadding(0, dp(8), 0, dp(8))
    }

    private fun button(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        minHeight = dp(48)
        setOnClickListener { action() }
    }

    private fun numberInput(value: Long) = EditText(this).apply {
        setText(value.toString())
        inputType = android.text.InputType.TYPE_CLASS_NUMBER
        minHeight = dp(48)
        contentDescription = "Trim time in milliseconds"
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private companion object {
        const val REQUEST_IMPORT = 1
        const val REQUEST_EXPORT = 2
    }
}
