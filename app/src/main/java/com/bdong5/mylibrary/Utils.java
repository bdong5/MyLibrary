package com.bdong5.mylibrary;

import java.util.ArrayList;

public class Utils {

    private static volatile Utils instance;

    private static ArrayList<Book> allBooks = new ArrayList<>();
    private static ArrayList<Book> alreadyReadBooks = new ArrayList<>();
    private static ArrayList<Book> wantToReadBooks = new ArrayList<>();
    private static ArrayList<Book> currentlyReadingBooks = new ArrayList<>();
    private static ArrayList<Book> favoriteBooks = new ArrayList<>();

    private Utils() {
        initData();
    }

    public static Utils getInstance() {
        if (instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils();
                }
            }
        }
        return instance;
    }

    private void initData() {
        //TODO: add initial data
        allBooks.add(new Book(1, "1Q84", "Haruki Murakami", 1350, "https://m.media-amazon.com/images/I/71oaO3Pxx-L._AC_UF1000,1000_QL80_.jpg",
                "A work of maddening brilliance", "Long Description"));
        allBooks.add(new Book(2, "Diary of a Wimpy Kid", "Jeff Kinney", 217, "https://upload.wikimedia.org/wikipedia/en/8/84/Diary_of_a_Wimpy_Kid_book_cover.jpg",
                "An educational masterpiece", "Long Desc"));
    }

    public static ArrayList<Book> getAllBooks() {
        return allBooks;
    }

    public static ArrayList<Book> getAlreadyReadBooks() {
        return alreadyReadBooks;
    }

    public static ArrayList<Book> getWantToReadBooks() {
        return wantToReadBooks;
    }

    public static ArrayList<Book> getCurrentlyReadingBooks() {
        return currentlyReadingBooks;
    }

    public static ArrayList<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public Book getBookById(int id){
        for (Book b: allBooks){
            if (b.getId() == id){
                return b;
            }
        }
        return null;
    }
}


