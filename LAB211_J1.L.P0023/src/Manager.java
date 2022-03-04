
import java.util.ArrayList;
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
public class Manager {

    public static int menu() {
        System.out.println("    1. Create Fruit");
        System.out.println("    2. Update Fruit");
        System.out.println("    3. View orders");
        System.out.println("    4. Shopping(for buyer)");
        System.out.println("    5. Exit");
        int choice = Validation.getInt("Enter your choice(1->5): ", 1, 5);
        return choice;
    }

    public static void createFruit(ArrayList<Fruit> list, String check) {
        System.out.println("Enter fruit infomation to create");
        while (true) {
            String id;
            if (!check.equals("")) {
                System.out.println("Fruit id is " + check);
                id = check;
                check = "";
            } else {
                id = Validation.getString("Enter fruit id: ", "ID does not contain space and special characters.", "^\\w+$");
                if (findByID(id, list) != null) {
                    System.err.println("ID is exist. Please re-enter id");
                    continue;
                }
            }
            String name;
            while (true) {
                name = FormatString.toTitle(Validation.getString("Enter fruit name: ", "Fruit name contains only letter and space", "^[a-zA-Z ]+$"));
                if (findByName(name, list) != null) {
                    System.err.println("Fruit name is exist. Please re-enter fruit name.");
                } else {
                    break;
                }
            }
            double price = Validation.getDouble("Enter price in $: ");
            int quantity = Validation.getInt("Enter quantity: ", 1, Integer.MAX_VALUE);
            String origin = FormatString.toTitle(Validation.getString("Enter origin: ", "Origin contains only letter and space", "^[a-zA-Z ]+$"));
            list.add(new Fruit(id, name, price, quantity, origin));
            System.out.println("Create successfull!");

            String choice = Validation.getString("Do you want to continue(Y/N): ", "Input must be Y/y or N/n", "^[YyNn]$");
            if (choice.equalsIgnoreCase("n")) {
                System.out.println("List of Fruit: ");
                System.out.printf("|%20s|%20s|%20s|%20s|%20s|\n",
                        FormatString.alignCenter(20, "++ Item ++"),
                        FormatString.alignCenter(20, "++ Fruit Name ++"),
                        FormatString.alignCenter(20, "++ Quantity ++"),
                        FormatString.alignCenter(20, "++ Origin ++"),
                        FormatString.alignCenter(20, "++ Price ++"));
                int count = 0;
                for (Fruit f : list) {
                    count++;
                    System.out.printf("|%20s|%20s|%20s|%20s|%20s|\n",
                            FormatString.alignCenter(20, count),
                            FormatString.alignCenter(20, f.getFruitName()),
                            FormatString.alignCenter(20, f.getQuantity()),
                            FormatString.alignCenter(20, f.getOrigin()),
                            FormatString.alignCenter(20, f.getPrice() + "$"));
                }
                break;
            }
        }
    }

    public static void updateFruit(ArrayList<Fruit> list) {
        if (list.isEmpty()) {
            System.out.println("There is no fruit to update");
            return;
        }
        String id = Validation.getString("Enter fruit id to update: ", "ID does not contain space and special characters.", "^\\w+$");
        Fruit fr = findByID(id, list);
        if (fr != null) {
            System.out.println("The current quantity is " + fr.getQuantity());
            int quatity = Validation.getInt("Enter new quantity to update: ", 1, Integer.MAX_VALUE);
            fr.setQuantity(quatity);
            System.out.println("Update successful");
        } else {
            String choice = Validation.getString("ID is not exist. Do you want to create fruit(Y/N): ", "Input must be Y/y or N/n", "^[YyNn]$");
            if (choice.equalsIgnoreCase("y")) {
                createFruit(list, id);
            }
        }
    }

    public static void viewOrders(Hashtable<String, ArrayList<Fruit>> cList) {
        if (cList.isEmpty()) {
            System.out.println("There is no customer to view");
            return;
        }
        for (String key : cList.keySet()) {
            System.out.println("Customer: " + key.trim());
            System.out.printf("|%20s|%20s|%20s|%20s|\n",
                    FormatString.alignCenter(20, "Product"),
                    FormatString.alignCenter(20, "Quantity"),
                    FormatString.alignCenter(20, "Price"),
                    FormatString.alignCenter(20, "Amount"));
            int count = 0;
            for (Fruit f : cList.get(key)) {
                count++;
                System.out.printf("|%20s|%20s|%20s|%20s|\n",
                        FormatString.alignCenter(20, count + ". " + f.getFruitName()),
                        FormatString.alignCenter(20, f.getQuantity()),
                        FormatString.alignCenter(20, f.getPrice() + "$"),
                        FormatString.alignCenter(20, f.getPrice() * f.getQuantity() + "$"));
            }
            System.out.println();
        }
    }

