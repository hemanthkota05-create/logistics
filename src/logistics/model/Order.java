package logistics.model;

public class Order {
    private final int id;
    private final String customer;
    private final Location destination;
    private final double weightKg;
    private final Priority priority;
    private Status status = Status.PENDING;
    private Driver driver;

    public Order(int id, String customer, Location destination, double weightKg, Priority priority) {
        this.id = id;
        this.customer = customer;
        this.destination = destination;
        this.weightKg = weightKg;
        this.priority = priority;
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public Location getDestination() { return destination; }
    public double getWeightKg() { return weightKg; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    @Override
    public String toString() {
        return String.format("#%d %-10s -> %-12s %5.1fkg %-8s %-16s %s",
                id, customer, destination.name(), weightKg, priority, status,
                driver == null ? "-" : driver.getName());
    }
}
