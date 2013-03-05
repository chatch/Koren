
package org.foonugget.kdict.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class opens a handle to the pinyin database and provides access
 * routines. If the database does not yet exist it is copied from the apk assets
 * folder to the Android system path. 
 * 
 * To inspect live database with sqlite:
 *   bash> adb shell 
 *   adbshell> sqlite3 /data/data/org.foonugget.kdict/databases/dict.ogg
 */
@Singleton
public class DictionaryDatabaseOpenHelper extends SQLiteOpenHelper {

    private final static String TAG = DictionaryDatabaseOpenHelper.class.getSimpleName();

    private final static int DB_VERSION = 2;

    private final static String PACKAGE_NAME = DictionaryDatabaseOpenHelper.class.getPackage()
            .getName().replace(".data", "");
    private final static String DB_DIR = "/data/data/" + PACKAGE_NAME + "/databases/";

    // Database has a .ogg extension to work around the 1MB limit.
    // TODO: consider implementing file download rather then bundling the dict.
    private final static String DB_NAME = "dict.ogg";
    private final static String DB_FULL_PATH = DB_DIR + DB_NAME;

    @Inject
    private AssetManager mAssetManager;

    static private SQLiteDatabase mDB;

    @Inject
    public DictionaryDatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SQLiteDatabase openDatabase() {

        if (mDB != null && mDB.isOpen()) {
            return mDB;
        }

        boolean bOpenFailed = false;
        try {
            mDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.i(TAG, "Exception opening database. Assuming it's because it does not exist.", e);
            // flag fail so we can install it below
            bOpenFailed = true;
            if (mDB != null) {
                mDB.close();
                mDB = null;
            }
        }

        /* open failed (most likely 1st run of app) or version mismatch */
        if (bOpenFailed) {
            Log.i(TAG, "copying database version " + DB_VERSION);
            try {
                InputStream dbInput = mAssetManager.open(DB_NAME);

                File dbDir = new File(DB_DIR);
                boolean mkdirRet = dbDir.mkdirs();
                if (mkdirRet == false) {
                    Log.w(TAG, "mkdirs failed for " + DB_DIR);
                }
                OutputStream dbOutput = new FileOutputStream(DB_FULL_PATH, false);

                copyDatabase(dbInput, dbOutput);
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy the database", e);
                throw new RuntimeException("unable to install database ...");
            }

            mDB = getReadableDatabase();
        }

        return mDB;
    }

    /*
     * The routine copies that sqlite database from the input stream to the
     * output stream.
     * @param dbInput An open handle to a sqlite database file
     * @param dbOutput An open handle to file to copy dbInput file data to
     */
    public void copyDatabase(InputStream dbInput, OutputStream dbOutput) throws IOException {
        /* create new instance to copy over */
        SQLiteDatabase newDB = getReadableDatabase();
        newDB.close();
        newDB = null;

        byte[] buffer = new byte[1024];
        int length;
        while ((length = dbInput.read(buffer)) > 0) {
            dbOutput.write(buffer, 0, length);
        }

        dbOutput.flush();
        dbOutput.close();
        dbInput.close();
    }

    @Override
    public synchronized void close() {
        if (mDB != null)
            mDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // do nothing - read only db bundle with apk
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing - read only db bundle with apk
    }

}
