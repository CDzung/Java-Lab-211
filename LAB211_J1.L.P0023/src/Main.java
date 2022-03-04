
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
public class Main {

    public static void main(String[] args) {
        ArrayList<Fruit> fruitList = new ArrayList<>();
        Hashtable<String, ArrayList<Fruit>> cList = new Hashtable<>();

        System.out.println("FRUIT SHOP SYSTEM");
        while (true) {
            int choice = Manager.menu();
            switch (choice) {
                case 1:
                    Manager.createFruit(fruitList, "");
                    break;
                case 2:
                    Manager.updateFruit(fruitList);
                    break;
                case 3:
                    Manager.viewOrders(cList);
                    break;
                case 4:
                    Manager.shopping(fruitList, cList);
                    break;
                case 5:
                    return;
            }
        }
    }
}
