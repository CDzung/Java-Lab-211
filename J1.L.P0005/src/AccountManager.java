
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mido
 */
public class AccountManager {

    private final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])[^\\s]{6,}$";
    private Account loginAccount;
    private Hashtable<Integer, String> user;
    private Hashtable<Integer, Account> bank;

    public AccountManager() {
        user = new Hashtable<>();
        bank = new Hashtable<>();
        loginAccount = null;
    }

    public void addAccount(int id, String password, String name, double balance) {
        user.put(id, password);
        bank.put(id, new Account(id, name, balance));
    }

    public Account getAccount(int id) {
        return bank.get(id);
    }

    public String getPassword(int id) {
        return user.get(id);
    }

    public void removeAccount(int id) {
        bank.remove(id);
        user.remove(id);
    }

    public void saveBank() {
        try {
            FileOutputStream file = new FileOutputStream("bank.dat");
            ObjectOutputStream oStream = new ObjectOutputStream(file);
            oStream.writeObject(bank);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void saveUser() {
        try {
            FileOutputStream file = new FileOutputStream("user.dat");
            ObjectOutputStream oStream = new ObjectOutputStream(file);
            oStream.writeObject(user);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void loadBank() {
        try {
            FileInputStream file = new FileInputStream("bank.dat");
            ObjectInputStream iStream = new ObjectInputStream(file);
            bank = (Hashtable<Integer, Account>) iStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public void loadUser() {
        try {
            FileInputStream file = new FileInputStream("user.dat");
            ObjectInputStream iStream = new ObjectInputStream(file);
            user = (Hashtable<Integer, String>) iStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public void create() {
        int id;
        while (true) {
            id = Validation.getInt("Enter account id: ", 1, Integer.MAX_VALUE);
            if (getAccount(id) != null) {
                System.out.println("The account id is exist!");
            } else {
                break;
            }
        }
        String name = Validation.getString("Enter account name: ", "Name contains only letters and spaces", "[a-zA-Z ]+");
        String password = Validation.getString("Enter password: ",
                "The password must contain at least 6 characters, including uppercase letters, lower case letters, numeric characters and 1 special character",
                PASSWORD_REGEX);
        while (!Validation.getString("Enter confirm password: ", "", ".*").endsWith(password)) {
            System.out.println("The two passwords must be the same");
        }

        addAccount(id, FormatString.toSHA256(password), name, 0);
        System.out.println("Create successful.");
        saveBank();
        saveUser();
    }

    public void login() {
        if (user.isEmpty()) {
            System.out.println("No account in database");
            return;
        }
        System.out.println("Enter account id and password to login");
        int id;
        while (true) {
            id = Validation.getInt("Enter account id: ", 1, Integer.MAX_VALUE);
            if (getAccount(id) == null) {
                System.out.println("The account id is not exist");
            } else {
                break;
            }
        }
        String password = Validation.getString("Enter password: ",
                "The password must contain at least 6 characters, including uppercase letters, lower case letters, numeric characters and 1 special character",
                PASSWORD_REGEX);
        if (FormatString.toSHA256(password).equals(getPassword(id))) {
            loginAccount = getAccount(id);
            System.out.println("Account name: " + loginAccount.getName());
            System.out.println("Login successful!");
        } else {
            System.out.println("The password and the id don't match.");
        }
    }

    public void withdraw() {
        if (loginAccount == null) {
            System.out.println("You must login to withdraw");
            return;
        }
        if (loginAccount.getBalance() < 1) {
            System.out.println("You don't have enough money to withdraw");
            return;
        }
        double money = Validation.getDouble("Enter amount of money to withdraw: ", 1, loginAccount.getBalance());
        loginAccount.setBalance(loginAccount.getBalance() - money);
        saveBank();
        System.out.println("Withdraw successful");
    }

    public void deposit() {
        if (loginAccount == null) {
            System.out.println("You must login to deposit");
            return;
        }
        double money = Validation.getDouble("Enter amount of money to deposit: ", 1, Double.MAX_VALUE);
        String choice = Validation.getString("Confirm to deposit?(Y/N): ", "Input must be y or n", "^[yYnN]$");
        if (choice.equalsIgnoreCase("y")) {
            loginAccount.setBalance(loginAccount.getBalance() + money);
            saveBank();
            System.out.println("Deposit successful");
        } else {
            System.out.println("Deposit failed");
        }
    }

    public void transfer() {
        if (loginAccount == null) {
            System.out.println("You must login to transfer");
            return;
        }
        if(loginAccount.getBalance() < 1) {
            System.out.println("You don't have enough money to transfer");
            return;
        }
        int id;
        while (true) {
            id = Validation.getInt("Enter account id: ", 1, Integer.MAX_VALUE);
            if (getAccount(id) == null) {
                System.out.println("The account id is not exist");
            } else {
                break;
            }
        }
        Account receiver = getAccount(id);
        System.out.println("Receiver's name: " + receiver.getName());
        double money = Validation.getDouble("Enter amount of money to transfer: ", 1, loginAccount.getBalance());
        String choice = Validation.getString("Confirm to transfer?(Y/N): ", "Input must be y or n", "^[yYnN]$");
        if (choice.equalsIgnoreCase("y")) {
            loginAccount.setBalance(loginAccount.getBalance() - money);
            receiver.setBalance(receiver.getBalance() + money);
            saveBank();
            System.out.println("Transfer successful");
        } else {
            System.out.println("Transfer failed");
        }
    }
    
    public void remove() {
        if (loginAccount == null) {
            System.out.println("You must login to remove");
            return;
        }
        String choice = Validation.getString("Confirm to remove?(Y/N): ", "Input must be y or n", "^[yYnN]$");
        if (choice.equalsIgnoreCase("y")) {
            removeAccount(loginAccount.getId());
            saveBank();
            saveUser();
            loginAccount = null;
            System.out.println("Remove successful");
        } else {
            System.out.println("Remove failed");
        }
    }

}