    public static void shopping(ArrayList<Fruit> list, Hashtable<String, ArrayList<Fruit>> cList) {
        ArrayList<Integer> index = new ArrayList<>();
        ArrayList<Fruit> orderList = new ArrayList<>();

        double total = 0;
        while (true) {
            System.out.println("List of Fruit: ");
            System.out.printf("|%20s|%20s|%20s|%20s|%20s|\n",
                        FormatString.alignCenter(20, "++ Item ++"),
                        FormatString.alignCenter(20, "++ Fruit Name ++"),
                        FormatString.alignCenter(20, "++ Quantity ++"),
                        FormatString.alignCenter(20, "++ Origin ++"),
                        FormatString.alignCenter(20, "++ Price ++"));
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                Fruit f = list.get(i);
                if(f.getQuantity() > 0) {
                    count++;
                    if(total == 0)
                        index.add(i);
                    System.out.printf("|%20s|%20s|%20s|%20s|%20s|\n",
                            FormatString.alignCenter(20, count),
                            FormatString.alignCenter(20, f.getFruitName()),
                            FormatString.alignCenter(20, f.getQuantity()),
                            FormatString.alignCenter(20, f.getOrigin()),
                            FormatString.alignCenter(20, f.getPrice() + "$"));
                }
            }

            int choice = Validation.getInt("Select item to shopping: ", 1, index.size());
            Fruit f = list.get(index.get(choice - 1));
            System.out.println("You selected: " + f.getFruitName());
            int quantity = Validation.getInt("Please input quantity: ", 1, f.getQuantity());
            total += f.getPrice() * quantity;
            if (quantity == f.getQuantity()) {
                index.remove(index.get(choice - 1));
            }
            f.setQuantity(f.getQuantity() - quantity);
            
            Fruit fruit = findByName(f.getFruitName(), orderList);
            if (fruit == null) {
                orderList.add(new Fruit(f.getId(), f.getFruitName(), f.getPrice(), quantity, f.getOrigin()));
            } else {
                fruit.setQuantity(quantity + fruit.getQuantity());
            }

            String c = Validation.getString("Do you want to order now(Y/N): ", "Input must be Y/y or N/n", "^[YyNn]$");
            
            if (c.equalsIgnoreCase("y")) {
                orderList.sort((Fruit o1, Fruit o2) -> o2.getQuantity() - o1.getQuantity());
                System.out.printf("|%20s|%20s|%20s|%20s|\n",
                        FormatString.alignCenter(20, "Product"),
                        FormatString.alignCenter(20, "Quantity"),
                        FormatString.alignCenter(20, "Price"),
                        FormatString.alignCenter(20, "Amount"));
                int cnt = 0;
                for (Fruit fr : orderList) {
                    cnt++;
                    System.out.printf("|%20s|%20s|%20s|%20s|\n",
                            FormatString.alignCenter(20, cnt + ". " + fr.getFruitName()),
                            FormatString.alignCenter(20, fr.getQuantity()),
                            FormatString.alignCenter(20, fr.getPrice() + "$"),
                            FormatString.alignCenter(20, fr.getPrice() * fr.getQuantity() + "$"));
                }
                System.out.println("Total: " + total + "$");
                String customerName = FormatString.toTitle(Validation.getString("Input your name: ", "Name contains only letter and space", "^[a-zA-Z ]+$"));
                while (cList.containsKey(customerName)) {
                    customerName += " ";
                }
                cList.put(customerName, orderList);
                break;
            }
        }
    }

    public static Fruit findByID(String id, ArrayList<Fruit> list) {
        for (Fruit f : list) {
            if (f.getId().equalsIgnoreCase(id)) {
                return f;
            }
        }
        return null;
    }

    public static Fruit findByName(String name, ArrayList<Fruit> list) {
        for (Fruit f : list) {
            if (f.getFruitName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
}
