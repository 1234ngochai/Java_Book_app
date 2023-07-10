package com.fit2081.myfirstproject.provider;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "book_id")
    private String book_id;
    @ColumnInfo(name = "book_title")
    private String title;
    @ColumnInfo(name = "book_isbn")
    private String isbn;
    @ColumnInfo(name = "book_author")
    private String author;
    @ColumnInfo(name = "book_decription")
    private String descrition;
    @ColumnInfo(name = "book_price")
    private String price;

    public Book(String book_id, String title, String isbn, String author, String descrition, String price) {
        this.book_id = book_id;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.descrition = descrition;
        this.price = price;
        Log.d("week6App","addbook");
    }
    public String getBook_id() {
        return book_id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescrition() {
        return descrition;
    }

    public String getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }



}
