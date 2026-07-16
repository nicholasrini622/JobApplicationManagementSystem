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

public class ApplicationService {
    private int followUpLimit;
    private JobApplicationRepository repository;

    public ApplicationService(int followUpLimit) {
        if (followUpLimit > 0) {
            this.followUpLimit = followUpLimit;
        } else {
            this.followUpLimit = 7;
        }
        this.repository = new JobApplicationRepository();
    }
/*
Method: validateApplication
Purpose:  Validates if a job application has all the required fields before storing the record.
Parameters: JobApplication application
Return:  Boolean - true if valid, false if record is invalid.
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
/*
Method: addApplication
Purpose:  Adds valid unique job application to ArrayList<JobApplication>
Parameters: JobApplication application
Return:  Boolean - true if application record is added, false if invalid or non-unique.
 */
    public boolean addApplication(JobApplication application) {
        if (!validateApplication(application) || isDuplicateApplication(application,0)) {
            return false;
        }
        application.setLastUpdatedDate(LocalDate.now());
        return repository.addApplication(application);
    }
/*
Method: updateApplication
Purpose:  Update an existing application record with valid information
Parameters: JobApplication application
Return: boolean - true if record was updated, false if the record was invalid, a duplicate, or missing.
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
        return repository.updateApplication(application);
        }

/*
Method: removeAppplication
Purpose:  Remove  a jobApplication record from the list based on applicationID
Parameters: int applicationID
Return: boolean - true if application was removed, false if there isn't a matching ID
 */
    public boolean removeApplication(int applicationID) {
        JobApplication application = findApplicationById(applicationID);
        if (application == null) {
            return false;
        }
        return repository.removeApplication(applicationID);
    }
/*
Method: findApplicationById
Purpose: This method searches the application record list for any record with a matching ID
Parameters: int applicationID
Returns; JobApplication or null if no match
 */
    public JobApplication findApplicationById(int applicationID) {
        return repository.findApplication(applicationID);
    }
/*
Method: getAllApplications()
Purpose: Gives access to current ArrayList<JobApplication>
Parameter: None
Return:  ArrayList<JobApplication>
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

/*
Method: sortApplications
Purpose: Create a sorted application record list copy based on a selected field
Parameters: String sortField
Return: sorted ArrayList<JobApplication>
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
/*
Method: filterByStatus
Purpose: Create a filtered application list based on a certain status
Parameters: ApplicationStatus status
Return: filtered ArrayList<JobApplication>
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
/*
Method: isDuplicateApplication
Purpose: Check if an existing application already has the same company and position
Parameters: JobApplication applicationCopy
Return: boolean - true if duplicate is found, false if no duplicate is found.
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
/*
Method: isFollowUpNeeded
Purpose: Check if a application record needs a follow-up based off last updated date
Parameters: JobApplication application
Return: boolean - true if follow-up is required, false if no follow-up needed
 */
    public boolean isFollowUpNeeded(JobApplication application) {
        if (application == null || application.getStatus() == ApplicationStatus.DENIED || application.getLastUpdatedDate() == null) {
            return false;
        }
        LocalDate followUpDate = LocalDate.now().minusDays(followUpLimit);

        return !application.getLastUpdatedDate().isAfter(followUpDate);
    }
/*
Method: getFollowUpApplications()
Purpose: Method will create a list of applications that need a follow-up
Parameters: None
Return: ArrayList<JobApplication> records that need a follow-up.
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
/*
Method: validateSalary
Purpose: Validation for salary field
Parameters: double salary
Return: boolean - true if salary is >=0, false it the salary is a negative number
 */
    public boolean validateSalary(double salary) {
        return salary >= 0;
    }
/*
Method: validateDate
Purpose: Validation for application date.  Checks if date is from the future
Parameters: LocalDate date
Return: boolean - true if date is valid, false if date is null or from the future.
 */
    public boolean validateDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
}

