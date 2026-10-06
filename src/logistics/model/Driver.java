package logistics.model;

import java.util.ArrayList;
import java.util.List;

public class Driver {
    private final int id;
    private final String name;
    private final double capacityKg;
    private Location currentLocation;
    private final List<Order> route = new ArrayList<>();

    public Driver(int id, String name, double capacityKg, Location start) {
        this.id = id;
        this.name = name;
        this.capacityKg = capacityKg;
        this.currentLocation = start;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getCapacityKg() { return capacityKg; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location l) { this.currentLocation = l; }
    public List<Order> getRoute() { return route; }

    public double getLoadKg() {
        return route.stream().mapToDouble(Order::getWeightKg).sum();
    }

    public double remainingCapacity() { return capacityKg - getLoadKg(); }
}
