package logistics.model;

public record Location(String name, double x, double y) {
    public double distanceTo(Location other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}
