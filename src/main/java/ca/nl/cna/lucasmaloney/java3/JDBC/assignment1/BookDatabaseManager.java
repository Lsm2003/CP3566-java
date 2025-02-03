package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import ca.nl.cna.lucasmaloney.java3.JDBC.DatabaseProperties;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseManager {

    public static final String DB_NAME = "books";

    public static void loadLibrary(Library lib) {
        String BOOKS_QUERY = "SELECT isbn, title, editionNumber, copyright FROM TITLES";
        String AUTHORS_QUERY = "SELECT authorID, firstName, lastName FROM AUTHORS";
        String RELATIONSHIP_QUERY = "SELECT b.authorID, a.firstName, a.lastName, b.isbn, c.title, c.editionNumber, c.copyright FROM\n" +
                "authors a JOIN authorisbn b ON a.authorID = b.authorID\n" +
                "JOIN titles c ON c.isbn = b.isbn ";

        // Load all books from the titles table
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(BOOKS_QUERY);

            // Extract all book data
            while (rs.next()) {
                Book currentBook = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                lib.getBookList().add(currentBook);

//                System.out.println("=".repeat(20));
//                System.out.println("ISBN: " + currentBook.getIsbn());
//                System.out.println("Title: " + currentBook.getTitle());
//                System.out.println("Edition Number: " + currentBook.getEditionNumber());
//                System.out.println("Copyright: " + currentBook.getCopyright());

                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load all authors
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(AUTHORS_QUERY);

            // Extract all book data
            while (rs.next()) {
                Author currentAuthor = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                lib.getAuthorList().add(currentAuthor);

//                System.out.println("=".repeat(20));
//                System.out.println("Author ID: " + currentAuthor.getAuthorID());
//                System.out.println("First Name: " + currentAuthor.getFirstName());
//                System.out.println("Last Name: " + currentAuthor.getLastName());

                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load relationships
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(RELATIONSHIP_QUERY);

            while (rs.next()) {
                int authorID = rs.getInt("authorID");
                String isbn = rs.getString("isbn");

                if (lib.getBook(isbn) != null) {
                    Book currentBook = lib.getBook(isbn);
                    currentBook.addAuthor(new Author(authorID, rs.getString("firstName"), rs.getString("lastName")));
                }

                if (lib.getAuthor(authorID) != null) {
                    Author currentAuthor = lib.getAuthor(authorID);
                    currentAuthor.addBook(new Book(isbn, rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright")));
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE methods
    public static void createBook(Book book) {
        String CREATE_BOOK_QUERY = "INSERT INTO titles VALUES (?, ?, ?, ?)";
        System.out.println(CREATE_BOOK_QUERY);
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_BOOK_QUERY);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEditionNumber());
            pstmt.setString(4, book.getCopyright());
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Created book successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating book");
        }
    }

    public static void createAuthor(Author author) {
        String CREATE_AUTHOR_QUERY = "INSERT INTO authors VALUES (?, ?, ?)";
        System.out.println(CREATE_AUTHOR_QUERY);
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_AUTHOR_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, author.getFirstName());
            pstmt.setString(3, author.getLastName());
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Created author successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating author");
        }
    }

    public static void createRelation(Book book, Author author) {
        String CREATE_RELATION_QUERY = "INSERT INTO authorisbn VALUES (?, ?)";
        System.out.println(CREATE_RELATION_QUERY);
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_RELATION_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, book.getIsbn());
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Created relation successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating relation");
        }
    }

    // GET Methods
    public static Book getBook(String isbn) {
        String GET_BOOK_QUERY = "SELECT * FROM titles WHERE isbn = ?";
        System.out.println(GET_BOOK_QUERY);

        Book book = null;
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_BOOK_QUERY);
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));

                System.out.println("Successfully retrieved book!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting book");
        }

        return book;
    }

    public static List<Book> getAllBooks() {
        String GET_BOOKS_QUERY = "SELECT * FROM titles";
        System.out.println(GET_BOOKS_QUERY);

        List<Book> books = new ArrayList<Book>();
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_BOOKS_QUERY);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = null;
                book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                books.add(book);

                System.out.println("Successfully retrieved: " + rs.getString("title"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting books");
        }

        return books;
    }

    public static Author getAuthor(String authorID) {
        String GET_AUTHOR_QUERY = "SELECT * FROM authors WHERE authorID = ?";
        System.out.println(GET_AUTHOR_QUERY);

        Author author = null;
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_AUTHOR_QUERY);
            pstmt.setString(1, authorID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));

                System.out.println("Successfully retrieved authors!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting authors");
        }

        return author;
    }

    public static List<Author> getAllAuthors() {
        String GET_AUTHORS_QUERY = "SELECT * FROM authors";
        System.out.println(GET_AUTHORS_QUERY);

        List<Author> authors = new ArrayList<Author>();
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_AUTHORS_QUERY);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Author author = null;
                author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                authors.add(author);

                System.out.println("Successfully retrieved: " + rs.getString("firstName") + " " + rs.getString("lastName"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting authors");
        }

        return authors;
    }

    // UPDATE Methods
    public static void updateBook(String isbn, Book book) {
        String UPDATE_BOOK_QUERY = "UPDATE titles SET isbn = ?, title = ?, editionNumber = ?, copyright = ? WHERE isbn = ?";
        System.out.println(UPDATE_BOOK_QUERY);

        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK_QUERY);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEditionNumber());
            pstmt.setString(4, book.getCopyright());
            pstmt.setString(5, isbn);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Successfully updated book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating book");
        }
    }

    public static void updateAuthor(int authorID, Author author) {
        String UPDATE_BOOK_QUERY = "UPDATE authors SET authorID = ?, firstName = ?, lastName = ? WHERE authorID = ?";
        System.out.println(UPDATE_BOOK_QUERY);

        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, author.getFirstName());
            pstmt.setString(3, author.getLastName());
            pstmt.setInt(4, authorID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Successfully updated book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating book");
        }
    }

    // DELETE Methods
    public static void deleteBook(String isbn) {
        String DELETE_BOOK_QUERY = "DELETE FROM titles WHERE isbn = ?";
        System.out.println(DELETE_BOOK_QUERY);

        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOK_QUERY);
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Successfully deleted book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting book");
        }
    }

    public static void deleteAuthor(String authorID) {
        String DELETE_AUTHOR_QUERY = "DELETE FROM authors WHERE authorID = ?";
        System.out.println(DELETE_AUTHOR_QUERY);

        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_AUTHOR_QUERY);
            pstmt.setString(1, authorID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Successfully deleted author!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting author");
        }
    }

    public static void deleteRelation(Book book, Author author) {
        String DELETE_RELATION_QUERY = "DELETE FROM authorisbn WHERE authorID = ? AND isbn = ?";
        System.out.println(DELETE_RELATION_QUERY);
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_RELATION_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, book.getIsbn());
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Deleted relation successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting relation");
        }
    }
}