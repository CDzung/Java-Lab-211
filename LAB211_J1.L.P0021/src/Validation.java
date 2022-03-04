
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mido
 */
public class Validation {
    private final static Scanner sc = new Scanner(System.in);
    
    //Return integer number in known range with validation
    static int getInt(String msg, int min, int max) {
        while(true){
            System.out.print(msg);
            String s = sc.nextLine();
            if(s.isEmpty()){
                System.err.println("Input cound not be empty");
                continue;
            }
            try {
                int res = Integer.parseInt(s);
                if(res<min || res>max){
                    System.err.println("Please input number in range [" + min + ", " + max + "]");
                } else 
                    return res;
            } catch (NumberFormatException e) {
                System.err.println("Input must be integer number");
                
            }
        }
    }
    
    //Return string in known regex format with validation
    public static String getString(String msg, String formatMsg, String regex){
        while(true){
            System.out.print(msg);
            String s = sc.nextLine();
            if(s.isEmpty()){
                System.err.println("Input cound not be empty");
                continue;
            }
            if(regex.equals("course")){
                if(s.equalsIgnoreCase("java") || s.equalsIgnoreCase(".net") || s.equalsIgnoreCase("c/c++"))
                    return s;
                System.err.println(formatMsg);
                continue;
            }
            if(s.matches(regex))
                return s;
            System.err.println(formatMsg);
        }
    }

}
