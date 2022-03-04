
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mido
 */
public class BookStoreManagement {

    private HashMap<String, Book> book;
    private HashMap<Integer, String> author;

    public BookStoreManagement() {
        book = new HashMap<>();
        author = new HashMap<>();
    }

    public void saveToFile(String fileName, Object obj) {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream oStream = new ObjectOutputStream(file);
            oStream.writeObject(obj);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void loadFromFile() {
        try {
            FileInputStream file = new FileInputStream("author.dat");
            ObjectInputStream iStream = new ObjectInputStream(file);
            if (iStream.available() != 0) {
                author = (HashMap<Integer, String>) iStream.readObject();
            }
            file = new FileInputStream("book.dat");
            iStream = new ObjectInputStream(file);
            if (iStream.available() != 0) {
                book = (HashMap<String, Book>) iStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> showTheBookList() {
        System.out.printf(
                "%-10s | %-20s | %-20s | %-20s | %-20s | %-20s\n",
                "Index",
                "ISBN",
                "Title",
                "Price",
                "Author ID",
                "Author Name"
        );
        ArrayList<String> list = new ArrayList(book.keySet());
        for (int i = 0; i < list.size(); i++) {
            Book b = book.get(list.get(i));
            System.out.printf(
                    "%-10s | %-20s | %-20s | %-20s | %-20s | %-20s\n",
                    i + 1,
                    b.getISBN(),
                    b.getTitle(),
                    b.getPrice(),
                    b.getAuthorID(),
                    author.get(b.getAuthorID())
            );
        }
        return list;
    }

    public ArrayList<Integer> showAuthorList() {
        ArrayList<Integer> list = new ArrayList(author.keySet());
        System.out.printf(
                "%-10s | %-20s | %-20s\n",
                "Index",
                "Author ID",
                "Author Name"
        );
        for (int i = 0; i < list.size(); i++) {
            String auName = author.get(list.get(i));
            System.out.printf(
                    "%-10s | %-20s | %-20s\n",
                    i + 1,
                    list.get(i),
                    auName
            );
        }
        return list;
    }

    public void addBook() {
        if (author.size() < 1) {
            System.out.println("There is no author in database");
            return;
        }
        
        boolean check = true;
        while (check) {
            System.out.println("Enter book information to add");

            String ISBN = Validation.getString("Enter ISBN: ", "Invalid ISBN", "97[89]-\\d-\\d+-\\d+-\\d", false);
            if (book.get(ISBN) != null) {
                System.out.println("The ISBN existed in database");
                continue;
            }

            String title = Validation.getString("Enter title: ", "Invalid title", ".+", false);

            double price = Validation.getDouble("Enter price: ", 0, Double.MAX_VALUE, false);

            ArrayList<Integer> list = showAuthorList();

            int choice = Validation.getInt("Enter author index: ", 1, list.size());
            book.put(ISBN, new Book(ISBN, title, price, list.get(choice - 1)));

            

            check = Validation.getString("Continue add? (Y/N): ", "", "[ynYN]", false).equalsIgnoreCase("y");
        }
        
        saveToFile("book.dat", book);
    }

    public void updateBook() {
        if (book.size() < 1) {
            System.out.println("There is no book in the store");
            return;
        }

        ArrayList<String> list = showTheBookList();
        int choice = Validation.getInt("Enter book index: ", 1, list.size());
        Book b = book.get(list.get(choice - 1));

        String title = Validation.getString("Enter new title: ", "", ".+", true);
        if (title.equals("")) {
            System.out.println("Failed");
        } else {
            System.out.println("Successful");
            b.setTitle(title);
        }

        double price = Validation.getDouble("Enter new price: ", 0, Double.MAX_VALUE, true);
        if (price == -1) {
            System.out.println("Failed");
        } else {
            System.out.println("Successful");
            b.setPrice(price);
        }

        ArrayList<Integer> authorList = showAuthorList();

        int idx = Validation.getInt("Enter author index: ", 1, authorList.size());
        int auID = authorList.get(idx - 1);

        if (auID == b.getAuthorID()) {
            System.out.println("Failed");
        } else {
            System.out.println("Successful");
            b.setAuthorID(auID);
        }

        System.out.println("After update:\n" + b + ", Author Name = " + author.get(auID));

        saveToFile("book.dat", book);
    }

    public void deleteBook() {
        if (book.size() < 1) {
            System.out.println("There is no book in the store");
            return;
        }

        ArrayList<String> list = showTheBookList();
        int idx = Validation.getInt("Enter book index: ", 1, list.size());
        Book b = book.get(list.get(idx - 1));

        String choice = Validation.getString("Confirm delete? (Y/N): ", "", "[ynYN]", true);
        if (choice.equalsIgnoreCase("y")) {
            book.remove(b.getISBN());
            System.out.println("Successful");
        } else {
            System.out.println("Failed");
        }

        saveToFile("book.dat", book);
    }

    public void searchBook() {
        if (book.size() < 1) {
            System.out.println("There is no book in the store");
            return;
        }

        int choice = Validation.getInt("Choose way to search (1: search by title, 2: search by author name): ", 1, 2);

        String searchText = Validation.getString("Enter text: ", "", ".+", false).toLowerCase().trim();

        System.out.printf(
                "%-15s | %-20s | %-30s | %-20s | %-20s | %-30s\n",
                "Index",
                "ISBN",
                "Title",
                "Price",
                "Author ID",
                "Author Name"
        );
        int i = 0;
        for (String s : book.keySet()) {
            i++;
            Book b = book.get(s);
            if ((choice == 1 && b.getTitle().toLowerCase().trim().contains(searchText))
                    || (choice == 2 && author.get(b.getAuthorID()).toLowerCase().trim().contains(searchText))) {
                System.out.printf(
                        "%-10s | %-20s | %-30s | %-20s | %-20s | %-30s\n",
                        i,
                        b.getISBN(),
                        b.getTitle(),
                        b.getPrice(),
                        b.getAuthorID(),
                        author.get(b.getAuthorID())
                );
            }
        }

    }

    public void deleteAuthor() {
        if (author.size() < 1) {
            System.out.println("There is no author to delete");
            return;
        }

        ArrayList<Integer> list = showAuthorList();

        int idx = Validation.getInt("Enter author index: ", 1, list.size());

        boolean check = true;
        for (String s : book.keySet()) {
            if (book.get(s).getAuthorID() == list.get(idx - 1)) {
                check = false;
                break;
            }
        }

        if (check) {
            boolean choice = Validation.getString("Confirm delete? (Y/N): ", "", "[ynYN]", false).equalsIgnoreCase("y");
            if (choice) {
                author.remove(list.get(idx - 1));
                System.out.println("Successfull");
            } else {
                System.out.println("Cancel");
            }

        } else {
            System.out.println("Failed. This author has a book in the store, you cannot delete this author.");
        }

        saveToFile("author.dat", author);
    }

    public void addAuthor(int key, String value) {
        author.put(key, value);
        saveToFile("author.dat", author);
    }

}
