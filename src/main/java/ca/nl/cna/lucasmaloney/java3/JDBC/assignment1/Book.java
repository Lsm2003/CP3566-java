package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<Author>();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public String getCopyright() {
        return copyright;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void addAuthor(Author author) {
        this.authorList.add(author);

        if(!author.getBookList().contains(this)) {
            author.addBook(this);
        }
    }
}
