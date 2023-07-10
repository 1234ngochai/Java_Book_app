package com.fit2081.myfirstproject.provider;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepo bookRepo;

    public static final String TAG="week3tag";
    public Application application;
    LiveData<List<Book>> myBook;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepo = new BookRepo(application);
        myBook = bookRepo.getBookList();
        application = application;
    }

    public void setMyBook(LiveData<List<Book>> myBook) {
        this.myBook = myBook;
    }

    public void addBook(Book book){
        bookRepo.addBookRepo(book);
        Log.d(TAG,"addBookViewmodel");
    }
    public void deleteAll(){
        bookRepo.deleteAll();
    }
    public LiveData<List<Book>> getMyBook() {
        return myBook;
    }
    public List<Book> getFilteredBooks(String author, String title, double maxPrice) {
        BookDatabase db = BookDatabase.getDatabase(application);
        List<Book> filteredBooks = new ArrayList<>();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables("books");

        String selection = "";
        List<String> selectionArgs = new ArrayList<>();

        if (author != null && !author.isEmpty()) {
            selection += "book_author LIKE ?";
            selectionArgs.add("%" + author + "%");
        }

        if (title != null && !title.isEmpty()) {
            if (!selection.isEmpty()) {
                selection += " AND ";
            }
            selection += "book_title LIKE ?";
            selectionArgs.add("%" + title + "%");
        }

        if (maxPrice > 0) {
            if (!selection.isEmpty()) {
                selection += " AND ";
            }
            selection += "book_price <= ?";
            selectionArgs.add(String.valueOf(maxPrice));
        }

        String[] selectionArgsArray = selectionArgs.toArray(new String[0]);

        Cursor cursor = db.query(queryBuilder.buildQuery(null, selection, null, null, null, null), selectionArgsArray);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String bookId = cursor.getString(cursor.getColumnIndex("book_id"));
            @SuppressLint("Range") String book_title = cursor.getString(cursor.getColumnIndex("book_title"));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex("book_isbn"));
            @SuppressLint("Range") String book_author = cursor.getString(cursor.getColumnIndex("book_author"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("book_decription"));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex("book_price"));

            Book book = new Book(bookId, book_title, isbn, book_author, description, price);

            filteredBooks.add(book);
        }

        cursor.close();

        return filteredBooks;
    }
}
