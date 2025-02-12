package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> bookList = new ArrayList<Book>();
    private List<Author> authorList = new ArrayList<Author>();

    /**
     * Returns list of books in the library
     * @return list of Book objects
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Returns list of authors
     * @return list of Author objects
     */
    public List<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Adds a book to the library
     * @param book the Book object to be added
     */
    public void addBook(Book book) {
        if (!bookList.contains(book)) {
            bookList.add(book);
            BookDatabaseManager.createBook(book, this);
        }
    }

    /**
     * Adds an author to the library
     * @param author the Author object to be added
     */
    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            BookDatabaseManager.createAuthor(author, this);
        }
    }

    /**
     * Retrieves a book from the library by its ISBN
     * @param isbn the ISBN of the book
     * @return the Book object with the matching ISBN
     */
    public Book getBook(String isbn) {
        for (Book book : bookList) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Retrieves an author from the library by their authorID
     *
     * @param authorID the ID of the author
     * @return the Author object with the matching authorID
     */
    public Author getAuthor(int authorID) {
        for (Author author : authorList) {
            if (author.getAuthorID() == authorID) {
                return author;
            }
        }
        return null;
    }

    /**
     * Retrieves list of all authorIDs in the library
     * @return a list of authorIDs
     */
    public List<Integer> getAuthorIDs() {
        List<Integer> authorIDs = new ArrayList<>();
        for (Author author : authorList) {
            authorIDs.add(author.getAuthorID());
        }
        return authorIDs;
    }

    /**
     * Updates a book in the library by its ISBN
     *
     * @param isbn the ISBN of the book
     * @param book the Book object containing the updated details
     */
    public void setBook(String isbn, Book book) {
        Book currentBook = getBook(isbn);
        if (currentBook != null) {
            currentBook.setTitle(book.getTitle());
            currentBook.setEditionNumber(book.getEditionNumber());
            currentBook.setCopyright(book.getCopyright());
        }

        for (Author author : authorList) {
            for (Book b : author.getBookList()) {
                if (b.getIsbn().equals(isbn)) {
                    b.setTitle(book.getTitle());
                    b.setEditionNumber(book.getEditionNumber());
                    b.setCopyright(book.getCopyright());
                }
            }
        }
    }

    /**
     * Updates the details of an author by their author ID.
     *
     * @param authorID the ID of the author
     * @param author the Author object containing the updated details
     */
    public void setAuthor(int authorID, Author author) {
        Author currentAuthor = getAuthor(authorID);

        if (currentAuthor != null) {
            currentAuthor.setFirstName(author.getFirstName());
            currentAuthor.setLastName(author.getLastName());
        }

        for (Book book : bookList) {
            for (Author a : book.getAuthorList()) {
                if (a.getAuthorID() == (authorID)) {
                    a.setFirstName(author.getFirstName());
                    a.setLastName(author.getLastName());
                }
            }
        }
    }

    /**
     * Deletes a book by its ISBN
     * @param isbn the ISBN of the book to be deleted
     */
    public void deleteBook(String isbn) {
        Book currentBook = getBook(isbn);
        if (currentBook != null) {
            bookList.remove(currentBook);
        }
    }

    /**
     * Deletes an author by their authorID
     * @param authorID the ID of the author
     */
    public void deleteAuthor(int authorID) {
        Author currentAuthor = getAuthor(authorID);
        if (currentAuthor != null) {
            authorList.remove(currentAuthor);
        }
    }
}
