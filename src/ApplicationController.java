/*
Nicholas Rini
Software Development I
07/05/2026
Class: ApplicationController
Purpose: Controller class connects the ConsoleView, ApplicationService, and ImportService classes
 */
import java.util.ArrayList;

/**
 * Handles communication between ConsoleView, ApplicationService, and ImportService.  Process menu choices
 * send application to ApplicationService, and tell ConsoleView messages or records to display
 */
public class ApplicationController {
    private ConsoleView view;
    private ApplicationService applicationService;
    private ImportService importService;

    /**
     * Create ApplicationController with ConsoleView and service classes
     * @param view ConsoleView for input and output
     * @param applicationService ApplicationService to manage job applications
     * @param importService ImportService to import job applications
     */
    public ApplicationController(ConsoleView view, ApplicationService applicationService, ImportService importService){
        this.view = view;
        this.applicationService = applicationService;
        this.importService = importService;
    }

    /**
     * Sends job application record to ApplicationService
     * @param application job application record to add
     * @return boolean - true if application is added, false if invalid or duplicate
     */
    public boolean processAddApplication(JobApplication application){
        if(applicationService.addApplication(application)){
            view.showMessage("Application added!");
            return true;
        }
        view.showError("Application was not added.  May contain duplicate or invalid data.");
        return false;
    }

    /**
     * Send updated application to ApplicationService to update record
     * @param application job application record containing updated information
     * @return boolean - true if application is updated, false if failed
     */
    public boolean processUpdateApplication(JobApplication application){
        if(applicationService.updateApplication(application)){
            view.showMessage("Application successfully updated.");
            return true;
        }
        view.showError("Application could not be updated. Record can not be duplicate and ID must exist.");
        return false;
    }

    /**
     * Checks for a job application by ID, confirm removal, and remove record
     * @param applicationId applicationID of application record to remove
     * @return boolean - true if application is removed, false if not removed
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

    /**
     * Import valid job application records from a text file and display them
     * @param filePath path of text file to import
     * @return int for number of valid application records/rows created
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

    /**
     * Get application records from ApplicationService
     * @return ArrayList of all job application records
     */
    public ArrayList<JobApplication> viewApplications(){
        return applicationService.getAllApplications();
    }

    /**
     * Filter application records by a status and display results
     * @param status application status to filter by
     * @return ArrayList containing all filtered applications
     */
    public ArrayList<JobApplication> processFilterApplication(ApplicationStatus status){
        ArrayList<JobApplication> filterList = applicationService.filterByStatus(status);
        view.displayAllApplications(filterList);
        return filterList;
    }

    /**
     * Sorts applications by a selected field and display the results
     * @param sortField field to sort application records by
     * @return ArrayList containing sorted applications
     */
    public ArrayList<JobApplication> processSortApplications(String sortField){
        ArrayList<JobApplication> sortedList = applicationService.sortApplications(sortField);
        view.displayAllApplications(sortedList);
        return sortedList;
    }

    /**
     * Retrieve applications that may require a follow-up and display results
     * @return ArrayList containing job applications needing a follow-up
     */
    public ArrayList<JobApplication> processFollowUp(){
        ArrayList<JobApplication> followUpList = applicationService.getFollowUpApplications();
        view.showFollowUp(followUpList);
        return followUpList;
    }

    /**
     * Runs main console menu and processes user choices
     * @return boolean - true when menu loop ends
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
