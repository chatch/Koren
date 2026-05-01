package org.foonugget.kdict

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTest {

    @Test
    fun testEscapeDatabaseStringLiteral() {
        assertEquals("it''s", escapeDatabaseStringLiteral("it's"))
        assertEquals("its''", escapeDatabaseStringLiteral("its'"))
        assertEquals("its", escapeDatabaseStringLiteral("its"))
        assertEquals("", escapeDatabaseStringLiteral(""))
    }

    private fun escapeDatabaseStringLiteral(string: String): String {
        return string.replace("'", "''")
    }
}
