package logistics.service;

import logistics.model.Location;
import logistics.model.Order;
import logistics.model.Priority;

import java.util.ArrayList;
import java.util.List;

/** Nearest-neighbor heuristic; EXPRESS orders are always visited before others. */
public class RouteOptimizer {

    public List<Order> optimize(Location start, List<Order> orders) {
        List<Order> remaining = new ArrayList<>(orders);
        List<Order> result = new ArrayList<>();
        Location current = start;

        while (!remaining.isEmpty()) {
            boolean hasExpress = remaining.stream().anyMatch(o -> o.getPriority() == Priority.EXPRESS);
            Order best = null;
            double bestDist = Double.MAX_VALUE;
            for (Order o : remaining) {
                if (hasExpress && o.getPriority() != Priority.EXPRESS) continue;
                double d = current.distanceTo(o.getDestination());
                if (d < bestDist) { bestDist = d; best = o; }
            }
            result.add(best);
            remaining.remove(best);
            current = best.getDestination();
        }
        return result;
    }

    public double totalDistance(Location start, List<Order> route) {
        double total = 0;
        Location current = start;
        for (Order o : route) {
            total += current.distanceTo(o.getDestination());
            current = o.getDestination();
        }
        return total;
    }
}
