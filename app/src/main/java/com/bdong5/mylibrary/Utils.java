package com.bdong5.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    private static final String ALL_BOOKS_KEY = "all_books";
    private static final String ALREADY_READ_BOOKS = "already_read_books";
    private static final String WANT_TO_READ_BOOKS = "want_to_read_books";
    private static final String CURRENTLY_READING_BOOKS = "currently_reading_books";
    private static final String FAVORITE_BOOKS = "favorite_books";

    private static volatile Utils instance;
    private SharedPreferences sharedPreferences;


    private Utils(Context context) {

        sharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE);


        if (null==getAllBooks()){
            initData();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        if (null==getAlreadyReadBooks()){
            editor.putString(ALREADY_READ_BOOKS, gson.toJson(new ArrayList<Book>()));
            editor.commit();
        }
        if (null==getWantToReadBooks()){
            editor.putString(WANT_TO_READ_BOOKS, gson.toJson(new ArrayList<Book>()));
            editor.commit();        }
        if (null==getCurrentlyReadingBooks()){
            editor.putString(CURRENTLY_READING_BOOKS, gson.toJson(new ArrayList<Book>()));
            editor.commit();        }
        if (null==getFavoriteBooks()){
            editor.putString(FAVORITE_BOOKS, gson.toJson(new ArrayList<Book>()));
            editor.commit();        }
        initData();

    }

    public static Utils getInstance(Context context) {
        if (instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils(context);
                }
            }
        }
        return instance;
    }

    private void initData() {
        //TODO: add initial data

        ArrayList<Book> books = new ArrayList<>();



        books.add(new Book(1, "1Q84", "Haruki Murakami", 1350, "https://m.media-amazon.com/images/I/71oaO3Pxx-L._AC_UF1000,1000_QL80_.jpg",
                "A work of maddening brilliance", "Long Desc"));
        books.add(new Book(2, "Diary of a Wimpy Kid", "Jeff Kinney", 217, "https://upload.wikimedia.org/wikipedia/en/8/84/Diary_of_a_Wimpy_Kid_book_cover.jpg",
                "An educational masterpiece", "Long Desc"));

        // Get a SharedPreferences editor to make changes to the stored data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Create a Gson object for converting Java objects to JSON
        Gson gson = new Gson();
        // Convert the list of books (books) to a JSON string and save it in SharedPreferences using the specified key
        editor.putString(ALL_BOOKS_KEY, gson.toJson(books));
        editor.commit();


    }

    public ArrayList<Book> getAllBooks() {
        Gson gson = new Gson();
        // Creates a Type object representing a generic ArrayList<Book> using TypeToken.
        // This is necessary for correctly deserializing a JSON array of Book objects with Gson.
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(ALL_BOOKS_KEY, null), type);
        return books;
    }

    public ArrayList<Book> getAlreadyReadBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(ALREADY_READ_BOOKS, null), type);
        return books;
    }

    public ArrayList<Book> getWantToReadBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(WANT_TO_READ_BOOKS, null), type);
        return books;
    }

    public ArrayList<Book> getCurrentlyReadingBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(CURRENTLY_READING_BOOKS, null), type);
        return books;
    }

    public ArrayList<Book> getFavoriteBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(FAVORITE_BOOKS, null), type);
        return books;
    }

    public Book getBookById(int id){
        ArrayList<Book> books = getAllBooks();
        if (null!=books){
            for (Book b: books){
                if (b.getId() == id){
                    return b;
        }
    }}        return null;
    }

    public boolean addToAlreadyRead(Book book){
        ArrayList<Book> books = getAlreadyReadBooks();
        if(null!=books){
            if(books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALREADY_READ_BOOKS);
                editor.putString(ALREADY_READ_BOOKS, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addToWantToRead(Book book){
        ArrayList<Book> books = getWantToReadBooks();
        if(null!=books){
            if(books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(WANT_TO_READ_BOOKS);
                editor.putString(WANT_TO_READ_BOOKS, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }


    public boolean addToCurrentlyReading(Book book) {
        ArrayList<Book> books = getCurrentlyReadingBooks();
        if(null!=books){
            if(books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(CURRENTLY_READING_BOOKS);
                editor.putString(CURRENTLY_READING_BOOKS, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;       }

    public boolean addToFavorite(Book book) {
        ArrayList<Book> books = getFavoriteBooks();
        if(null!=books){
            if(books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(FAVORITE_BOOKS); // remove previous list to update later on
                editor.putString(FAVORITE_BOOKS, gson.toJson(books)); // update new list
                editor.commit();
                return true;
            }
        }
        return false;           }

    public boolean removeFromAlreadyRead(Book book){
        ArrayList<Book> books = getAlreadyReadBooks();
        if (null!=books){
            for (Book b: books){
                if (b.getId() == book.getId()){
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(ALREADY_READ_BOOKS);
                    editor.putString(ALREADY_READ_BOOKS, gson.toJson(books));
                    editor.commit();
                    return true;
                }
            }
        } return false;
    }

    public boolean removeFromCurrentlyReading(Book book){
        ArrayList<Book> books = getCurrentlyReadingBooks();
        if (null!=books){
            for (Book b: books){
                if (b.getId() == book.getId()){
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(CURRENTLY_READING_BOOKS);
                    editor.putString(CURRENTLY_READING_BOOKS, gson.toJson(books));
                    editor.commit();
                    return true;
                }
            }
        } return false;    }
    public boolean removeFromWantToRead(Book book){
        ArrayList<Book> books = getWantToReadBooks();
        if (null!=books){
            for (Book b: books){
                if (b.getId() == book.getId()){
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(WANT_TO_READ_BOOKS);
                    editor.putString(WANT_TO_READ_BOOKS, gson.toJson(books));
                    editor.commit();
                    return true;
                }
            }
        } return false;
    }
    public boolean removeFromFavorites(Book book){
        ArrayList<Book> books = getFavoriteBooks();
        if (null!=books){
            for (Book b: books){
                if (b.getId() == book.getId()){
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(FAVORITE_BOOKS);
                    editor.putString(FAVORITE_BOOKS, gson.toJson(books));
                    editor.commit();
                    return true;
                }
            }
        } return false;    }
}


