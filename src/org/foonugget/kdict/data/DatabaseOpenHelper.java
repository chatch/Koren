
package org.foonugget.kdict.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class opens a handle to the pinyin database and provides access
 * routines. If the database does not yet exist it is copied from the apk assets
 * folder to the Android system path.
 */
@Singleton
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final static String TAG = DatabaseOpenHelper.class.getSimpleName();

    private final static int DB_VERSION = 1;
    private final static String PREFS_DB_VERSION_KEY = "DB_VERSION";

    private final static String APP_PKG_NAME = DatabaseOpenHelper.class
            .getPackage().getName().replace(".data", "");
    private final static String DB_DIR = "/data/data/" + APP_PKG_NAME
            + "/databases/";

    // Database has a .ogg extension to work around the 1MB limit.
    // TODO: consider implementing file download rather then bundling the dict.
    private final static String DB_NAME = "dict.ogg";
    private final static String DB_FULL_PATH = DB_DIR + DB_NAME;

    @Inject
    @Named("sharedPreferences")
    private SharedPreferences mPrefs;

    @Inject
    private AssetManager mAssetManager;

    static private SQLiteDatabase mDB;

    @Inject
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SQLiteDatabase openDatabase() {

        if (mDB != null && mDB.isOpen()) {
            return mDB;
        }

        boolean bOpenFailed = false;
        try {
            mDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.i(TAG,
                    "Exception opening database. Assuming it's because it does not exist.",
                    e);
            // flag fail so we can install it below
            bOpenFailed = true;
            if (mDB != null) {
                mDB.close();
                mDB = null;
            }
        }

        /* Get currently installed database version */
        int installedDbVersion = mPrefs.getInt(PREFS_DB_VERSION_KEY, 1);

        /* open failed (most likely 1st run of app) or version mismatch */
        if (bOpenFailed || installedDbVersion != DB_VERSION) {
            Log.i(TAG, "copying database version " + DB_VERSION);
            try {
                InputStream dbInput = mAssetManager.open(DB_NAME);

                File dbDir = new File(DB_DIR);
                boolean mkdirRet = dbDir.mkdirs();
                if (mkdirRet == false) {
                    Log.w(TAG, "mkdirs failed for " + DB_DIR);
                }
                OutputStream dbOutput = new FileOutputStream(DB_FULL_PATH,
                        false);

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
    public void copyDatabase(InputStream dbInput, OutputStream dbOutput)
            throws IOException {
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
        // do nothing - we create the database before packaging the apk
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

}
