/**
 * Console Version of the Job Application Management System.  Class creates ApplicationService,
 * ImportService, ConsoleView, and ApplicationController and starts the main menu loop
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