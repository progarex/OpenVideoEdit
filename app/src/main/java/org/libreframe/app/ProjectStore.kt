package org.libreframe.app

import android.content.Context
import android.util.AtomicFile
import org.json.JSONObject
import java.io.File

class ProjectStore(context: Context) {
    private val directory = File(context.filesDir, "projects").apply { mkdirs() }

    fun list(): List<EditProject> = directory.listFiles { file -> file.extension == "json" }
        .orEmpty()
        .mapNotNull { file -> runCatching { read(file) }.getOrNull() }
        .sortedByDescending(EditProject::updatedAtEpochMillis)

    fun get(id: String): EditProject? {
        val parsedId = runCatching { java.util.UUID.fromString(id).toString() }.getOrNull()
            ?: return null
        val file = File(directory, "$parsedId.json")
        return if (file.isFile) read(file) else null
    }

    fun save(project: EditProject) {
        val safeId = java.util.UUID.fromString(project.id).toString()
        val atomicFile = AtomicFile(File(directory, "$safeId.json"))
        val output = atomicFile.startWrite()
        try {
            output.write(encode(project).toString().toByteArray(Charsets.UTF_8))
            atomicFile.finishWrite(output)
        } catch (error: Exception) {
            atomicFile.failWrite(output)
            throw error
        }
    }

    private fun read(file: File): EditProject {
        val json = JSONObject(file.readText())
        return EditProject(
            id = json.getString("id"),
            name = json.getString("name"),
            sourceUri = json.getString("sourceUri"),
            sourceDisplayName = json.getString("sourceDisplayName"),
            sourceDurationUs = json.getLong("sourceDurationUs"),
            trimStartUs = json.getLong("trimStartUs"),
            trimEndUs = json.getLong("trimEndUs"),
            createdAtEpochMillis = json.getLong("createdAtEpochMillis"),
            updatedAtEpochMillis = json.getLong("updatedAtEpochMillis"),
        )
    }

    private fun encode(project: EditProject) = JSONObject()
        .put("schemaVersion", 1)
        .put("id", project.id)
        .put("name", project.name)
        .put("sourceUri", project.sourceUri)
        .put("sourceDisplayName", project.sourceDisplayName)
        .put("sourceDurationUs", project.sourceDurationUs)
        .put("trimStartUs", project.trimStartUs)
        .put("trimEndUs", project.trimEndUs)
        .put("createdAtEpochMillis", project.createdAtEpochMillis)
        .put("updatedAtEpochMillis", project.updatedAtEpochMillis)
}
