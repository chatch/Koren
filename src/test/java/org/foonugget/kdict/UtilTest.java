
package org.foonugget.kdict;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {

    @Test
    public void test() {
        assertEquals("it''s", Util.escapeDatabaseStringLiteral("it's"));
        assertEquals("its''", Util.escapeDatabaseStringLiteral("its'"));
        assertEquals("its", Util.escapeDatabaseStringLiteral("its"));
        assertEquals("", Util.escapeDatabaseStringLiteral(""));
    }

}
