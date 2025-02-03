package ca.nl.cna.lucasmaloney.java3.JDBC.assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class BookApplication {
    public static void printOptions() {
        System.out.println("=".repeat(20));
        System.out.println("0. Exit");
        System.out.println("1. Show All Books");
        System.out.println("2. Show All Authors");
        System.out.println("3. Edit Book by ISBN");
        System.out.println("4. Edit Author by ID");
        System.out.println("5. Add New Book");
        System.out.println("=".repeat(20));
    }

    public static void showAllBooks(Library lib) {
        for (Book book : lib.getBookList()) {
            System.out.print("Title: " + book.getTitle() + " " + "\nISBN: " + book.getIsbn() +
                    "\nEdition: " + book.getEditionNumber() + "\nCopyright: " + book.getCopyright());

            // Single or Multi author
            if (book.getAuthorList().size() > 1) {
                System.out.print("\nAuthors: ");
                for (int i = 0; i < book.getAuthorList().size(); i++) {
                    // Check for last author
                    if (i == book.getAuthorList().size() - 1) {
                        System.out.print(book.getAuthorList().get(i).getFirstName() + " " + book.getAuthorList().get(i).getLastName());
                    } else {
                        System.out.print(book.getAuthorList().get(i).getFirstName() + " " + book.getAuthorList().get(i).getLastName() + ", ");
                    }
                }
            } else {
                System.out.print("\nAuthor: " + book.getAuthorList().getFirst().getFirstName() + " " + book.getAuthorList().getFirst().getLastName());
            }


            System.out.println();
            System.out.println("-".repeat(20));
        }
    }

    public static void showAllAuthors(Library lib) {
        for (Author author : lib.getAuthorList()) {
            System.out.print("Name: " + author.getFirstName() + " " + author.getLastName() +
                    "\nAuthorID: " + author.getAuthorID());

            // Single or Multi author
            if (author.getBookList().size() > 1) {
                System.out.print("\nBooks: \n");
                for (int i = 0; i < author.getBookList().size(); i++) {
                    // Check for last book
                    if (i == author.getBookList().size() - 1) {
                        System.out.print("\t" + author.getBookList().get(i).getTitle());
                    } else {
                        System.out.print("\t" + author.getBookList().get(i).getTitle() + ", \n");
                    }
                }
            } else {
                System.out.print("\nBook: " + author.getBookList().getFirst().getTitle());
            }


            System.out.println();
            System.out.println("-".repeat(20));
        }
    }

    public static void editBook(Library lib, String editIsbn, String newTitle, int newEdition, String newCopyright) {
        Book currentBook = lib.getBook(editIsbn);
        Book newBook = new Book(editIsbn, newTitle, newEdition, newCopyright);

        if (newBook.getTitle().isEmpty()) {
            newBook.setTitle(currentBook.getTitle());
        }

        if (newBook.getEditionNumber() == -1) {
            newBook.setEditionNumber(currentBook.getEditionNumber());
        }

        if (newBook.getCopyright().isEmpty()) {
            newBook.setCopyright(currentBook.getCopyright());
        }

        lib.setBook(editIsbn, newBook);
        BookDatabaseManager.updateBook(editIsbn, newBook);
    }

    public static void editAuthor(Library lib, int authorID, String firstName, String lastName) {
        Author currentAuthor = lib.getAuthor(authorID);
        Author newAuthor = new Author(authorID, firstName, lastName);

        if (newAuthor.getFirstName().isEmpty()) {
            newAuthor.setFirstName(currentAuthor.getFirstName());
        }

        if (newAuthor.getLastName().isEmpty()) {
            newAuthor.setLastName(currentAuthor.getLastName());
        }

        lib.setAuthor(authorID, newAuthor);
        BookDatabaseManager.updateAuthor(authorID, newAuthor);
    }

    public static void addNewBook(Library lib, String newISBN, String newTitle, int newEdition, String newCopyright, List<Author> authorList) {
        Book newBook = new Book(newISBN, newTitle, newEdition, newCopyright);
        for (Author author : authorList) {
            newBook.addAuthor(author);
            lib.addAuthor(author);
        }

        lib.addBook(newBook);
        for (Author author : authorList) {
            BookDatabaseManager.createRelation(newBook, author);
        }
    }

    public static Author getNewBookAuthor(Library lib, List<Author> newAuthors) {
        Scanner sc = new Scanner(System.in);
        String authorID;
        String firstName;
        String lastName;

        System.out.println("Creating new Author");
        System.out.println("Enter AuthorID: ");
        authorID = sc.nextLine();
        while (lib.getAuthor(Integer.parseInt(authorID)) != null || authorID == "") {
            if (lib.getAuthor(Integer.parseInt(authorID)) != null) {
                System.out.println("AuthorID already exists");
            } else {
                System.out.println("AuthorID cannot be empty");
            }

            System.out.println("Enter AuthorID: ");
            authorID = sc.nextLine();
        }

        System.out.println("Enter Author First Name: ");
        firstName = sc.nextLine();
        while (firstName.isEmpty()) {
            System.out.print("Enter Author First Name: ");
            firstName = sc.nextLine().trim();
            if (firstName.isEmpty()) {
                System.out.println("First name cannot be blank. Please enter a valid first name.");
            }
        }

        System.out.println("Enter Author Last Name: ");
        lastName = sc.nextLine();
        while (lastName.isEmpty()) {
            System.out.print("Enter Author Last Name: ");
            lastName = sc.nextLine().trim();
            if (lastName.isEmpty()) {
                System.out.println("Last name cannot be blank. Please enter a valid last name.");
            }
        }

        Author newAuthor = new Author(Integer.parseInt(authorID), firstName, lastName);
        lib.addAuthor(newAuthor);
        return newAuthor;
    }

    public static Author getExistingBookAuthors(Library lib, List<Author> existingAuthors) {
        Scanner sc = new Scanner(System.in);
        String authorChoice = "";
        int authorID = -1;

        for (Author author : lib.getAuthorList()) {
            System.out.print("AuthorID: " + author.getAuthorID() + " ");
            System.out.println("Name: " + author.getFirstName() + " " + author.getLastName());
        }

        while (authorID == -1) {
            System.out.print("Enter AuthorID: ");
            authorChoice = sc.nextLine().trim();

            if (!authorChoice.isEmpty()) {
                try {
                    authorID = Integer.parseInt(authorChoice);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for AuthorID.");
                    authorID = -1; // Reset to keep the loop running
                }
                if (!lib.getAuthorIDs().contains(authorID)) {
                    System.out.println("AuthorID does not exist. Please enter an existing ID.");
                    authorID = -1;
                }
                for (Author author : existingAuthors) {
                    if (author.getAuthorID() == authorID) {
                        System.out.println("Error: Author already exists");
                        authorID = -1;
                    }
                }
            } else {
                System.out.println("AuthorID cannot be blank. Please enter a valid number.");
            }
        }

        return lib.getAuthor(Integer.parseInt(authorChoice));
    }

    public static void main(String[] args) {
        Library lib = new Library();
        BookDatabaseManager.loadLibrary(lib);

        printOptions();
        System.out.println("Choose an option: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        while (!Objects.equals(choice, "0")) {

            if (Objects.equals(choice, "0")) {
                System.out.println("Exiting...");
                break;
            } else if (Objects.equals(choice, "1")) {
                showAllBooks(lib);

            } else if (Objects.equals(choice, "2")) {
                showAllAuthors(lib);
            } else if (Objects.equals(choice, "3")) {
                System.out.println("Enter ISBN of book to edit: ");
                String editISBN = scanner.nextLine();

                System.out.println("Enter new title(or blank to keep original title): ");
                String newTitle = scanner.nextLine();

                System.out.println("Enter new edition number( or -1 to keep original edition number): ");
                int newEditionNumber = Integer.parseInt(scanner.nextLine());

                System.out.println("Enter new copyright(or blank to keep original copyright): ");
                String newCopyright = scanner.nextLine();

                editBook(lib, editISBN, newTitle, newEditionNumber, newCopyright);
            } else if (Objects.equals(choice, "4")) {
                System.out.println("Enter Author ID of author to edit: ");
                int editAuthorID = Integer.parseInt(scanner.nextLine());

                System.out.println("Enter new first name(or blank to keep original name): ");
                String newFirstName = scanner.nextLine();

                System.out.println("Enter new last name( or blank to keep original name): ");
                String newLastName = scanner.nextLine();

                editAuthor(lib, editAuthorID, newFirstName, newLastName);
            } else if (Objects.equals(choice, "5")) {
                Scanner sc = new Scanner(System.in);
                String newISBN = "";
                String newTitle = "";
                int newEditionNumber = -1;
                String newCopyright = "";

                // Validate ISBN (ensure it's not empty)
                while (newISBN.isEmpty()) {
                    System.out.print("Enter ISBN of new Book: ");
                    newISBN = sc.nextLine().trim();
                    if (newISBN.isEmpty()) {
                        System.out.println("ISBN cannot be blank. Please enter a valid ISBN.");
                    }
                }

                // Validate Title (ensure it's not empty)
                while (newTitle.isEmpty()) {
                    System.out.print("Enter new title: ");
                    newTitle = sc.nextLine().trim();
                    if (newTitle.isEmpty()) {
                        System.out.println("Title cannot be blank. Please enter a valid title.");
                    }
                }

                // Validate Edition Number (ensure it's a valid integer)
                while (newEditionNumber == -1) {
                    System.out.print("Enter new edition number: ");
                    String editionInput = sc.nextLine().trim();

                    if (!editionInput.isEmpty()) {
                        try {
                            newEditionNumber = Integer.parseInt(editionInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid integer for the edition number.");
                            newEditionNumber = -1; // Reset to keep the loop running
                        }
                    } else {
                        System.out.println("Edition number cannot be blank. Please enter a valid number.");
                    }
                }

                // Validate Copyright (ensure it's not empty)
                while (newCopyright.isEmpty()) {
                    System.out.print("Enter new copyright: ");
                    newCopyright = sc.nextLine().trim();
                    if (newCopyright.isEmpty()) {
                        System.out.println("Copyright cannot be blank. Please enter a valid copyright.");
                    }
                }

                List<Author> newAuthorList = new ArrayList<>();
                Author currentAuthor;
                String authorChoice = "";

                while (newAuthorList.isEmpty() || !Objects.equals(authorChoice, "0")) { // Ensures at least one author is added
                    System.out.println("=".repeat(20));
                    System.out.println("0. Exit");
                    System.out.println("1. Add existing author");
                    System.out.println("2. Add new author");
                    System.out.println("Choose an option: ");
                    System.out.println("=".repeat(20));
                    authorChoice = scanner.nextLine();

                    if (Objects.equals(authorChoice, "1")) {
                        currentAuthor = getExistingBookAuthors(lib, newAuthorList);
                        newAuthorList.add(currentAuthor);
                    } else if (Objects.equals(authorChoice, "2")) {
                        currentAuthor = getNewBookAuthor(lib, newAuthorList);
                        newAuthorList.add(currentAuthor);
                    } else if (Objects.equals(authorChoice, "0") && newAuthorList.isEmpty()) {
                        System.out.println("Error: Must add at least one author before exiting.");
                        authorChoice = ""; // Reset to keep looping
                    }
                }


                addNewBook(lib, newISBN, newTitle, newEditionNumber, newCopyright, newAuthorList);
            } else {
                System.out.println("Invalid choice");
            }

            printOptions();
            System.out.println("Choose an option: ");
            choice = scanner.nextLine();
        }
    }
}