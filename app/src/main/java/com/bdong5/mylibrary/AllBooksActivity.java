package com.bdong5.mylibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity {

    private RecyclerView booksRecView;
    private BookRecViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_books); // connect to xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new BookRecViewAdapter(this, "allBooks");
        booksRecView = findViewById(R.id.booksRecView);
        booksRecView.setAdapter(adapter);
        booksRecView.setLayoutManager(new LinearLayoutManager(this));


//        adapter.setBooks(Utils.getInstance(this).getAllBooks());
        Utils.getInstance(this).getAllBooks(new Utils.FirebaseCallback<ArrayList<Book>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCallback(ArrayList<Book> result) {
                adapter.setBooks(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("Firestore", "Error getting books", e);
            }
        });

    }
}