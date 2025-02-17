package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import ca.nl.cna.lucasmaloney.java3.JDBC.DatabaseProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseManager {

    public static final String DB_NAME = "books";

    /**
     * Loads data from database and populates the library.
     * @param lib The library object to be populated
     */
    public static void loadLibrary(Library lib) {
        String BOOKS_QUERY = "SELECT isbn, title, editionNumber, copyright FROM TITLES";
        String AUTHORS_QUERY = "SELECT authorID, firstName, lastName FROM AUTHORS";
        String RELATIONSHIP_QUERY = "SELECT b.authorID, a.firstName, a.lastName, b.isbn, c.title, c.editionNumber, c.copyright FROM\n" + "authors a JOIN authorisbn b ON a.authorID = b.authorID\n" + "JOIN titles c ON c.isbn = b.isbn ";

        // Load books from titles table
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(BOOKS_QUERY);

            // add books to the library
            while (rs.next()) {
                Book currentBook = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                lib.getBookList().add(currentBook);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load authors from authors table
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(AUTHORS_QUERY);

            // Extract and add authors to the library
            while (rs.next()) {
                Author currentAuthor = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                lib.getAuthorList().add(currentAuthor);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load relationships between authors and books
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(RELATIONSHIP_QUERY);

            // Extract and establish relationships between books and authors
            while (rs.next()) {
                int authorID = rs.getInt("authorID");
                String isbn = rs.getString("isbn");

                // Retrieve book and author from the library
                Book currentBook = lib.getBook(isbn);
                Author currentAuthor = lib.getAuthor(authorID);

                // Add relationships if the book and author exist
                currentBook.addAuthor(currentAuthor);
                currentAuthor.addBook(currentBook);

            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE methods

    /**
     * adds new book to the database. Adds relationship between book and its author
     *
     * @param book The book object containing the details of the book to be added
     * @param lib The library object containing books and authors
     */
    public static void createBook(Book book, Library lib) {
        String CREATE_BOOK_QUERY = "INSERT INTO titles VALUES (?, ?, ?, ?)";
        System.out.println(CREATE_BOOK_QUERY);
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_BOOK_QUERY);

            // Set query parameters with book details
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEditionNumber());
            pstmt.setString(4, book.getCopyright());

            pstmt.executeUpdate();

            System.out.println("New Book Created");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating book");
        }

        for (Author author : lib.getAuthorList()) {
            if (author.getBookList().contains(book)) {
                createRelation(book, author);
            }
        }
    }

    /**
     * adds new author record to database
     *
     * @param author The Author object containing the details of the author to be added
     * @param lib The library object containing books and authors
     */
    public static void createAuthor(Author author, Library lib) {
        String CREATE_AUTHOR_QUERY = "INSERT INTO authors VALUES (?, ?, ?)";
        System.out.println(CREATE_AUTHOR_QUERY);
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_AUTHOR_QUERY);

            // Set query parameters with author details
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, author.getFirstName());
            pstmt.setString(3, author.getLastName());

            pstmt.executeUpdate();

            System.out.println("Created author successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating author");
        }

        for (Book book : lib.getBookList()) {
            if (book.getAuthorList().contains(author)) {
                createRelation(book, author);
            }
        }
    }


    /**
     * Creates relationship between book and author
     *
     * @param book   The Book object representing the book
     * @param author The Author object representing the author
     */
    public static void createRelation(Book book, Author author) {
        String CREATE_RELATION_QUERY = "INSERT INTO authorisbn VALUES (?, ?)";
        System.out.println(CREATE_RELATION_QUERY);
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(CREATE_RELATION_QUERY);

            // Set query parameters with author ID and book ISBN
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, book.getIsbn());

            pstmt.executeUpdate();

            System.out.println("Created relation");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating relation");
        }
    }


    // GET Methods

    /**
     * Retrieves a book using ISBN
     *
     * @param isbn The ISBN of the book to retrieve
     * @return The Book object if found (null by default)
     */
    public static Book getBook(String isbn) {
        String GET_BOOK_QUERY = "SELECT * FROM titles WHERE isbn = ?";
        System.out.println(GET_BOOK_QUERY);

        Book book = null;
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_BOOK_QUERY);

            // Set query parameter with the provided ISBN
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            // If a book is found, create Book object
            if (rs.next()) {
                book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("editionNumber"),
                        rs.getString("copyright")
                );
                System.out.println("Successfully retrieved book!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting book");
        }

        return book;
    }


    /**
     * Retrieves all books from the database
     * @return A list of Book objects
     */
    public static List<Book> getAllBooks() {
        String GET_BOOKS_QUERY = "SELECT * FROM titles";
        System.out.println(GET_BOOKS_QUERY);

        List<Book> books = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_BOOKS_QUERY);
            ResultSet rs = pstmt.executeQuery();

            // Iterate through result set and create Book objects
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("editionNumber"),
                        rs.getString("copyright")
                );
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

    /**
     * Retrieves author using authorID
     *
     * @param authorID The ID of the author
     * @return The Author object if found (Default null)
     */
    public static Author getAuthor(String authorID) {
        String GET_AUTHOR_QUERY = "SELECT * FROM authors WHERE authorID = ?";
        System.out.println(GET_AUTHOR_QUERY);

        Author author = null;
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(GET_AUTHOR_QUERY);

            // Set query parameter with the provided author ID
            pstmt.setString(1, authorID);
            ResultSet rs = pstmt.executeQuery();

            // If an author is found, create an Author object
            if (rs.next()) {
                author = new Author(
                        rs.getInt("authorID"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                System.out.println("Successfully retrieved author!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting author");
        }

        return author;
    }


    /**
     * Retrieves all authors
     * @return a List of Author objects representing all authors retrieved from the database.
     */
    public static List<Author> getAllAuthors() {
        String GET_AUTHORS_QUERY = "SELECT * FROM authors";
        System.out.println(GET_AUTHORS_QUERY);

        List<Author> authors = new ArrayList<Author>();
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
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

    /**
     * Updates a book
     *
     * @param isbn the ISBN of the book
     * @param book the Book object containing updated book details
     */
    public static void updateBook(String isbn, Book book) {
        String UPDATE_BOOK_QUERY = "UPDATE titles SET isbn = ?, title = ?, editionNumber = ?, copyright = ? WHERE isbn = ?";
        System.out.println(UPDATE_BOOK_QUERY);

        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK_QUERY);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEditionNumber());
            pstmt.setString(4, book.getCopyright());
            pstmt.setString(5, isbn);
            pstmt.executeUpdate();

            System.out.println("Successfully updated book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating book");
        }
    }


    /**
     * Updates an author
     *
     * @param authorID the ID of the author
     * @param author the Author object containing the updated author details
     */
    public static void updateAuthor(int authorID, Author author) {
        String UPDATE_BOOK_QUERY = "UPDATE authors SET authorID = ?, firstName = ?, lastName = ? WHERE authorID = ?";
        System.out.println(UPDATE_BOOK_QUERY);

        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, author.getFirstName());
            pstmt.setString(3, author.getLastName());
            pstmt.setInt(4, authorID);
            pstmt.executeUpdate();

            System.out.println("Successfully updated book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating book");
        }
    }


    // DELETE Methods

    /**
     * Deletes a book
     *
     * @param book the book to be deleted
     * @param lib The library object containing books and authors
     */
    public static void deleteBook(Book book, Library lib) {
        String DELETE_BOOK_QUERY = "DELETE FROM titles WHERE isbn = ?";
        System.out.println(DELETE_BOOK_QUERY);

        for (Author author : lib.getAuthorList()) {
            for (Book currentBook: author.getBookList()) {
                if (currentBook.getIsbn().equals(book.getIsbn())) {
                    deleteRelation(book, author);
                }
            }
        }

        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOK_QUERY);
            pstmt.setString(1, book.getIsbn());
            pstmt.executeUpdate();

            System.out.println("Successfully deleted book!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting book");
        }


    }


    /**
     * Deletes an author
     *
     * @param author the author to be deleted
     * @param lib The library object containing books and authors
     */
    public static void deleteAuthor(Author author, Library lib) {
        String DELETE_AUTHOR_QUERY = "DELETE FROM authors WHERE authorID = ?";
        System.out.println(DELETE_AUTHOR_QUERY);

        for (Book book : lib.getBookList()) {
            for (Author currentAuthor: book.getAuthorList()) {
                if (currentAuthor.getAuthorID() == (author.getAuthorID())) {
                    deleteRelation(book, author);
                }
            }
        }

        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_AUTHOR_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.executeUpdate();

            System.out.println("Successfully deleted author!");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting author");
        }
    }


    /**
     * Deletes a relationship between a book and an author
     *
     * @param book the Book object representing the book
     * @param author the Author object representing the author
     */
    public static void deleteRelation(Book book, Author author) {
        String DELETE_RELATION_QUERY = "DELETE FROM authorisbn WHERE authorID = ? AND isbn = ?";
        System.out.println(DELETE_RELATION_QUERY);
        try {
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(DELETE_RELATION_QUERY);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, book.getIsbn());
            pstmt.executeUpdate();

            System.out.println("Deleted relation successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting relation");
        }
    }
}