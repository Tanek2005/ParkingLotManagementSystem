import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketManager {

    private static final String URL = "jdbc:sqlite:Parkingdb.db";

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void createTicketTable() {

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Ticket (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehicleId INTEGER, " +
                "entryTime DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "exitTime DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "createDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(vehicleId) REFERENCES Vehicle(id)" +
                ");"
            );

        } catch (Exception e) {
            System.out.println("Error creating Ticket table");
        }
    }

    public static void storeTicket(int vehicleId) {

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String query =
                "INSERT INTO Ticket (vehicleId) VALUES (" + vehicleId + ")";

            stmt.executeUpdate(query);

        } catch (Exception e) {
            System.out.println("Error storing ticket");
        }
    }

    public static void updateTicket(int id) {

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String query =
                "UPDATE Ticket SET exitTime = datetime('now') WHERE id = " + id;

            stmt.executeUpdate(query);

        } catch (Exception e) {
            System.out.println("Error updating ticket");
        }
    }

    public static Ticket getTicket(int id) {

        Ticket t = null;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM Ticket WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {


                LocalDateTime entry = LocalDateTime.parse(
                        rs.getString("entryTime").replace(" ", "T")
                );

                LocalDateTime exit = LocalDateTime.parse(
                        rs.getString("exitTime").replace(" ", "T")
                );

                t = new Ticket(
                        rs.getInt("id"),
                        rs.getInt("vehicleId"),
                        entry,
                        exit
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching ticket");
        }

        return t;
    }

    public static void main(String[] args) {

        createTicketTable();
        storeTicket(2);        
        updateTicket(1);       

        Ticket t1 = getTicket(1);

        if (t1 != null) {
            System.out.println("Entry: " + t1.getEntryTime());
            System.out.println("Exit : " + t1.getExitTime());
        } else {
            System.out.println("Ticket not found");
        }
    }
}

 class Ticket {

    int id;
    int vehicleId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(int id,int vehicleId, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.entryTime = entryTime;
        if (exitTime == null) {
            this.exitTime = entryTime;
        } else {
            this.exitTime = exitTime;
        }
    }
    public int getId() {
        return id;
    }
    public int getIntVehicleId() {
        return vehicleId;
    }
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}