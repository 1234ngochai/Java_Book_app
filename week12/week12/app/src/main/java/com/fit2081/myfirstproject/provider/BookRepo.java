package com.fit2081.myfirstproject.provider;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

public class BookRepo {
    private BookDao bookDao;

    private LiveData<List<Book>> bookList;

    public BookRepo(Application app){
        BookDatabase db = BookDatabase.getDatabase(app);

        bookDao = db.bookDao();

        bookList = bookDao.getAllBook();
    }

    public void addBookRepo(Book book){
        BookDatabase.databaseWriteExecutor.execute(()->{
            bookDao.addBook(book);
        });
    }

    public LiveData<List<Book>> getBookList() {
        return bookList;
    }

    public void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            bookDao.deleteAllBook();
        });
    }

}
