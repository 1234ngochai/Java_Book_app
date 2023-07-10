package com.fit2081.myfirstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fit2081.myfirstproject.provider.RecycleViewFragment;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_3,recycleViewFragment).commit();
    }
}