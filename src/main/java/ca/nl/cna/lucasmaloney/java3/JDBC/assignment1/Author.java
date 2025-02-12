package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private final int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    /**
     * Constructs an Author object
     *
     * @param authorID  The unique identifier for the author
     * @param firstName The first name
     * @param lastName  The last name
     */
    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        bookList = new ArrayList<Book>();
    }

    /**
     * Gets the authorID
     * @return The authorID
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * Gets the first name.
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the list of books written by the author
     * @return A list of books written by this author
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Sets the first name
     * @param firstName The new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name
     * @param lastName The new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Adds a book to the author's book list
     * If the book does not already have this author in its author list, it adds this author to the book as well
     * @param book The book
     */
    public void addBook(Book book) {
        this.bookList.add(book);

        // Ensure relationships are maintained
        if (!book.getAuthorList().contains(this)) {
            book.addAuthor(this);
        }
    }
}
