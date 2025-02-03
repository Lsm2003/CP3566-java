package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private final int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    /**
     * Constructs an Author object with the given ID, first name, and last name.
     *
     * @param authorID  The unique identifier for the author.
     * @param firstName The first name of the author.
     * @param lastName  The last name of the author.
     */
    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        bookList = new ArrayList<Book>();
    }

    /**
     * Gets the unique author ID.
     *
     * @return The author's unique ID.
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * Gets the author's first name.
     *
     * @return The first name of the author.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the author's last name.
     *
     * @return The last name of the author.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the list of books written by the author.
     *
     * @return A list of books authored by this author.
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Sets the author's first name.
     *
     * @param firstName The new first name of the author.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the author's last name.
     *
     * @param lastName The new last name of the author.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Adds a book to the author's book list.
     * If the book does not already have this author in its author list, it adds this author to the book as well.
     *
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        this.bookList.add(book);

        // Ensure relationships are maintained
        if (!book.getAuthorList().contains(this)) {
            book.addAuthor(this);
        }
    }
}
