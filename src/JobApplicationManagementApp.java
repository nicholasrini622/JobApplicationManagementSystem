/*
Nicholas Rini
Software Development I
07/05/2026
JobApplicationManagementApp
This is the main class that will perform the runMenu() function.  The user will be able to perform CRUD operations, sort, filter, and check for follow-up
alerts for Job Application records.  The user shall be able to import records from a text-file or manually enter them.  This program will evolve in to an all-in
one storage and operation center for Job Application Records.
 */

public class JobApplicationManagementApp {
    public static void main(String[] args){
        ApplicationService applicationService = new ApplicationService(7);
        ImportService importService = new ImportService(applicationService);
        ConsoleView consoleView = new ConsoleView();
        ApplicationController controller = new ApplicationController(consoleView,applicationService,importService);
        consoleView.setController(controller);
        controller.runMenu();
    }
}
