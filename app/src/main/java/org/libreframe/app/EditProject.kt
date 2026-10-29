package org.libreframe.app

import java.util.UUID

data class EditProject(
    val id: String,
    val name: String,
    val sourceUri: String,
    val sourceDisplayName: String,
    val sourceDurationUs: Long,
    val trimStartUs: Long = 0,
    val trimEndUs: Long = sourceDurationUs,
    val createdAtEpochMillis: Long = System.currentTimeMillis(),
    val updatedAtEpochMillis: Long = createdAtEpochMillis,
) {
    init {
        require(id.isNotBlank())
        require(name.isNotBlank())
        require(sourceUri.isNotBlank())
        require(sourceDurationUs > 0)
        require(trimStartUs >= 0)
        require(trimEndUs > trimStartUs)
        require(trimEndUs <= sourceDurationUs)
    }

    val outputDurationUs: Long
        get() = trimEndUs - trimStartUs

    fun withTrim(startUs: Long, endUs: Long): EditProject = copy(
        trimStartUs = startUs,
        trimEndUs = endUs,
        updatedAtEpochMillis = System.currentTimeMillis(),
    )

    companion object {
        fun create(name: String, uri: String, displayName: String, durationUs: Long): EditProject {
            val now = System.currentTimeMillis()
            return EditProject(
                id = UUID.randomUUID().toString(),
                name = name.ifBlank { displayName },
                sourceUri = uri,
                sourceDisplayName = displayName,
                sourceDurationUs = durationUs,
                createdAtEpochMillis = now,
                updatedAtEpochMillis = now,
            )
        }
    }
}
