package com.fit2081.myfirstproject.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class BookContentProvider extends ContentProvider {
    BookDatabase db;

    public BookContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete("books", selection, selectionArgs);

        return deletionCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert("books", 0, values);

        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public boolean onCreate() {
        db = BookDatabase.getDatabase(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("books");
        String query = builder.buildQuery(projection,selection,null,null,sortOrder,null);

        Log.d("Week8 book content provider ",query);
        Cursor cursor = db.getOpenHelper().getReadableDatabase().query(query);



        // TODO: Implement this to handle query requests from clients.
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}