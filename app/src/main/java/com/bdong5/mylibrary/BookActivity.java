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
import com.bumptech.glide.util.Util;
import com.bdong5.mylibrary.Book;


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
        if (intent!=null){

            // Extract the book ID passed from the previous activity
            String bookId = intent.getStringExtra(BOOK_ID_KEY);

            if (bookId!=null && !bookId.isEmpty()){     // Check if the book ID is not null and not empty

                Utils.getInstance(this).getBookById(bookId, new Utils.FirebaseCallback<Book>() { // Obtain book details from Firebase using bookId
                    @Override
                    public void onCallback(Book result) { // If book is successfully retrieved, handle:
                        if (result != null){

                            setData(result); // Populate UI with book data
                            handleAlreadyRead(result); // Check if book is already in list
                            handleWantToReadBooks(result); // ^^
                            handleCurrentlyReadingBooks(result); // ^^
                            handleFavoriteBooks(result); // ^^
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BookActivity.this, "Failed to load book", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        }

    private void handleFavoriteBooks(Book incomingBook) {
       Utils.getInstance(this).getFavoriteBooks(new Utils.FirebaseCallback<ArrayList<Book>>() { // obtain favorite books from Firebase
            @Override
            public void onCallback(ArrayList<Book> favoriteBooks) { // handle favorite books from Firebase
                boolean existInFavoriteBooks = false;
                for (Book b: favoriteBooks){ // Check if book exists in favorite books list
                    if (b.getId().equals(incomingBook.getId())){
                        existInFavoriteBooks = true; // book exists
                        break;
                    }
                }
                if (existInFavoriteBooks){
                    btnAddtoFavourite.setEnabled(false); // disable button if book is in list
                } else{
                    btnAddtoFavourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.getInstance(BookActivity.this).addToFavorite(incomingBook);
                            Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookActivity.this, FavoriteBookActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BookActivity.this, "Sorry, an error occured. Please try again.", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void handleCurrentlyReadingBooks(Book incomingBook) {
        Utils.getInstance(this).getCurrentlyReadingBooks(new Utils.FirebaseCallback<ArrayList<Book>>() { // get currently reading books from firebase
            @Override
            public void onCallback(ArrayList<Book> currentlyReadingBooks) { // handle currently reading books
                boolean existInCurrentlyReadingBooks = false;
                for (Book b: currentlyReadingBooks){
                    if (b.getId().equals(incomingBook.getId())){
                        existInCurrentlyReadingBooks = true;
                        break;
                    }
                }
                if (existInCurrentlyReadingBooks){
                    btnAddToCurrentlyReading.setEnabled(false); // disable button if book is in list
                }
                else{
                    btnAddToCurrentlyReading.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.getInstance(BookActivity.this).addToCurrentlyReading(incomingBook);
                            Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookActivity.this, CurrentlyReadingBookActivity.class)); // Open currently reading screen

                        }
                    });
                }


            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BookActivity.this, "Sorry, an error occured. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void handleWantToReadBooks(Book incomingBook) {
        Utils.getInstance(this).getWantToReadBooks(new Utils.FirebaseCallback<ArrayList<Book>>() { // obtain want to read books from Firebase
            @Override
            public void onCallback(ArrayList<Book> wantToReadBooks) { // handle want to read books
                boolean existInWantToReadBooks = false;
                for (Book b : wantToReadBooks) {
                    if (b.getId() == incomingBook.getId()) {
                        existInWantToReadBooks = true;
                        break;
                    }
                }
                if (existInWantToReadBooks) {
                    btnAddToWantToReadList.setEnabled(false); // disable button if book is in list
                } else {
                    btnAddToWantToReadList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.getInstance(BookActivity.this).addToWantToRead(incomingBook);
                            Toast.makeText(BookActivity.this, "Book successfully added.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookActivity.this, WantToReadBookActivity.class));
                        }
                    });
                }


            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BookActivity.this, "Sorry, an error occured. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    /** Enable and Disable Button
     * Add the book to Already Read Book ArrayList
     * @param incomingBook
     */
    private void handleAlreadyRead(Book incomingBook) {
        Utils.getInstance(this).getAlreadyReadBooks(new Utils.FirebaseCallback<ArrayList<Book>>() { // obtain already read books from Firebase
            @Override
            public void onCallback(ArrayList<Book> alreadyReadBooks) { // handle already read books
                boolean existInAlreadyReadBooks = false;
                for (Book b: alreadyReadBooks) {
                    if (b.getId() == incomingBook.getId()) {
                        existInAlreadyReadBooks = true;
                        break;
                    }
                }

                if (existInAlreadyReadBooks){
                    btnAddToAlreadyReadList.setEnabled(false); // disable button if book is in list
                } else{
                    btnAddToAlreadyReadList.setOnClickListener(new View.OnClickListener() { // enable click action
                        @Override
                        public void onClick(View v) {
                            Utils.getInstance(BookActivity.this).addToAlreadyRead(incomingBook);
                            Toast.makeText(BookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookActivity.this, AlreadyReadBookActivity.class));
                            }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BookActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });


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