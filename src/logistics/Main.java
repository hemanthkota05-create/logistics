package logistics;

import logistics.model.*;
import logistics.service.DispatchService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Location depot = new Location("Guntur Depot", 0, 0);
        DispatchService svc = new DispatchService(depot);

        svc.addDriver(new Driver(1, "Ravi", 100, depot));
        svc.addDriver(new Driver(2, "Anil", 60, depot));
        svc.addDriver(new Driver(3, "Suresh", 40, depot));

        Location[] places = {
            new Location("Brodipet", 2, 3), new Location("Arundelpet", 3, 1),
            new Location("Mangalagiri", 8, 4), new Location("Tenali", 14, -6),
            new Location("Narasaraopet", -12, 10), new Location("Amaravati", 12, 9)
        };
        svc.addOrder("Hemanth", places[0], 5, Priority.EXPRESS);
        svc.addOrder("Priya", places[1], 12, Priority.STANDARD);
        svc.addOrder("Kiran", places[2], 30, Priority.STANDARD);
        svc.addOrder("Sneha", places[3], 20, Priority.ECONOMY);
        svc.addOrder("Varun", places[4], 45, Priority.EXPRESS);
        svc.addOrder("Divya", places[5], 8, Priority.ECONOMY);

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Logistics Delivery System ===");
            System.out.println("1. List orders");
            System.out.println("2. Add order");
            System.out.println("3. Dispatch pending orders");
            System.out.println("4. Show optimized routes");
            System.out.println("5. Start deliveries");
            System.out.println("6. Deliver next stop for a driver");
            System.out.println("7. Summary");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> svc.getOrders().forEach(System.out::println);
                    case "2" -> {
                        System.out.print("Customer: "); String name = in.nextLine();
                        System.out.print("Destination name: "); String dest = in.nextLine();
                        System.out.print("x y coordinates (km): ");
                        String[] xy = in.nextLine().trim().split("\\s+");
                        System.out.print("Weight kg: "); double w = Double.parseDouble(in.nextLine());
                        System.out.print("Priority (EXPRESS/STANDARD/ECONOMY): ");
                        Priority p = Priority.valueOf(in.nextLine().trim().toUpperCase());
                        Order o = svc.addOrder(name, new Location(dest,
                                Double.parseDouble(xy[0]), Double.parseDouble(xy[1])), w, p);
                        System.out.println("Created " + o);
                    }
                    case "3" -> System.out.println("Assigned " + svc.dispatch() + " order(s).");
                    case "4" -> svc.printRoutes();
                    case "5" -> { svc.startDeliveries(); System.out.println("Drivers are on the road."); }
                    case "6" -> {
                        System.out.print("Driver id: "); int id = Integer.parseInt(in.nextLine());
                        System.out.print("Delivered successfully? (y/n): ");
                        boolean ok = in.nextLine().trim().equalsIgnoreCase("y");
                        Order o = svc.deliverNext(id, ok);
                        System.out.println(o == null ? "No active stops for that driver." : "Updated: " + o);
                    }
                    case "7" -> svc.printSummary();
                    case "0" -> { System.out.println("Bye."); return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }
}
