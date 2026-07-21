/*
Nicholas Rini
Software Development I
07/05/2026
Class: ApplicationService
Purpose:  This class will manage validation, adding, updating, removing, sorting, filtering, and follow-up check for
ArrayList<JobApplication>
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Handles the main logic for job application records
 * Validate applications, add applications, update applications, remove, sort/filter, and perform follow-up check
 */
public class ApplicationService {
    private int followUpLimit;
    private JobApplicationRepository repository;

    /**
     * Create an ApplicationService object with follow-up limit
     * If follow up limit is not >0, 7 day limit will be used
     * @param followUpLimit days before a follow up is considered needed
     */
    public ApplicationService(int followUpLimit) {
        if (followUpLimit > 0) {
            this.followUpLimit = followUpLimit;
        } else {
            this.followUpLimit = 7;
        }
        this.repository = new JobApplicationRepository();
    }

    /**
     * Validates job application records, maintains that record has all required fields
     * @param application JobApplication record to validate
     * @return boolean - true if valid, false if not
     */
    public boolean validateApplication(JobApplication application) {
        if (application == null || application.getStatus() == null || application.getWorkStructure() == null) {
            return false;
        }
        if (application.getCompany() == null || application.getCompany().isBlank() || application.getPosition() == null || application.getPosition().isBlank()
                || application.getLocation() == null || application.getLocation().isBlank()) {
            return false;
        }
        if (!validateSalary(application.getSalary())) {
            return false;
        }
        if (!validateDate(application.getApplicationDate())) {
            return false;
        }
        return true;
    }

    /**
     * Add a valid unique application to database.
     * Last updated date is set to current date
     * @param application application to add
     * @return boolean - true if application is added, false if invalid or duplicate
     */
    public boolean addApplication(JobApplication application) {
        if (!validateApplication(application) || isDuplicateApplication(application,0)) {
            return false;
        }
        application.setLastUpdatedDate(LocalDate.now());
        return repository.addApplication(application);
    }

    /**
     * Update job application with valid information.  Updated application can not exist or become a duplicate
     * @param application job application record to update
     * @return true if updated, false if invalid, missing, or becomes a duplicate
     */
    public boolean updateApplication(JobApplication application) {
        if (!validateApplication(application)) {
            return false;
        }
        JobApplication currentApplication = findApplicationById(application.getApplicationID());
        if (currentApplication == null) {
            return false;
        }
        if(isDuplicateApplication(application, application.getApplicationID())){
            return false;
            }
        application.setLastUpdatedDate(LocalDate.now());
        boolean updated = repository.updateApplication(application);
        if(!updated){
            System.out.println("Update failed: rep[ository/database update failed");
        }
        return updated;
        }

    /**
     * Remove application from database based off applicationID
     * @param applicationID ID of application record to remove
     * @return boolean - true if record was removed, false if no application is found to remove
     */
    public boolean removeApplication(int applicationID) {
        JobApplication application = findApplicationById(applicationID);
        if (application == null) {
            return false;
        }
        return repository.removeApplication(applicationID);
    }

    /**
     * Locate a job application based off applicationID
     * @param applicationID ID of application to find
     * @return matching JobApplication record, or null if no record could be found
     */
    public JobApplication findApplicationById(int applicationID) {
        return repository.findApplication(applicationID);
    }

    /**
     * Grabs all applications from the database
     * @return Arraylist containing all job applications
     */
    public ArrayList<JobApplication> getAllApplications() {
        return repository.getAllApplications();
    }
/*
Method: createApplicationID()
Purpose:  Finds the highest current ID and adds one to create next application ID
Parameters: None
Return: int
 */
    /*
    private int createApplicationID() {
        int maxID = 0;
        for (JobApplication application : applications) {
            if (application.getApplicationID() > maxID) {
                maxID = application.getApplicationID();
            }
        }
        return maxID + 1;
    }
    */

