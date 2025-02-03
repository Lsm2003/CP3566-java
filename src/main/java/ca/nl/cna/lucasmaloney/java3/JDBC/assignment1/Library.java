package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> bookList = new ArrayList<Book>();
    private List<Author> authorList = new ArrayList<Author>();

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void addBook(Book book) {
        if(!bookList.contains(book)) {
            bookList.add(book);
            BookDatabaseManager.createBook(book);
        }
    }

    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            BookDatabaseManager.createAuthor(author);
        }
    }

    public Book getBook(String isbn) {
        for (Book book : bookList) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    };

    public Author getAuthor(int authorID) {
        for (Author author : authorList) {
            if (author.getAuthorID() == authorID ) {
                return author;
            }
        }
        return null;
    }

    public List<Integer> getAuthorIDs() {
        List<Integer> authorIDs = new ArrayList<>();
        for (Author author : authorList) {
            authorIDs.add(author.getAuthorID());
        }
        return authorIDs;
    }

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
}
