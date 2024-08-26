package com.bdong5.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Utils {

    public interface FirebaseCallback<T> {
        void onCallback(T result);

        void onFailure(Exception e);
    }


//    private static final String ALL_BOOKS_KEY = "all_books";
//    private static final String ALREADY_READ_BOOKS = "already_read_books";
//    private static final String WANT_TO_READ_BOOKS = "want_to_read_books";
//    private static final String CURRENTLY_READING_BOOKS = "currently_reading_books";
//    private static final String FAVORITE_BOOKS = "favorite_books";

    private static volatile Utils instance;
    //    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private CollectionReference booksRef;


    private Utils(Context context) {

        // Initialize Firebase Firestore (db)
        db = FirebaseFirestore.getInstance();
        booksRef = db.collection("books");
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

        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("1", "1Q84", "Haruki Murakami", 1350, "https://m.media-amazon.com/images/I/71oaO3Pxx-L._AC_UF1000,1000_QL80_.jpg",
                "A work of maddening brilliance", "Long Desc"));
        books.add(new Book("2", "Diary of a Wimpy Kid", "Jeff Kinney", 217, "https://upload.wikimedia.org/wikipedia/en/8/84/Diary_of_a_Wimpy_Kid_book_cover.jpg",
                "An educational masterpiece", "Long Desc"));


        // Save each book to Firestore instead of using SharedPreferences
        for (Book b : books) {
            booksRef.document(String.valueOf(b.getId())).set(b);
        }


    }

    /**
     * Retrieves all books from the Firestore database.
     *
     * @param callback A FirebaseCallback that will handle the result (success or failure) of the operation.
     */
    public void getAllBooks(final FirebaseCallback<ArrayList<Book>> callback) {
        // Fetch the documents from the "books" collection in Firestore
        booksRef.get().addOnCompleteListener(task -> {
            // Check if the task was successful
            if (task.isSuccessful()) {
                // Create an ArrayList to hold the retrieved Book objects
                ArrayList<Book> books = new ArrayList<>();

                // Iterate through each document in the task's result
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Convert each document into a Book object and add it to the list
                    books.add(document.toObject(Book.class));
                }

                // Pass the list of books to the callback's onCallback method
                callback.onCallback(books);
            } else {
                // If the task failed, pass the exception to the callback's onFailure method
                callback.onFailure(task.getException());
            }
        });
    }

    public void getAlreadyReadBooks(final FirebaseCallback<ArrayList<Book>> callback) {
        getAllBooks(new FirebaseCallback<ArrayList<Book>>() {
            @Override
            public void onCallback(ArrayList<Book> result) {
                ArrayList<Book> alreadyReadBooks = new ArrayList<>();

                AtomicInteger tasksCompleted = new AtomicInteger(0);
                // AtomicInteger is used to safely count the number of completed asynchronous tasks


                for (Book book : result) {
                    DocumentReference bookRef = booksRef.document(book.getId());
                    CollectionReference alreadyReadRef = bookRef.collection("already_read");

                    // Find all "already_read" within collection
                    alreadyReadRef.get().addOnCompleteListener(subTask -> {
                        if (subTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subDoc : subTask.getResult()) {
                                alreadyReadBooks.add(subDoc.toObject(Book.class));
                            }

                            // Increment the task counter and check if all tasks are complete
                            if (tasksCompleted.incrementAndGet() == result.size()) {
                                callback.onCallback(alreadyReadBooks);
                            }
                        } else {
                            callback.onFailure(subTask.getException());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e); // Handle errors from getAllBooks
            }
        });
    }

    public void getWantToReadBooks(final FirebaseCallback<ArrayList<Book>> callback) {
        getAllBooks(new FirebaseCallback<ArrayList<Book>>() {
            @Override
            public void onCallback(ArrayList<Book> result) {
                ArrayList<Book> wantToReadBooks = new ArrayList<>();

                AtomicInteger tasksCompleted = new AtomicInteger(0);
                // AtomicInteger is used to safely count the number of completed asynchronous tasks

                for (Book book : result) {
                    DocumentReference bookRef = booksRef.document(book.getId());
                    CollectionReference wantToReadRef = bookRef.collection("want_to_read");

                    wantToReadRef.get().addOnCompleteListener(subTask -> {
                        if (subTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subDoc : subTask.getResult()) {
                                wantToReadBooks.add(subDoc.toObject(Book.class));
                            }

                            // Increment the task counter and check if all tasks are complete
                            if (tasksCompleted.incrementAndGet() == result.size()) {
                                callback.onCallback(wantToReadBooks);
                            }
                        } else {
                            callback.onFailure(subTask.getException());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e); // Handle errors from getAllBooks
            }
        });
    }


    public void getCurrentlyReadingBooks(final FirebaseCallback<ArrayList<Book>> callback) {
        getAllBooks(new FirebaseCallback<ArrayList<Book>>() {
            @Override
            public void onCallback(ArrayList<Book> result) {
                ArrayList<Book> currentlyReadingBooks = new ArrayList<>();

                AtomicInteger tasksCompleted = new AtomicInteger(0);
                // AtomicInteger is used to safely count the number of completed asynchronous tasks

                for (Book book : result) {
                    DocumentReference bookRef = booksRef.document(book.getId());
                    CollectionReference currentlyReadingRef = bookRef.collection("currently_reading");

                    currentlyReadingRef.get().addOnCompleteListener(subTask -> {
                        if (subTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subDoc : subTask.getResult()) {
                                currentlyReadingBooks.add(subDoc.toObject(Book.class));
                            }

                            // Increment the task counter and check if all tasks are complete
                            if (tasksCompleted.incrementAndGet() == result.size()) {
                                callback.onCallback(currentlyReadingBooks);
                            }
                        } else {
                            callback.onFailure(subTask.getException());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e); // Handle errors from getAllBooks
            }
        });
    }

    public void getFavoriteBooks(final FirebaseCallback<ArrayList<Book>> callback) {
        getAllBooks(new FirebaseCallback<ArrayList<Book>>() {
            @Override
            public void onCallback(ArrayList<Book> result) {
                ArrayList<Book> favoriteBooks = new ArrayList<>();

                AtomicInteger tasksCompleted = new AtomicInteger(0);
                // AtomicInteger is used to safely count the number of completed asynchronous tasks

                for (Book book : result) {
                    DocumentReference bookRef = booksRef.document(book.getId());
                    CollectionReference favoriteRef = bookRef.collection("favorite");

                    favoriteRef.get().addOnCompleteListener(subTask -> {
                        if (subTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subDoc : subTask.getResult()) {
                                favoriteBooks.add(subDoc.toObject(Book.class));
                            }

                            // Increment the task counter and check if all tasks are complete
                            if (tasksCompleted.incrementAndGet() == result.size()) {
                                callback.onCallback(favoriteBooks);
                            }
                        } else {
                            callback.onFailure(subTask.getException());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e); // Handle errors from getAllBooks
            }
        });
    }

    public void getBookById(String bookId, final FirebaseCallback<Book> callback) {
        booksRef.document(bookId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Book book = task.getResult().toObject(Book.class); // convert to Book object
                    callback.onCallback(book);
                } else {
                    // No document with bookId exists
                    callback.onCallback(null);
                }
            } else {
                callback.onFailure(task.getException());
            }
        }); // reference a specific document within booksRef with matching bookId
    }

    public void addToWantToRead(Book book) {

        DocumentReference bookRef = booksRef.document(book.getId()); // Reference specific book by bookId
        CollectionReference wantToReadRef = bookRef.collection("want_to_read"); // reference specific collection


        wantToReadRef.add(book).addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Book added to want_to_read subcollection with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding book to want_to_read subcollection", e);
        });
    }

    public void addToAlreadyRead(Book book){

        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference alreadyReadRef = bookRef.collection("already_read");

        alreadyReadRef.add(book).addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Book added to already_read subcollection with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding book to already_read subcollection", e);
        });
    }


   public void addToCurrentlyReading(Book book){
        DocumentReference bookref = booksRef.document(book.getId());
        CollectionReference currentlyReadingRef = bookref.collection("currently_reading");
        currentlyReadingRef.add(book).addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Book added to currently_reading subcollection with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding book to currently_reading subcollection", e);
        });
   }

    public void addToFavorite(Book book){
        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference favoriteRef = bookRef.collection("favorite");

        favoriteRef.add(book).addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Book added to favorite subcollection with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding book to favorite subcollection", e);
        });
    }

    public void removeFromAlreadyRead(Book book){
        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference alreadyReadRef = bookRef.collection("already_read");

        alreadyReadRef.document(book.getId()).delete().addOnSuccessListener(aVoid->{
            Log.d("Firestore", "Book successfully removed from subcollection");
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Unable to remove book from subcollection", e);
                });
    }

    public void removeFromCurrentlyReading(Book book){
        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference currentlyReadingRef = bookRef.collection("currently_reading");

        currentlyReadingRef.document(book.getId()).delete().addOnCompleteListener(aVoid->{
            Log.d("Firestore", "Book successfully removed from subcollection");
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Unable to remove book from subcollection", e);
        });
    }

    public void removeFromWantToRead(Book book){
        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference wantToReadRef = bookRef.collection("want_to_read");

        wantToReadRef.document(book.getId()).delete().addOnCompleteListener(aVoid->{
            Log.d("Firestore", "Book successfully removed from subcollection");
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Unable to remove book from subcollection", e);
        });
    }

    public void removeFromFavorites(Book book){
        DocumentReference bookRef = booksRef.document(book.getId());
        CollectionReference favoriteRef = bookRef.collection("favorite");

        favoriteRef.document(book.getId()).delete().addOnSuccessListener(aVoid->{
            Log.d("Firestore", "Book successfully removed from subcollection");
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Unable to remove book from subcollection", e);
        });
    }
}


