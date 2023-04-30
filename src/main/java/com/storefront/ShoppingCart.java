package com.storefront;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ShoppingCart {
    static Scanner input = new Scanner(System.in);
    static HashMap<String, Product> inventory = new HashMap<String, Product>();
    static ArrayList<Product> inShoppingCart = new ArrayList<Product>();
    static float grandTotal =0.0f;
    public static void main(String[] args) {

        boolean exit = false;
        while (!exit) {
            try {

                System.out.println("Welcome to the Store!");
                System.out.println("\t1: Show Products");
                System.out.println("\t2: Show your cart");
                System.out.println("\t3: Exit the store");
                System.out.println("=========================");
                System.out.print("What would you like to do?: ");
                int command = input.nextInt();
                input.nextLine();

                switch (command) {
                    case 1: //Show Product
                        loadInventory();
                        addToCart();
                        break;
                    case 2: //Show Cart
                        showCart(); //checkout function within
                        break;
                    case 3: //Exit
                        System.out.println("Thank you. Have a Good Day!");
                        exit = true;
                        break;
                    default: //User Error
                        System.out.println("Invalid input, try again.");
                        break;
                }

            } catch(IOException e){
                System.out.println("ERROR: An unexpected error occurred");
                e.getStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    public static void loadInventory() throws IOException {
        String fileName = "inventory.csv"; //inventory list (id|name|price)
        String line;

        BufferedReader bfr = new BufferedReader(new FileReader(fileName));
            while ((line = bfr.readLine()) != null) {

                String[] getInventoryInfo = line.split(Pattern.quote("|"));
                //assign variable by '|'
                String id = getInventoryInfo[0];
                String name = getInventoryInfo[1];
                float price = Float.parseFloat(getInventoryInfo[2]);

                Product product = new Product(id, name, price);
                //print inventory with toString
                inventory.put(product.getId(),product);
                System.out.println(product);

                }
            }
    public static void addToCart() {

                System.out.print("\nEnter the item ID you wish to add to your shopping cart or X to return to menu: ");
                String addItemID = input.nextLine();
                //User wishes to return to menu
                if (addItemID.equalsIgnoreCase("x")) {
                    System.out.println("Returning to menu");
                    return;
                }
                //index search via input addItemID
                Product matchedProduct = inventory.get(addItemID.toUpperCase());
                if (matchedProduct == null) {
                    System.out.println("We don't have that product ID");
                    return;
                }
                //add valid id to the shopping cart array list
                inShoppingCart.add(matchedProduct);
                System.out.println("Adding " + matchedProduct.getName() + " to your shopping cart.\n");
    }
    public static void showCart() {
        //access empty shopping cart
        if (inShoppingCart == null || inShoppingCart.size() == 0){
            System.out.println("You have no items in your shopping cart.\n");
            return;
        }
        //print current shopping cart status as well as total items and price
        for (int i=0; i < inShoppingCart.size(); i++){
            System.out.println((i+1) + ": " + inShoppingCart.get(i));
            Product matchedProduct = inShoppingCart.get(i);
            grandTotal += matchedProduct.getPrice();
        }
        System.out.println("==============================================================================");
        System.out.println("You selected " + inShoppingCart.size() + " item(s). Your subtotal is: " + String.format("$%.2f\n",grandTotal));

        System.out.print("Would you like to check out?('C' to checkout, 'X' to return to the menu: ");
        String command = input.nextLine();


            if (command.equalsIgnoreCase("C")) {
                checkOut();
            } else if (command.equalsIgnoreCase("X")) {
                System.out.println("Returning to menu.");
            } else {
                System.out.println("Incorrect input, try again");
            }

    }

    public static void checkOut(){
    //Prompt cash paid with
        System.out.println("Please enter the exact change you are paying with: ");
        float cashPaid = input.nextFloat();

        //if cashPaid >= total: continue checkout sequence
        if (cashPaid >= grandTotal){
            //-Print receipt
            float cashChange = cashPaid-grandTotal;
            System.out.println("The receipt for your purchase:");
            for (int i=0; i < inShoppingCart.size(); i++) {
                System.out.println((i + 1) + ": " + inShoppingCart.get(i));
            }
            System.out.println("==============================================================================");
            System.out.println("You purchased " + inShoppingCart.size() + " item(s). Your subtotal is: " + String.format("$%.2f\n",grandTotal));
            System.out.println("Cash: " + String.format("$%.2f", cashPaid));
            System.out.println("Cash change: " + String.format("$%.2f\n",cashChange));
            System.out.println("Thank you for shopping with us. Have a wonderful day!");

            //-clear shopping cart
            inShoppingCart.clear();

            //-return to menu

        } else {
            //if cashPaid < total: return home
            System.out.println("Insufficient funds. Please take your cash and try again.");

        }
    }
}