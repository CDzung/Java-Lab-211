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
        System.out.println("THE SECRET BANK");
        System.out.println("1. Create new account");
        System.out.println("2. Login");
        System.out.println("3. Withdraw");
        System.out.println("4. Deposit");
        System.out.println("5. Transfer");
        System.out.println("6. Remove");
        System.out.println("Others - Quits");
        return Validation.getInt("Enter your choice: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        manager.loadBank();
        manager.loadUser();
        while (true) {
            int choice = getChoice();
            switch (choice) {
                case 1:
                    manager.create();
                    break;
                case 2:
                    manager.login();
                    break;
                case 3:
                    manager.withdraw();
                    break;
                case 4:
                    manager.deposit();
                    break;
                case 5:
                    manager.transfer();
                    break;
                case 6:
                    manager.remove();
                    break;
                default:
                    return;
            }
        }
    }
}
