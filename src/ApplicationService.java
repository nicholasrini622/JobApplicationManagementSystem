import java.time.LocalDate;
import java.util.ArrayList;
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
        if (application == null || application.getStatus() == null || application.getWorkStructure() == null ) {
            return false;
        }
        if (application.getCompany() == null || application.getCompany().isBlank() || application.getPosition() == null || application.getPosition().isBlank()
        || application.getLocation() == null || application.getLocation().isBlank()){
            return false;
        }
        if (application.getSalary() < 0){
            return false;
        }
        if (application.getApplicationDate() == null || application.getApplicationDate().isAfter(LocalDate.now())){
            return false;
        }
        return true;
    }

    public boolean addApplication(JobApplication application) {
        if (!validateApplication(application) || isDuplicateApplication(application)){
            return false;
        }
        application.setApplicationID(createApplicationID());
        application.setLastUpdatedDate(LocalDate.now());
        applications.add(application);
        return true;
    }
    public boolean updateApplication(JobApplication application){
        if(!validateApplication(application)){
            return false;
        }
        JobApplication currentApplication = findApplicationById(application.getApplicationID());
        if(currentApplication == null){
            return false;
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
    public JobApplication findApplicationById(int applicationID){
        for(JobApplication application : applications){
            if(application.getApplicationID() == applicationID){
                return application;
            }
        }
        return null;
    }

    private int createApplicationID(){
        int maxID = 0;
        for(JobApplication application : applications){
            if(application.getApplicationID() > maxID){
                maxID = application.getApplicationID();
            }
        }
        return maxID + 1;
    }
    private boolean isDuplicateApplication(JobApplication applicationCopy){
        for(JobApplication application : applications){
            if(application.getCompany().equalsIgnoreCase(applicationCopy.getCompany()) && application.getPosition().equalsIgnoreCase(applicationCopy.getPosition())){
                return true;
            }
        }
        return false;
    }
    public boolean isFollowUpNeeded(JobApplication application){
        if(application == null || application.getStatus() == ApplicationStatus.DENIED || application.getLastUpdatedDate() == null){
            return false;
        }
        LocalDate followUpDate = LocalDate.now().minusDays(followUpLimit);

        return !application.getLastUpdatedDate().isAfter(followUpDate);
    }
}
