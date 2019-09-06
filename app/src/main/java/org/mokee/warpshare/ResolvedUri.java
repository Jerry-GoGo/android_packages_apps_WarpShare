package org.mokee.warpshare;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResolvedUri {

    private static final String TAG = "ResolvedUri";

    public final Uri uri;

    private final Context mContext;

    private boolean mOk = false;

    private String mName;
    private String mPath;
    private long mSize;

    ResolvedUri(Context context, Uri uri) {
        mContext = context;
        this.uri = uri;

        if (uri == null) {
            return;
        }

        Cursor cursor;

        try {
            cursor = context.getContentResolver().query(
                    uri, null, null, null, null);
        } catch (SecurityException e) {
            Log.e(TAG, "Failed resolving uri: " + uri, e);
            return;
        }

        if (cursor == null) {
            return;
        }

        final int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        final int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

        cursor.moveToFirst();

        mName = cursor.getString(nameIndex);
        mPath = "./" + mName;
        mSize = cursor.isNull(sizeIndex) ? -1 : cursor.getLong(sizeIndex);

        cursor.close();

        mOk = true;
    }

    public boolean ok() {
        return mOk;
    }

    public String name() {
        return mName;
    }

    public String path() {
        return mPath;
    }

    public long size() {
        return mSize;
    }

    public InputStream stream() throws FileNotFoundException {
        return mContext.getContentResolver().openInputStream(uri);
    }

}
