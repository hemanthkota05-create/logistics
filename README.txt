Logistics Delivery System (Java 17+)

Compile:  javac -d out $(find src -name "*.java")
Run:      java -cp out logistics.Main

Structure:
  model/    Location, Order, Driver, Priority, Status
  service/  DispatchService (assignment, status flow), RouteOptimizer (nearest neighbor)
  Main      console menu

Ideas to extend: Spring Boot REST API, MySQL via JDBC, real distances via a maps API,
vehicle types, delivery time windows, JUnit tests.
