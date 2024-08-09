package com.bdong5.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    public static final String BOOK_ID_KEY = "bookId";

    private TextView txtBookName, txtAuthorName, txtPages, txtDescription;
    private Button btnAddToCurrentlyReading, btnAddToWantToReadList, btnAddToAlreadyReadList, btnAddtoFavourite;
    private ImageView imgBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();


        Intent intent = getIntent();
        if (null != intent){
            int bookId = intent.getIntExtra(BOOK_ID_KEY, -1);
            if (bookId != -1){
                Book incomingBook = Utils.getInstance().getBookById(bookId);
                if (null != incomingBook){
                    setData(incomingBook);
                    handleAlreadyRead(incomingBook);
                    handleWantToReadBooks(incomingBook);
                    handleCurrentlyReadingBooks(incomingBook);
                    handleFavoriteBooks(incomingBook);
                }
            }
        }
    }

    private void handleFavoriteBooks(Book incomingBook) {
        ArrayList<Book> favoriteBooks = Utils.getInstance().getFavoriteBooks();
        boolean existInFavoriteBooks = false;
        for (Book b: favoriteBooks){
            if (b.getId() == incomingBook.getId()){
                existInFavoriteBooks = true;
            }
        }
        if (existInFavoriteBooks){
            btnAddtoFavourite.setEnabled(false);
        } else{}
    }

    private void handleCurrentlyReadingBooks(Book incomingBook) {
        ArrayList<Book> currentlyReadingBooks = Utils.getInstance().getCurrentlyReadingBooks();
        boolean existInCurrentlyReadingBooks = false;
        for (Book b: currentlyReadingBooks) {
            if (b.getId() == incomingBook.getId()) {
                existInCurrentlyReadingBooks = true;
            } else{
                btnAddtoFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if (Utils.getInstance().addToFavorite(incomingBook)){
                                Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BookActivity.this, FavoriteBookActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BookActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                });
            }
        }

        if (existInCurrentlyReadingBooks){
            btnAddToCurrentlyReading.setEnabled(false);
        } else{
            btnAddToCurrentlyReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.getInstance().addToCurrentlyReading(incomingBook)){
                        Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, CurrentlyReadingBookActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void handleWantToReadBooks(Book incomingBook) {
        ArrayList<Book> wantToReadBooks = Utils.getInstance().getWantToReadBooks();
        boolean existInWantToReadBooks = false;
        for (Book b: wantToReadBooks) {
            if (b.getId() == incomingBook.getId()) {
                existInWantToReadBooks = true;
            }
        }

        if (existInWantToReadBooks){
            btnAddToWantToReadList.setEnabled(false);
        } else{
            btnAddToWantToReadList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.getInstance().addToWantToRead(incomingBook)){
                        Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, WantToReadBookActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    /** Enable and Disable Button
     * Add the book to Already Read Book ArrayList
     * @param incomingBook
     */
    private void handleAlreadyRead(Book incomingBook) {
        ArrayList<Book> alreadyReadBooks = Utils.getInstance().getAlreadyReadBooks();
        boolean existInAlreadyReadBooks = false;
        for (Book b: alreadyReadBooks) {
            if (b.getId() == incomingBook.getId()) {
                existInAlreadyReadBooks = true;
            }
        }

        if (existInAlreadyReadBooks){
            btnAddToAlreadyReadList.setEnabled(false);
        } else{
            btnAddToAlreadyReadList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.getInstance().addToAlreadyRead(incomingBook)){
                        Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, AlreadyReadBookActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void setData(Book book) {
        txtBookName.setText(book.getName());
        txtAuthorName.setText(book.getAuthor());
        txtPages.setText(String.valueOf(book.getPages()));
        txtDescription.setText(book.getLongDesc());
        Glide.with(this).asBitmap().load(book.getImageURL()).into(imgBook);
    }

    private void initViews() {
        txtAuthorName = findViewById(R.id.txtAuthorName);
        txtBookName = findViewById(R.id.txtBookName);
        txtPages = findViewById(R.id.txtPages);
        txtDescription = findViewById(R.id.txtDescription);

        btnAddToAlreadyReadList = findViewById(R.id.btnAddToAlreadyReadList);
        btnAddToCurrentlyReading = findViewById(R.id.btnAddToCurrentlyReading);
        btnAddtoFavourite = findViewById(R.id.btnAddtoFavourite);
        btnAddToWantToReadList = findViewById(R.id.btnAddToWantToReadList);

        imgBook = findViewById(R.id.imgBook);
    }
}