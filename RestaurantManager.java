import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RestaurantManager {
    static ArrayList<String> activeOrders = new ArrayList<>();
    static String[] completedOrders = new String[100];
    static int completedIndex = 0;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadOrders();
        showMenu();
    }

    public static void showMenu() {
        while (true) {
            System.out.println("\n--- Restaurant Order System ---");
            System.out.println("1. Add New Order");
            System.out.println("2. View Active Orders");
            System.out.println("3. Complete an Order");
            System.out.println("4. View Completed Orders");
            System.out.println("5. Save and Exit");
            System.out.println("——————————————————————————————");

            int choice = getValidInt("Choose an option: ");

            switch (choice) {
                case 1 -> addOrder();
                case 2 -> viewActiveOrders();
                case 3 -> completeOrder();
                case 4 -> viewCompletedOrders();
                case 5 -> {
                    saveOrders();
                    System.out.println("Saved. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void addOrder() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter food item: ");
        String item = scanner.nextLine();
        int table = getValidInt("Enter table number: ");

        String order = "Table " + table + " - " + name + " ordered " + item;
        activeOrders.add(order);
        System.out.println("Order added!");
    }

    public static void viewActiveOrders() {
        if (activeOrders.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }
        for (int i = 0; i < activeOrders.size(); i++) {
            System.out.println((i + 1) + ". " + activeOrders.get(i));
        }
    }

    public static void completeOrder() {
        viewActiveOrders();
        if (activeOrders.isEmpty()) return;

        int index = getValidInt("Enter order number to complete: ") - 1;

        try {
            String order = activeOrders.remove(index);
            if (completedIndex < completedOrders.length) {
                completedOrders[completedIndex++] = order;
                System.out.println("Order marked as completed.");
            } else {
                System.out.println("Completed order list is full.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index.");
        }
    }

    public static void viewCompletedOrders() {
        if (completedIndex == 0) {
            System.out.println("No completed orders.");
            return;
        }
        for (int i = 0; i < completedIndex; i++) {
            System.out.println((i + 1) + ". " + completedOrders[i]);
        }
    }

    public static int getValidInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again.");
            return getValidInt(prompt);
        }
    }

    public static void saveOrders() {
        try (PrintWriter activeWriter = new PrintWriter("active_orders.txt");
             PrintWriter completedWriter = new PrintWriter("completed_orders.txt")) {

            for (String order : activeOrders) {
                activeWriter.println(order);
            }
            for (int i = 0; i < completedIndex; i++) {
                completedWriter.println(completedOrders[i]);
            }
        } catch (IOException e) {
            System.out.println("Error saving files: " + e.getMessage());
        }
    }

    public static void loadOrders() {
        try (BufferedReader activeReader = new BufferedReader(new FileReader("active_orders.txt"))) {
            String line;
            while ((line = activeReader.readLine()) != null) {
                activeOrders.add(line);
            }
        } catch (IOException e) {
            System.out.println("No active orders found. Starting fresh.");
        }

        try (BufferedReader completedReader = new BufferedReader(new FileReader("completed_orders.txt"))) {
            String line;
            while ((line = completedReader.readLine()) != null && completedIndex < completedOrders.length) {
                completedOrders[completedIndex++] = line;
            }
        } catch (IOException e) {
            System.out.println("No completed orders found. Starting fresh.");
        }
    }
}
