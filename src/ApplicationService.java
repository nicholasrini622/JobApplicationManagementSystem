import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ApplicationService {
    private ArrayList<JobApplication> applications;
    private int followUpLimit;

    public ApplicationService(int followUpLimit) {
        applications = new ArrayList<>();
        if (followUpLimit > 0) {
            this.followUpLimit = followUpLimit;
        } else {
            this.followUpLimit = 7;
        }
    }

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

    public boolean addApplication(JobApplication application) {
        if (!validateApplication(application) || isDuplicateApplication(application)) {
            return false;
        }
        application.setApplicationID(createApplicationID());
        application.setLastUpdatedDate(LocalDate.now());
        applications.add(application);
        return true;
    }

    public boolean updateApplication(JobApplication application) {
        if (!validateApplication(application)) {
            return false;
        }
        JobApplication currentApplication = findApplicationById(application.getApplicationID());
        if (currentApplication == null) {
            return false;
        }
        for (JobApplication app : applications) {
            if (app.getApplicationID() != application.getApplicationID()) {
                if (app.getCompany().equalsIgnoreCase(application.getCompany()) && app.getPosition().equalsIgnoreCase(application.getPosition())) {
                    return false;
                }
            }
        }
        currentApplication.setCompany(application.getCompany());
        currentApplication.setPosition(application.getPosition());
        currentApplication.setLocation(application.getLocation());
        currentApplication.setStatus(application.getStatus());
        currentApplication.setWorkStructure(application.getWorkStructure());
        currentApplication.setSalary(application.getSalary());
        currentApplication.setApplicationDate(application.getApplicationDate());
        currentApplication.setLastUpdatedDate(LocalDate.now());
        return true;
    }

    public boolean removeApplication(int applicationID) {
        JobApplication application = findApplicationById(applicationID);
        if (application == null) {
            return false;
        }
        applications.remove(application);
        return true;
    }

    public JobApplication findApplicationById(int applicationID) {
        for (JobApplication application : applications) {
            if (application.getApplicationID() == applicationID) {
                return application;
            }
        }
        return null;
    }

    public ArrayList<JobApplication> getAllApplications() {
        return applications;
    }

    private int createApplicationID() {
        int maxID = 0;
        for (JobApplication application : applications) {
            if (application.getApplicationID() > maxID) {
                maxID = application.getApplicationID();
            }
        }
        return maxID + 1;
    }

    public ArrayList<JobApplication> sortApplications(String sortField) {
        ArrayList<JobApplication> sortedList = new ArrayList<>(applications);
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
    public ArrayList<JobApplication> filterByStatus(ApplicationStatus status){
        ArrayList<JobApplication> filterList = new ArrayList<>();
        for(JobApplication application: applications){
            if(application.getStatus() == status){
                filterList.add(application);
            }
        }
        return filterList;
    }

    private boolean isDuplicateApplication(JobApplication applicationCopy) {
        for (JobApplication application : applications) {
            if (application.getCompany().equalsIgnoreCase(applicationCopy.getCompany()) && application.getPosition().equalsIgnoreCase(applicationCopy.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFollowUpNeeded(JobApplication application) {
        if (application == null || application.getStatus() == ApplicationStatus.DENIED || application.getLastUpdatedDate() == null) {
            return false;
        }
        LocalDate followUpDate = LocalDate.now().minusDays(followUpLimit);

        return !application.getLastUpdatedDate().isAfter(followUpDate);
    }
    public ArrayList<JobApplication> getFollowUpApplications(){
        ArrayList<JobApplication> followUp = new ArrayList<>();
        for(JobApplication application : applications){
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

    public boolean validateSalary(double salary) {
        return salary >= 0;
    }

    public boolean validateDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
}

