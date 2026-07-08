/*
Nicholas Rini
Software Development I
07/05/2026
Class: ApplicationController
Purpose: Controller class connects the ConsoleView, ApplicationService, and ImportService classes
 */
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
/*
Method: processAddApplication
Purpose: Send created job application to applicationService
Parameters: JobApplication application
Return: boolean - true if application was added, false if the application is invalid or a duplicate
 */
    public boolean processAddApplication(JobApplication application){
        if(applicationService.addApplication(application)){
            view.showMessage("Application added!");
            return true;
        }
        view.showError("Application was not added.  May contain duplicate or invalid data.");
        return false;
    }
/*
Method: processUpdateApplication
Purpose: Send updated job application to ApplicationService to update application record
Parameters: JobApplication application
Return: boolean - true if application was updated, false if the update failed
 */
    public boolean processUpdateApplication(JobApplication application){
        if(applicationService.updateApplication(application)){
            view.showMessage("Application successfully updated.");
            return true;
        }
        view.showError("Application could not be updated. Record can not be duplicate and ID must exist.");
        return false;
    }
/*
Method: processRemoveApplication
Purpose:  Locate job application by application ID, confirms removal, and removes the application record
Parameters: int applicationId
Return: boolean - true if application is removed, false is application removal failed or cancelled.
 */
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
/*
Method: processImportApplications
Purpose:  Imports job aplpications from text file and display valid records
Parameters: String filePath
Return: int (number of valid applications imported)
 */
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
/*
Method: viewApplications()
Purpose:  Get current job application records from application service class
Parameters: None
Return: ArrayList<JobApplication>
 */
    public ArrayList<JobApplication> viewApplications(){
        return applicationService.getAllApplications();
    }
/*
Method: processFilterApplications
Purpose: Filter job application records by application status and display records
Parameters: ApplicationStatus status
Return: filtered ArrayList<JobApplication>
 */
    public ArrayList<JobApplication> processFilterApplication(ApplicationStatus status){
        ArrayList<JobApplication> filterList = applicationService.filterByStatus(status);
        view.displayAllApplications(filterList);
        return filterList;
    }
/*
Method: processSortApplications
Purpose: Sort application records by a selected field, display the results
Parameters: String sortField
Return: sorted ArrayList<JobApplication>
 */
    public ArrayList<JobApplication> processSortApplications(String sortField){
        ArrayList<JobApplication> sortedList = applicationService.sortApplications(sortField);
        view.displayAllApplications(sortedList);
        return sortedList;
    }
/*
Method: processFollowUp()
Purpose: Retrieve applications that need a follow-up and display them
Parameters: None
Return: ArrayList<JobApplication> of records that need a follow-up
 */
    public ArrayList<JobApplication> processFollowUp(){
        ArrayList<JobApplication> followUpList = applicationService.getFollowUpApplications();
        view.showFollowUp(followUpList);
        return followUpList;
    }
/*
Method: runMenu()
Purpose: Run the main CLi menu loop and process user choices
Parameters: None
Return: boolean - true after loop ends (did not want to create void methods)
 */
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
                        JobApplication updatedApplication = view.UpdatedApplicationInput(currentApplication);
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