    /**
     * Create a sorted list of job application records based on selected sort field, company, position, salary, and date
     * @param sortField field to sort by
     * @return sorted ArrayList
     */
    public ArrayList<JobApplication> sortApplications(String sortField) {
        ArrayList<JobApplication> sortedList = new ArrayList<>(getAllApplications());
        if (sortField == null || sortField.isBlank()) {
            return sortedList;
        }
        switch (sortField.trim().toLowerCase()) {
            case "company":
                Collections.sort(sortedList, new Comparator<JobApplication>() {
                    @Override
                    public int compare(JobApplication a, JobApplication b) {
                        return a.getCompany().compareToIgnoreCase(b.getCompany());
                    }
                });
                break;

            case "position":
                Collections.sort(sortedList, new Comparator<JobApplication>() {
                    @Override
                    public int compare(JobApplication a, JobApplication b) {
                        return a.getPosition().compareToIgnoreCase(b.getPosition());
                    }
                });
                break;

            case "salary":
                Collections.sort(sortedList, new Comparator<JobApplication>() {
                    @Override
                    public int compare(JobApplication a, JobApplication b) {
                        return Double.compare(b.getSalary(), a.getSalary());
                    }
                });
                break;

            case "date":
                Collections.sort(sortedList, new Comparator<JobApplication>() {
                    @Override
                    public int compare(JobApplication a, JobApplication b) {
                        return a.getApplicationDate().compareTo(b.getApplicationDate());
                    }
                });
                break;
            default:
                break;
        }
        return sortedList;
    }

    /**
     * Create a filtered list based off ApplicationStatus
     * @param status application status to filter by
     * @return a filtered ArrayList based off selected status.
     */
    public ArrayList<JobApplication> filterByStatus(ApplicationStatus status){
        ArrayList<JobApplication> filterList = new ArrayList<>();
        for(JobApplication application: getAllApplications()){
            if(application.getStatus() == status){
                filterList.add(application);
            }
        }
        return filterList;
    }

    /**
     * Check if application has same company and position.  Ignore Application ID
     * @param applicationCopy application to check for duplicate
     * @param currentApplicationID id of current application, or 0 if application is new
     * @return true if a duplicate exists, false if no duplicates found
     */
    private boolean isDuplicateApplication(JobApplication applicationCopy, int currentApplicationID) {
        for (JobApplication application : getAllApplications()) {
            boolean sameCompany = application.getCompany().equalsIgnoreCase(applicationCopy.getCompany());
            boolean samePosition = application.getPosition().equalsIgnoreCase(applicationCopy.getPosition());
            boolean otherApplication = application.getApplicationID() != currentApplicationID;
            if(sameCompany && samePosition && otherApplication){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a job application record requires a follow-up.  Applications marked as denied should not be marked
     * @param application JobApplication record to check
     * @return boolean - true if follow up is needed, false if no follow up is needed.
     */
    public boolean isFollowUpNeeded(JobApplication application) {
        if (application == null || application.getStatus() == ApplicationStatus.DENIED || application.getLastUpdatedDate() == null) {
            return false;
        }
        LocalDate followUpDate = LocalDate.now().minusDays(followUpLimit);

        return !application.getLastUpdatedDate().isAfter(followUpDate);
    }

    /**
     * Creates a list of job application records that need a follow up
     * @return ArrayList of job applications that need follow up
     */
    public ArrayList<JobApplication> getFollowUpApplications(){
        ArrayList<JobApplication> followUp = new ArrayList<>();
        for(JobApplication application : getAllApplications()){
            if(application != null){
                if(isFollowUpNeeded(application)){
                    application.setFollowUpNeeded(true);
                    followUp.add(application);
                }
                else{
                    application.setFollowUpNeeded(false);
                }
            }
        }
        return followUp;
    }

    /**
     * Checks if a salary is negative
     * @param salary salary to validate
     * @return true if salary is not negative, false if it is negative
     */
    public boolean validateSalary(double salary) {
        return salary >= 0;
    }

    /**
     * Validate application date to check if not null and not in the future
     * @param date application date to validate
     * @return boolean - true if date is valid, false if null or future date
     */
    public boolean validateDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
}

