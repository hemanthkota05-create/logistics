package logistics.service;

import logistics.model.*;

import java.util.*;

public class DispatchService {
    private final Location depot;
    private final List<Order> orders = new ArrayList<>();
    private final List<Driver> drivers = new ArrayList<>();
    private final RouteOptimizer optimizer = new RouteOptimizer();
    private int nextOrderId = 1;

    public DispatchService(Location depot) { this.depot = depot; }

    public Location getDepot() { return depot; }
    public List<Order> getOrders() { return orders; }
    public List<Driver> getDrivers() { return drivers; }

    public void addDriver(Driver d) { drivers.add(d); }

    public Order addOrder(String customer, Location dest, double weightKg, Priority p) {
        Order o = new Order(nextOrderId++, customer, dest, weightKg, p);
        orders.add(o);
        return o;
    }

    /** Assign pending orders (express first, heaviest first) to the nearest driver with enough capacity. */
    public int dispatch() {
        List<Order> pending = orders.stream()
                .filter(o -> o.getStatus() == Status.PENDING)
                .sorted(Comparator.comparing(Order::getPriority)
                        .thenComparing(Comparator.comparingDouble(Order::getWeightKg).reversed()))
                .toList();

        int assigned = 0;
        for (Order o : pending) {
            Driver best = drivers.stream()
                    .filter(d -> d.remainingCapacity() >= o.getWeightKg())
                    .min(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(o.getDestination())))
                    .orElse(null);
            if (best == null) continue;
            best.getRoute().add(o);
            o.setDriver(best);
            o.setStatus(Status.ASSIGNED);
            assigned++;
        }
        for (Driver d : drivers) {
            List<Order> sorted = optimizer.optimize(depot, d.getRoute());
            d.getRoute().clear();
            d.getRoute().addAll(sorted);
        }
        return assigned;
    }

    public void startDeliveries() {
        for (Order o : orders) {
            if (o.getStatus() == Status.ASSIGNED) o.setStatus(Status.OUT_FOR_DELIVERY);
        }
    }

    /** Complete the next stop on a driver's route. Returns the order, or null if none left. */
    public Order deliverNext(int driverId, boolean success) {
        Driver d = drivers.stream().filter(x -> x.getId() == driverId).findFirst().orElse(null);
        if (d == null) return null;
        for (Order o : d.getRoute()) {
            if (o.getStatus() == Status.OUT_FOR_DELIVERY) {
                o.setStatus(success ? Status.DELIVERED : Status.FAILED);
                d.setCurrentLocation(o.getDestination());
                return o;
            }
        }
        return null;
    }

    public void printRoutes() {
        for (Driver d : drivers) {
            System.out.printf("%n%s (load %.1f/%.1f kg) route length %.1f km%n",
                    d.getName(), d.getLoadKg(), d.getCapacityKg(),
                    optimizer.totalDistance(depot, d.getRoute()));
            System.out.println("  Depot");
            for (Order o : d.getRoute()) {
                System.out.printf("  -> %s (order #%d, %s, %s)%n",
                        o.getDestination().name(), o.getId(), o.getPriority(), o.getStatus());
            }
        }
    }

    public void printSummary() {
        Map<Status, Long> counts = new EnumMap<>(Status.class);
        for (Order o : orders) counts.merge(o.getStatus(), 1L, Long::sum);
        System.out.println("\nSummary: " + counts);
    }
}
