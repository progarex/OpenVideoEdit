package org.libreframe.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EditProjectTest {
    @Test
    fun trimUpdatePreservesSourceAndCalculatesDuration() {
        val project = EditProject.create("Draft", "content://source", "clip.mp4", 10_000_000)

        val trimmed = project.withTrim(2_000_000, 7_000_000)

        assertEquals("content://source", trimmed.sourceUri)
        assertEquals(5_000_000L, trimmed.outputDurationUs)
        assertEquals(2_000_000L, trimmed.trimStartUs)
        assertEquals(7_000_000L, trimmed.trimEndUs)
    }

    @Test
    fun trimUpdateRejectsEmptyOrOutOfRangeSelections() {
        val project = EditProject.create("Draft", "content://source", "clip.mp4", 10_000_000)

        assertThrows(IllegalArgumentException::class.java) {
            project.withTrim(5_000_000, 5_000_000)
        }
        assertThrows(IllegalArgumentException::class.java) {
            project.withTrim(-1, 5_000_000)
        }
        assertThrows(IllegalArgumentException::class.java) {
            project.withTrim(5_000_000, 10_000_001)
        }
    }
}
