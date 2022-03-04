
import java.util.ArrayList;

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
        ArrayList<Student> list = new ArrayList<>();
        ArrayList<Report> rList = new ArrayList<>();
        
        System.out.println("WELCOME TO STUDENT MANAGEMENT");
        while (true) {
            int choice = Manager.menu();
            switch (choice) {
                case 1:
                    Manager.create(list, rList);
                    System.out.println("=======================================");
                    break;
                case 2:
                    Manager.findAndSort(list);
                    System.out.println("=======================================");
                    break;
                case 3:
                    Manager.updateAndDelete(list, rList);
                    System.out.println("=======================================");
                    break;
                case 4:
                    Manager.report(rList);
                    System.out.println("=======================================");
                    break;
                case 5:
                    return;
            }
        }
    }
}
