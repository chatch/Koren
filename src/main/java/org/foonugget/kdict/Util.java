package org.foonugget.kdict;

public class Util {

    public static String escapeDatabaseStringLiteral(String string) {
        return string.replaceAll("'", "''");
    }

}
