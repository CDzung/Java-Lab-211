/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mido
 */
public class Main {

    public static int getChoice() {
        System.out.println("\n=================== HKT Book Store Management ===================");
        System.out.println("1. Show the book list");
        System.out.println("2. Add new book");
        System.out.println("3. Update book");
        System.out.println("4. Delete book");
        System.out.println("5. Delete author");
        System.out.println("6. Search book");
        System.out.println("Others. Exit");
        return Validation.getInt("Enter your choice: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        BookStoreManagement manager = new BookStoreManagement();
        manager.loadFromFile();
        while (true) {
            int choice = getChoice();
            switch (choice) {
                case 1:
                    manager.showTheBookList();
                    break;
                case 2:
                    manager.addBook();
                    break;
                case 3:
                    manager.updateBook();
                    break;
                case 4:
                    manager.deleteBook();
                    break;
                case 5:
                    manager.deleteAuthor();
                    break;
                case 6:
                    manager.searchBook();
                    break;
                default:
                    return;
            }
        }
    }
}
