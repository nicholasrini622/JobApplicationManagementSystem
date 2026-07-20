/*
Nicholas Rini
Software Development I
07/05/2026
Class: JobApplication
Purpose: JobApplication class stores the data for a single job application record.  Records include a company,position,status,
salary, location, workStructure, date, optional URL, and a follow-up status.  LocalDate used for formatting and manipulation.
 */
import java.time.LocalDate;

/**
 * Manages a single job application record
 * Class stores job application record application ID, company, position,
 * status, salary, location, work structure, application date, application URL, and
 * a follow up check
 */
public class JobApplication {
    private int applicationID;
    private String company;
    private String position;
    private ApplicationStatus status;
    private double salary;
    private String location;
    private WorkStructure workStructure;
    private LocalDate applicationDate;
    private LocalDate lastUpdatedDate;
    private String applicationUrl;
    private boolean followUpNeeded;

    /**
     * Constructor - Creates empty record with default values
     * Application and last updated date are set to current local date
     */
    public JobApplication(){
        this.company = "";
        this.position = "";
        this.location = "";
        this.applicationDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
        this.applicationUrl = "";

    }

    /**
     * Create a job application record
     * @param applicationID unique application ID
     * @param company company name applied to
     * @param position position applied to
     * @param status status of current application
     * @param salary salary for current application
     * @param location location of the job applied to
     * @param workStructure Structure of job such as ONSITE,REMOTE,HYBRID
     * @param applicationDate date an application was applied to
     * @param lastUpdatedDate date an application was last updated
     * @param applicationUrl option field for url of job application
     * @param followUpNeeded checks whether an application needs a follow-up
     */
    public JobApplication(int applicationID, String company, String position, ApplicationStatus status, double salary, String location,
    WorkStructure workStructure, LocalDate applicationDate, LocalDate lastUpdatedDate, String applicationUrl, boolean followUpNeeded){
        this.applicationID = applicationID;
        this.company = company;
        this.position = position;
        this.status = status;
        this.salary = salary;
        this.location = location;
        this.workStructure = workStructure;
        this.applicationDate = applicationDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.applicationUrl = applicationUrl;
        this.followUpNeeded = followUpNeeded;
    }

    /**
     * Get an application ID
     * @return applicationID
     */
    public int getApplicationID() {
        return applicationID;
    }

    /**
     * Set an application ID
     * @param applicationID application ID
     */
    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    /**
     * Get company name
     * @return String company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * Set a company name
     * @param company company name of job application
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Get job position
     * @return position of job applied to
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set job position
     * @param position position applied to
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Get application Status
     * @return application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Set an application status
     * @param status status of application i.e. APPLIED
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Get an application salary
     * @return job salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Set job salary
     * @param salary salary for a job/application
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Get location of job applied to
     * @return Location of job applied to
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set location of job applied to
     * @param location location of job applied to
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get work structure for job applied to
     * @return WorkStructure of job applied to
     */
    public WorkStructure getWorkStructure() {
        return workStructure;
    }

    /**
     * Set work structure for job applied to
     * @param workStructure workStructure of job applied to i.e. REMOTE
     */
    public void setWorkStructure(WorkStructure workStructure) {
        this.workStructure = workStructure;
    }

    /**
     * Get date of job applied for
     * @return application date
     */
    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    /**
     * Set application date of job applied for
     * @param applicationDate application date
     */
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * Get last update date for job application
     * @return last updated date
     */
    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * Set date application was last updated
     * @param lastUpdatedDate date application was last updated
     */
    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * Get applicationURL for job applied to
     * @return application URL
     */
    public String getApplicationUrl() {
        return applicationUrl;
    }

    /**
     *Set application URL for job applied to
     * @param applicationUrl application URL for job application
     */
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    /**
     * Check if an application needs a follow-up
     * @return true if follow up needed, false if not
     */
    public boolean isFollowUpNeeded() {
        return followUpNeeded;
    }

    /**
     * Set if an application needs a follow-up
     * @param followUpNeeded true if application needs follow up, false if not needed
     */
    public void setFollowUpNeeded(boolean followUpNeeded) {
        this.followUpNeeded = followUpNeeded;
    }

    /**
     * Create a formatted display of all fields for job application record
     * @return formatted string of job application records
     */
    @Override
    public String toString(){
        String applicationStatusDisplay = (status!= null) ? status.getDisplayName() : "N/A";
        String workStructureDisplay = (workStructure != null) ? workStructure.getDisplayName() : "N/A";
        String applicationDateDisplay = (applicationDate != null) ? applicationDate.toString() : "N/A";
        String lastUpdatedDisplay = (lastUpdatedDate != null) ? lastUpdatedDate.toString() : "N/A";
        String salaryDisplay = (salary > 0) ? "$" + salary : "N/A";
        String urlDisplay = (applicationUrl != null && !applicationUrl.isBlank()) ? applicationUrl : "N/A";

        return "\n**********" + "\nJob Application Record ~" + "\n**********" +
                "\nApplication ID: " + applicationID + "\nCompany: " + company + "\nPoisition: " + position
                + "\nStatus: " + applicationStatusDisplay + "\nSalary: " + salaryDisplay + "\nLocation: " + location
                + "\nWork Structure: " + workStructureDisplay + "\nApplication Date: " + applicationDateDisplay +
                "\nLast Updated: " + lastUpdatedDisplay + "\nApplication URL: " + urlDisplay + "\nFollow-up Alert: " + followUpNeeded
                + "\n**********";
    }
}
