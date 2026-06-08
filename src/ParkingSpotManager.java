import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ParkingSpotManager {

    private static final String URL = "jdbc:sqlite:Parkingdb.db";

    static int countBike = 0;
    static int countCar = 0;
    // static int totalCars = 50;
    // static int totalBikes = 50;

    public static void createTable()  {
    try (Connection conn = DriverManager.getConnection(URL);
         Statement stmt = conn.createStatement()) {
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS ParkingSpot (" +
            "spotId TEXT PRIMARY KEY, " +
            "size INTEGER, " +
            "isOccupied INTEGER" +
            ")"
        );

    } catch (Exception e) {
        System.out.println("Error creating table");

    }

}

    private static void saveSpot(ParkingSpot parkingSpot)  {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            if (parkingSpot.size == 2) {
                ParkingSpotManager.countBike++;
                String spotId = ParkingSpotManager.countBike + "B";
                parkingSpot.id = spotId;
                stmt.execute(
                    "INSERT INTO ParkingSpot (spotId, size, isOccupied) VALUES ('"
                    + spotId + "', "
                    + parkingSpot.size + ", "
                    + (parkingSpot.isOccupied ? 1 : 0) + ")"
                );
            } else if (parkingSpot.size == 4) {
                ParkingSpotManager.countCar++;
                String spotId = ParkingSpotManager.countCar + "C";
                parkingSpot.id = spotId;
                stmt.execute(
                    "INSERT INTO ParkingSpot (spotId, size, isOccupied) VALUES ('"
                    + spotId + "', "
                    + parkingSpot.size + ", "
                    + (parkingSpot.isOccupied ? 1 : 0) + ")"
                );
            } else {
                System.out.println("Invalid object !!!!");
            }
        } catch (Exception e) {
            System.out.println("Error saving spot");
        }
    }

    static ParkingSpot getSpot(String id) {
        ParkingSpot spot = null;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String query =
                "SELECT * FROM ParkingSpot WHERE spotId = '" + id + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                spot = new ParkingSpot(
                        rs.getString("spotId"),
                        rs.getInt("size"),
                        rs.getInt("isOccupied") == 1
                );
            }
        } catch (Exception e) {
            System.out.println("Error fetching spot");
        }
        return spot;
    }


    public static void main(String[] args) {

        ParkingSpotManager.createTable();

        ParkingSpot b1 = new ParkingSpot(null, 2, true);
        saveSpot(b1);

ParkingSpot b2 = new ParkingSpot(null, 2, true);
saveSpot(b2);

ParkingSpot c1 = new ParkingSpot(null, 4, true);
saveSpot(c1);

System.out.println("Saved Spots:");
System.out.println(b1.id + " " + b2.id + " " + c1.id);
    }

}

class ParkingSpot {
    String id;
    int size;
    Boolean isOccupied;

    public ParkingSpot(String id, int size, Boolean isOccupied) {
        this.id = id;
        this.size = size;
        this.isOccupied = isOccupied;
    }
    void assignSpot() {
        this.isOccupied = true;
    }
    void freeSpot() {
        this.isOccupied = false;
    }
}

// To Assign ParkingSpot 
// 1. For checking the Parking Spot availabilty :-
//    Check the no of available spots by checking the count of parking spots assigned of each size. 
//    eg;-if(count(isOccupied==True && count(size>100) return false;)
//    ))
// 2.And probe for an enpty spot if available in the database.
// 3.then return the parkingSpot object for that
// 4.For assignment getParkingSpot object 

