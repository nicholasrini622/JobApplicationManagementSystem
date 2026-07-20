import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages SQLite database connection and setup
 * Stores database path, creates conneciton, and creates a job_application table if it is not present.
 */
public class DBHelper {
    private static String databasePath = "job_applications.db";

    /**
     * Set file path for application, replaces backslash with foward slash
     * @param newPath database file path
     */
    public static void setDatabasePath(String newPath){
        if(newPath != null && !newPath.isBlank()){
        databasePath = newPath.replace("\\","/");
        }
    }

    /**
     * Get current file path
     * @return current file path for database
     */
    public static String getDatabasePath(){
        return databasePath;
    }

    /**
     * Create and return connection for SQlite database
     * @return connection to SQLite database
     * @throws SQLException if database cannot be created
     */
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    /**
     * Initialize database, create table if it doesnt exist
     * @return true if database initialized, false if error
     */
    public static boolean initializeDatabase(){
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS job_applications(
                application_id INTEGER PRIMARY KEY AUTOINCREMENT,
                company TEXT NOT NULL,
                position TEXT NOT NULL,
                status TEXT NOT NULL,
                salary REAL NOT NULL,
                location TEXT NOT NULL,
                work_structure TEXT NOT NULL,
                application_date TEXT NOT NULL,
                last_updated_date TEXT NOT NULL,
                application_url TEXT,
                follow_up_needed INTEGER NOT NULL DEFAULT 0,
                UNIQUE(company,position))""";

    try(Connection connection = getConnection();
    Statement statement = connection.createStatement()){
        statement.execute(createTableSQL);
        return true;
    }
    catch (SQLException e){
        System.out.println("Database not loaded");
        System.out.println(e.getMessage());
        return false;
    }
    }
}
