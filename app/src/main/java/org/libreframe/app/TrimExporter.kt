package org.libreframe.app

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri

class TrimExporter(private val context: Context) {
    fun export(
        project: EditProject,
        destination: Uri,
        isCancelled: () -> Boolean,
    ) {
        val extractor = MediaExtractor()
        var muxer: MediaMuxer? = null
        var outputDescriptor: android.os.ParcelFileDescriptor? = null
        var muxerStarted = false
        var succeeded = false

        try {
            extractor.setDataSource(context, Uri.parse(project.sourceUri), null)
            val tracks = (0 until extractor.trackCount).mapNotNull { sourceIndex ->
                val format = extractor.getTrackFormat(sourceIndex)
                val mime = format.getString(MediaFormat.KEY_MIME).orEmpty()
                if (mime.startsWith("video/") || mime.startsWith("audio/")) {
                    sourceIndex to format
                } else {
                    null
                }
            }
            require(tracks.any { it.second.getString(MediaFormat.KEY_MIME).orEmpty().startsWith("video/") }) {
                "The selected source has no video track."
            }
            require(tracks.isNotEmpty()) { "No supported audio or video tracks were found." }

            outputDescriptor = context.contentResolver.openFileDescriptor(destination, "w")
                ?: error("The selected destination could not be opened.")
            muxer = MediaMuxer(outputDescriptor.fileDescriptor, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            tracks.forEach { (_, format) ->
                val rotation = runCatching { format.getInteger(MediaFormat.KEY_ROTATION) }.getOrNull()
                if (rotation != null && rotation != 0) {
                    muxer.setOrientationHint(rotation)
                }
            }

            val outputTrackBySource = tracks.associate { (sourceIndex, format) ->
                sourceIndex to muxer.addTrack(format)
            }
            tracks.forEach { (sourceIndex, _) -> extractor.selectTrack(sourceIndex) }
            extractor.seekTo(project.trimStartUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)

            val originUs = extractor.sampleTime.coerceAtLeast(0)

            muxer.start()
            muxerStarted = true
            val buffer = createSampleBuffer(tracks.map { it.second })
            val bufferInfo = MediaCodec.BufferInfo()
            var videoSampleCount = 0
            val videoTrackIndexes = tracks.filter { (_, format) ->
                format.getString(MediaFormat.KEY_MIME).orEmpty().startsWith("video/")
            }.map { it.first }.toSet()

            while (extractor.sampleTrackIndex >= 0) {
                check(!isCancelled()) { "Export cancelled." }
                val sampleTimeUs = extractor.sampleTime
                if (sampleTimeUs >= project.trimEndUs) break

                val sourceTrack = extractor.sampleTrackIndex
                val outputTrack = outputTrackBySource[sourceTrack]
                if (outputTrack != null && sampleTimeUs >= originUs) {
                    buffer.clear()
                    val sampleSize = extractor.readSampleData(buffer, 0)
                    if (sampleSize < 0) break
                    bufferInfo.set(
                        0,
                        sampleSize,
                        sampleTimeUs - originUs,
                        extractor.sampleFlags,
                    )
                    muxer.writeSampleData(outputTrack, buffer, bufferInfo)
                    if (sourceTrack in videoTrackIndexes) videoSampleCount++
                }
                extractor.advance()
            }

            check(!isCancelled()) { "Export cancelled." }
            check(videoSampleCount > 0) { "No video samples fell within the selected trim range." }
            muxer.stop()
            muxerStarted = false
            succeeded = true
        } finally {
            if (muxerStarted) runCatching { muxer?.stop() }
            runCatching { muxer?.release() }
            runCatching { outputDescriptor?.close() }
            extractor.release()
            if (!succeeded) {
                runCatching { context.contentResolver.delete(destination, null, null) }
            }
        }
    }

    private fun createSampleBuffer(formats: List<MediaFormat>) =
        java.nio.ByteBuffer.allocateDirect(
            formats.maxOf { format ->
                val reportedSize = runCatching {
                    format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
                }.getOrDefault(DEFAULT_SAMPLE_BUFFER_SIZE)
                require(reportedSize <= MAX_SAMPLE_BUFFER_SIZE) {
                    "This source requires a sample buffer larger than the supported 32 MiB limit."
                }
                reportedSize.coerceAtLeast(DEFAULT_SAMPLE_BUFFER_SIZE)
            },
        )

    private companion object {
        const val DEFAULT_SAMPLE_BUFFER_SIZE = 1 * 1024 * 1024
        const val MAX_SAMPLE_BUFFER_SIZE = 32 * 1024 * 1024
    }
}
