package com.bdong5.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

//        String longDesc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "\nsed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut " +
//                "\nenim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
//                "\naliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate " +
//                "\nvelit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
//                "\nproident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//
//        //TODO;
//        Book book = new Book(
//                1,
//                "1Q84",
//                "Haruki Murakami",
//                1350,
//                "https://m.media-amazon.com/images/I/71oaO3Pxx-L._AC_UF1000,1000_QL80_.jpg",
//                "A work of maddening brilliance",
//                longDesc
//        );

        Intent intent = getIntent();
        if (null != intent){
            int bookId = intent.getIntExtra(BOOK_ID_KEY, -1);
            if (bookId != -1){
                Book incomingBook = Utils.getInstance().getBookById(bookId);
                if (null != incomingBook){
                    setData(incomingBook);
                }
            }
        }

//// Create a book object for "Diary of a Wimpy Kid" and set its data
//        Book book2 = new Book(
//                2,
//                "Diary of a Wimpy Kid",
//                "Jeff Kinney",
//                217,
//                "https://upload.wikimedia.org/wikipedia/en/8/84/Diary_of_a_Wimpy_Kid_book_cover.jpg",
//                "An educational masterpiece",
//                "Long Desc"
//        );
//        setData(book2);

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