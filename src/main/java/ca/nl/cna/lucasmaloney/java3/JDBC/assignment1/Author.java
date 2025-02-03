package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        bookList = new ArrayList<Book>();
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public Book getBook(String isbn) {
        for (Book book : bookList) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addBook(Book book) {
        this.bookList.add(book);

        if(!book.getAuthorList().contains(this)) {
            book.addAuthor(this);
        }
    }
}
