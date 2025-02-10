package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> bookList = new ArrayList<Book>();
    private List<Author> authorList = new ArrayList<Author>();

    /**
     * Returns the list of books in the library.
     *
     * @return a list of Book objects representing the books in the library.
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Returns the list of authors in the library.
     *
     * @return a list of Author objects representing the authors in the library.
     */
    public List<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Adds a book to the library. If the book is not already in the library,
     * it is added to the book list and the database is updated.
     *
     * @param book the Book object to be added to the library.
     */
    public void addBook(Book book) {
        if (!bookList.contains(book)) {
            bookList.add(book);
            BookDatabaseManager.createBook(book, this);
        }
    }

    /**
     * Adds an author to the library. If the author is not already in the library,
     * the author is added to the author list and the database is updated.
     *
     * @param author the Author object to be added to the library.
     */
    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            BookDatabaseManager.createAuthor(author, this);
        }
    }

    /**
     * Retrieves a book from the library by its ISBN.
     *
     * @param isbn the ISBN of the book to be retrieved.
     * @return the Book object with the matching ISBN, or {@code null} if no such book exists.
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
     * Retrieves an author from the library by their author ID.
     *
     * @param authorID the ID of the author to be retrieved.
     * @return the {@link Author} object with the matching author ID, or {@code null} if no such author exists.
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
     * Retrieves a list of all author IDs in the library.
     *
     * @return a list of author IDs.
     */
    public List<Integer> getAuthorIDs() {
        List<Integer> authorIDs = new ArrayList<>();
        for (Author author : authorList) {
            authorIDs.add(author.getAuthorID());
        }
        return authorIDs;
    }

    /**
     * Updates the details of a book in the library by its ISBN.
     *
     * @param isbn the ISBN of the book to be updated.
     * @param book the Book object containing the updated details.
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
     * Updates the details of an author in the library by their author ID.
     *
     * @param authorID the ID of the author to be updated.
     * @param author the Author object containing the updated details.
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
     * Deletes a book from the library by its ISBN.
     *
     * @param isbn the ISBN of the book to be deleted.
     */
    public void deleteBook(String isbn) {
        Book currentBook = getBook(isbn);
        if (currentBook != null) {
            bookList.remove(currentBook);
        }
    }

    /**
     * Deletes an author from the library by their author ID.
     *
     * @param authorID the ID of the author to be deleted.
     */
    public void deleteAuthor(int authorID) {
        Author currentAuthor = getAuthor(authorID);
        if (currentAuthor != null) {
            authorList.remove(currentAuthor);
        }
    }
}
