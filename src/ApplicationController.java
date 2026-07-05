import java.util.ArrayList;
public class ApplicationController {
    private ConsoleView view;
    private ApplicationService applicationService;
    private ImportService importService;
    public ApplicationController(ConsoleView view, ApplicationService applicationService, ImportService importService){
        this.view = view;
        this.applicationService = applicationService;
        this.importService = importService;
    }
    public boolean processAddApplication(JobApplication application){
        if(applicationService.addApplication(application)){
            view.showMessage("Application added!");
            return true;
        }
        view.showError("Application was not added.  May contain duplicate or invalid data.");
        return false;
    }
    public boolean processUpdateApplication(JobApplication application){
        if(applicationService.updateApplication(application)){
            view.showMessage("Application successfully updated.");
            return true;
        }
        view.showError("Application could not be updated. Record can not be duplicate and ID must exist.");
        return false;
    }
    public boolean processRemoveApplication(int applicationId){
        JobApplication application = applicationService.findApplicationById(applicationId);
        if(application == null){
            view.showError("Application not found.  Record not removed.");
            return false;
        }
        if(!view.deleteConfirmation(application)){
            view.showMessage("User cancelled removal.  No record removed.");
            return false;
        }
        if(applicationService.removeApplication(applicationId)){
            view.showMessage("Application removed.");
            return true;
        }
        view.showError("Application not removed.");
        return false;
    }
    public int processImportApplications(String filePath){
        ArrayList<JobApplication> importApplications = importService.importApplications(filePath);
        if(importApplications.isEmpty()){
            view.showMessage("No applications imported.");
        }
        else{
            view.showMessage("Applications imported.  Record List:");
            view.displayAllApplications(importApplications);
        }
        view.showMessage("Skipped invalid record - " + importService.getInvalidRecordCount());
        return importApplications.size();
    }
    public ArrayList<JobApplication> viewApplications(){
        return applicationService.getAllApplications();
    }
    public ArrayList<JobApplication> processFilterApplication(ApplicationStatus status){
        ArrayList<JobApplication> filterList = applicationService.filterByStatus(status);
        view.displayAllApplications(filterList);
        return filterList;
    }
    public ArrayList<JobApplication> processSortApplications(String sortField){
        ArrayList<JobApplication> sortedList = applicationService.sortApplications(sortField);
        view.displayAllApplications(sortedList);
        return sortedList;
    }
    public ArrayList<JobApplication> processFollowUp(){
        ArrayList<JobApplication> followUpList = applicationService.getFollowUpApplications();
        view.showFollowUp(followUpList);
        return followUpList;
    }
    public boolean runMenu(){
        boolean running = true;
        while(running){
            view.displayMenu();
            int choice = view.menuChoice();
            switch(choice){
                case 1:
                    JobApplication newApplication = view.collectApplicationInput();
                    processAddApplication(newApplication);
                    break;
                case 2:
                    int removeID = view.readApplicationId();
                    processRemoveApplication(removeID);
                    break;
                case 3:
                    int updateID = view.readApplicationId();
                    JobApplication currentApplication = applicationService.findApplicationById(updateID);
                    if(currentApplication == null){
                        view.showError("Application ID not found.  No records were changed.");
                    }
                    else{
                        view.showMessage("Current application record to update");
                        System.out.println(currentApplication);
                        JobApplication updatedApplication = view.collectApplicationInput();
                        updatedApplication.setApplicationID(updateID);
                        processUpdateApplication(updatedApplication);

                    }
                    break;
                case 4:
                    view.displayAllApplications(viewApplications());
                    break;
                case 5:
                ApplicationStatus status = view.readStatusFilter();
                processFilterApplication(status);
                break;

                case 6:
                    String sortField = view.readSortField();
                    processSortApplications(sortField);
                    break;
                case 7:
                    String filePath = view.readFilePath();
                    processImportApplications(filePath);
                    break;
                case 8:
                    processFollowUp();
                    break;
                case 9:
                    view.showMessage("You miss 100% of the shots you dont take ~ Michael Scott");
                    running = false;
                    break;
            }
        }
        return true;
    }

}
