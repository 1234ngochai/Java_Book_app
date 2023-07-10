package com.fit2081.myfirstproject.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Query("Delete from books Where book_id = :id")
    public void deleteBook(int id);
    @Insert
    public void addBook(Book book);

    @Query("delete FROM books")
    void deleteAllBook();

    @Query("Select * from books")
    public LiveData<List<Book>> getAllBook();
}
