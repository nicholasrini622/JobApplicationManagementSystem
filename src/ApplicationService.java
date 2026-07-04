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
        if (application == null) {
            return false;
        }
        if (application.getCompany() == null || application.getCompany().isBlank()) {
            return false;
        }
        if (application.getPosition() == null || application.getPosition().isBlank()) {
            return false;
        }
        if (application.getLocation() == null || application.getLocation().isBlank()) {
            return false;
        }
        if (application.getStatus() == null) {
            return false;
        }
        if (application.getWorkStructure() == null) {
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
        if (!validateApplication(application)) {
            return false;
        }
        if (isDuplicateApplication(application)) {
            return false;
        }
        application.setApplicationID(createApplicationID());
        application.setLastUpdatedDate(LocalDate.now());
        applications.add(application);
        return true;
    }


    public boolean validateSalary(double salary) {
        if (salary >= 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean validateDate(LocalDate date){
        if(date == null){
            return false;
        }
        if(date.isAfter(LocalDate.now())){
            return false;
        }
        else{
            return true;
        }
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
}
