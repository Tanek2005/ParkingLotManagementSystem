import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class VehicleManager {

    private static final String URL = "jdbc:sqlite:Parkingdb.db";

    public static void createVehicleTable() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS Vehicle (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "type VARCHAR(50), " +
                    "licensePlate VARCHAR(50), " +
                    "createDate DATETIME" +
                    ");");

        }
    }

    public static void storeVehicle(String type, String licensePlate, LocalDate createDate) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                    "INSERT INTO Vehicle (type, licensePlate, createDate) VALUES ('"
                            + type + "', '" + licensePlate + "', '" + createDate + "')"
            );

        }
    }

    public static Vehicle getVehicle(int id) throws Exception {

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT id, type, licensePlate FROM Vehicle WHERE id = " + id
            );

            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("licensePlate")
                );
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {

        createVehicleTable();
        storeVehicle("Car", "B23323", LocalDate.now());
        storeVehicle("Bike", "XYZ999", LocalDate.now());

        Vehicle v = getVehicle(1);

        if (v != null) {
            System.out.println("Vehicle Type: " + v.getType());
            System.out.println("License Plate: " + v.getLicensePlate());
        } else {
            System.out.println("Vehicle not found");
        }
    }
}

class Vehicle {
    private int id;
    private String type;
    private String licensePlate;

    public Vehicle(int id, String type, String licensePlate) {
        this.id = id;
        this.type = type;
        this.licensePlate = licensePlate;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}