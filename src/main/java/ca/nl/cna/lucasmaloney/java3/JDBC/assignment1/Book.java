package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private final String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    /**
     * Constructs a Book object
     *
     * @param isbn          The International Standard Book Number (ISBN)
     * @param title         The title
     * @param editionNumber The edition number
     * @param copyright     The copyright information
     */
    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    /**
     * Gets the ISBN
     * @return The ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the title
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the edition number
     * @return The edition number
     */
    public int getEditionNumber() {
        return editionNumber;
    }

    /**
     * Gets the copyright information
     * @return The copyright information
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Gets the list of authors associated with the book
     * @return A list of authors for this book
     */
    public List<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Sets the title
     * @param title The new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the edition number
     * @param editionNumber The new edition number
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    /**
     * Sets the copyright information
     * @param copyright The new copyright information
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Adds an author to the book. If the author is not already associated with this book,
     * it also adds the book to the author's list
     * @param author The author
     */
    public void addAuthor(Author author) {
        this.authorList.add(author);

        // Ensure relationships are maintained
        if (!author.getBookList().contains(this)) {
            author.addBook(this);
        }
    }
}
