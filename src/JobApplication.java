import java.time.LocalDate;

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

    public JobApplication(){
        this.applicationID = 0;
        this.company = "";
        this.position = "";
        this.status = null;
        this.salary = 0.0;
        this.location = "";
        this.workStructure = null;
        this.applicationDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
        this.applicationUrl = "";
        this.followUpNeeded = false;

    }
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

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WorkStructure getWorkStructure() {
        return workStructure;
    }

    public void setWorkStructure(WorkStructure workStructure) {
        this.workStructure = workStructure;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public boolean isFollowUpNeeded() {
        return followUpNeeded;
    }

    public void setFollowUpNeeded(boolean followUpNeeded) {
        this.followUpNeeded = followUpNeeded;
    }
    @Override
    public String toString(){
        String applicationStatusDisplay;
        String workStructureDisplay;
        String applicationDateDisplay;
        String lastUpdatedDisplay;
        String salaryDisplay;
        String urlDisplay;
        if(status == null){
            applicationStatusDisplay = "N/A";
        }
        else{
            applicationStatusDisplay = status.getDisplayName();
        }
        if(workStructure == null){
            workStructureDisplay = "N/A";
        }
        else{
            workStructureDisplay = workStructure.getDisplayName();
        }
        if(applicationDate == null){
            applicationDateDisplay = "N/A";
        }
        else{
            applicationDateDisplay = applicationDate.toString();
        }
        if(lastUpdatedDate == null){
            lastUpdatedDisplay = "N/A";
        }
        else{
            lastUpdatedDisplay = lastUpdatedDate.toString();
        }
        if(salary <=0){
            salaryDisplay = "N/A";
        }
        else{
            salaryDisplay = "$" + salary;
        }
        if(applicationUrl == null || applicationUrl.isBlank()){
            urlDisplay = "n/A";
        }
        else{
            urlDisplay = applicationUrl;
        }
        return "\n**********" + "\nJob Application Record ~" + "\n**********" +
                "\nApplication ID: " + applicationID + "\nCompany: " + company + "\nPoisition: " + position
                + "\nStatus: " + applicationStatusDisplay + "\nSalary: " + salaryDisplay + "\nLocation: " + location
                + "\nWork Structure: " + workStructureDisplay + "\nApplication Date: " + applicationDateDisplay +
                "\nLast Updated: " + lastUpdatedDisplay + "\nApplication URL: " + urlDisplay + "\nFollow-up Alert: " + followUpNeeded
                + "\n**********";
    }
}
