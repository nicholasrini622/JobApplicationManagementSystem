import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;

public class JobApplicationRepository {
    public JobApplicationRepository() {
        DBHelper.initializeDatabase();
    }

    public boolean addApplication(JobApplication application) {
        String sql = """
                INSERT INTO job_applications (company, position, status, salary, location, work_structure, application_date,
                last_updated_date, application_url, follow_up_needed) VALUES
                (?,?,?,?,?,?,?,?,?,?)""";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getCompany());
            statement.setString(2, application.getPosition());
            statement.setString(3, application.getStatus().name());
            statement.setDouble(4, application.getSalary());
            statement.setString(5, application.getLocation());
            statement.setString(6, application.getWorkStructure().name());
            statement.setString(7, application.getApplicationDate().toString());
            statement.setString(8, application.getLastUpdatedDate().toString());
            statement.setString(9, application.getApplicationUrl());
            statement.setInt(10, application.isFollowUpNeeded() ? 1 : 0);
            int rowsAdded = statement.executeUpdate();

            if (rowsAdded > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Application not added");
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean removeApplication(int applicationID){
        String sql = """
                DELETE FROM job_applications WHERE application_id = ?""";
        try(Connection connection = DBHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,applicationID);
            int rowsDeleted = statement.executeUpdate();
            if(rowsDeleted > 0){
                return true;
            }
        }
        catch(SQLException e){
            System.out.println("Application could not be removed");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ArrayList<JobApplication> getAllApplications() {
        ArrayList<JobApplication> applications = new ArrayList<>();
        String sql = """
                SELECT * FROM job_applications ORDER BY application_id""";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                JobApplication application = buildApplication(resultSet);
                applications.add(application);
            }
        } catch (SQLException e) {
            System.out.println("Application not loaded");
            System.out.println(e.getMessage());
        }
        return applications;
    }

    private JobApplication buildApplication(ResultSet resultSet) throws SQLException {
        int applicationID = resultSet.getInt("application_id");
        String company = resultSet.getString("company");
        String position = resultSet.getString("position");
        ApplicationStatus status = ApplicationStatus.fromString(resultSet.getString("status"));
        double salary = resultSet.getDouble("salary");
        String location = resultSet.getString("location");
        WorkStructure workStructure = WorkStructure.fromString(resultSet.getString("work_structure"));
        LocalDate applicationDate = LocalDate.parse(resultSet.getString("application_date"));
        LocalDate lastUpdatedDate = LocalDate.parse(resultSet.getString("last_updated_date"));
        String applicationUrl = resultSet.getString("application_url");
        boolean followUpNeeded = resultSet.getInt("follow_up_needed") == 1;

        return new JobApplication(applicationID, company, position, status, salary, location, workStructure, applicationDate, lastUpdatedDate, applicationUrl, followUpNeeded);
    }

    public JobApplication findApplication(int applicationID) {
        String sql = """
                SELECT * FROM job_applications WHERE application_id = ?""";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, applicationID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return buildApplication(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Application wasn't found");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean updateApplication(JobApplication application) {
        String sql = """
                UPDATE job_applications
                SET company = ?, position = ?, status = ?, location = ?, work_Structure = ?, application_date = ?, last_updated_date = ?, application_url = ?
                follow_up_needed = ? WHERE application_id = ?""";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getCompany());
            statement.setString(2, application.getPosition());
            statement.setString(3, application.getStatus().name());
            statement.setDouble(4, application.getSalary());
            statement.setString(5, application.getLocation());
            statement.setString(6, application.getWorkStructure().name());
            statement.setString(7, application.getApplicationDate().toString());
            statement.setString(8, application.getLastUpdatedDate().toString());
            statement.setString(9, application.getApplicationUrl());
            statement.setInt(10, application.isFollowUpNeeded() ? 1 : 0);
            statement.setInt(11, application.getApplicationID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Application was not updated");
            System.out.println(e.getMessage());

        }
        return false;
    }
}
