/*
Nicholas Rini
Software Development I
07/05/2026
Class: ConsoleView
Purpose: Handles the console input and output of the Job Application Management System
Menu Display, input validation, error and confirmation messages, and record displays
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleView {
    private ApplicationController controller;
    private Scanner scanner;
    private String statusMessage;

    public ConsoleView(){
        this.controller = null;
        this.scanner = new Scanner(System.in);
        this.statusMessage = "";
    }
/*
Method: setController
Purpose: Connect ConsoleView to ApplicationController
Parameters: ApplicationController controller
Return: boolean - true after controller is assigned
 */
    public boolean setController(ApplicationController controller){
        this.controller = controller;
        return true;
    }
/*
Method: displayMenu
Purpose: Displays the main CLI menu to the user
Parameters: None
Return: boolean - true after menu is displayed (didn't want to add void method)
 */
    public boolean displayMenu(){
        System.out.println("\n**********");
        System.out.println("Job Application Management System");
        System.out.println("\n**********");
        System.out.println("1. Add Application");
        System.out.println("2. Remove Application");
        System.out.println("3. Update Application");
        System.out.println("4. View applications");
        System.out.println("5. Filter Applications");
        System.out.println("6. Sort Applications");
        System.out.println("7. Import Applications");
        System.out.println("8. View Follow-up Applications");
        System.out.println("9. Quit");
        return true;
    }
/*
Method: menuChoice
Purpose:  Read and validate user's menu choice
Parameters: none
Return: int
 */
    public int menuChoice(){
        while(true){
            System.out.println("Choose a menu option");
            String input = scanner.nextLine();
        try {
            int choice = Integer.parseInt(input);
            if(choice >=1 && choice <=9){
                return choice;
            }
            System.out.println("Invalid choice, enter a number 1-9.");
        }
        catch(NumberFormatException e){
            System.out.println("Input must be a valid menu choice.  Numbers 1-9 only.");
        }
        }

        }
/*
Method: collectApplicationInput
Purpose:  Collect and validate job application record details
Parameters: None
Return: JobApplication
 */
    public JobApplication collectApplicationInput(){
        String company = "";
        while(company.isBlank()){
            System.out.println("Enter the company name you applied to.");
            company = scanner.nextLine().trim();
            if(company.isBlank()){
                System.out.println("Company name must not be blank");
            }
        }
        String position = "";
        while(position.isBlank()){
            System.out.println("Enter the position you applied for.");
            position = scanner.nextLine().trim();
            if(position.isBlank()){
                System.out.println("Position must not be blank.");
            }
        }
        ApplicationStatus status = readStatusFilter();
        double salary = -1;
        while(salary < 0){
            System.out.println("Enter a salary amount or 0 for N/A");
            String input = scanner.nextLine();
            try{
                salary = Double.parseDouble(input);
                if(salary < 0){
                    System.out.println("Salary must be a positive number >= 0");
                }
            }
            catch(NumberFormatException e){
                System.out.println("Error: Salary must be only numbers.");
            }
        }
        String location = "";
        while(location.isBlank()){
            System.out.println("Enter the location of the job you applied for.");
            location = scanner.nextLine().trim();
            if(location.isBlank()){
                System.out.println("Error: Location can not be left blank.");
            }
        }
        WorkStructure workStructure = null;
        while(workStructure == null){
            System.out.println("\nWork Structure Options:");
            System.out.println("1. ONSITE");
            System.out.println("2. REMOTE");
            System.out.println("3. HYBRID");
            System.out.println("Choose a menu option number 1-3");
            String choice = scanner.nextLine().trim();
            if(choice.equals("1")){
                workStructure = WorkStructure.ONSITE;
            }
            else if(choice.equals("2")){
                workStructure = WorkStructure.REMOTE;
            }
            else if(choice.equals("3")){
                workStructure = WorkStructure.HYBRID;
            }
            else{
                System.out.println("Error, Invalid Selection!  Choose a number 1-3");
            }
        }
        LocalDate applicationDate = null;
        while(applicationDate == null){
            System.out.println("Enter application date yyyy/MM/dd formatting:");
            String input = scanner.nextLine().trim();
            try{
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                applicationDate = LocalDate.parse(input, dateFormat);
                if(applicationDate.isAfter(LocalDate.now())){
                    System.out.println("Marty?! Error: Application date can't be from the future.");
                    applicationDate = null;
                }
            }
            catch(DateTimeParseException e){
                System.out.println("Invalid format.  Date must use yyyy/MM/dd format");
            }
        }
        System.out.println("Enter optional URL or press enter.");
        String applicationUrl = scanner.nextLine().trim();
        return new JobApplication(0,company,position,status,salary,location,workStructure,applicationDate,LocalDate.now(),
                applicationUrl,false);
    }
/*
Method: readApplicationId
Purpose: Read applicationId entered by user, validate ID
Parameters: None
Return: int
 */
    public int readApplicationId(){
        while(true){
            System.out.println("Enter an application ID");
            String input = scanner.nextLine();
            try{
                int applicationId = Integer.parseInt(input);
                if(applicationId > 0){
                    return applicationId;
                }
                System.out.println("Application ID cannot be negative or 0.  ID must be > 0");
            }
            catch(NumberFormatException e){
                System.out.println("Application ID must be numerical.  Enter numbers only.");
            }
        }
    }
/*
Method: readSortField
Purpose: Read and validate what field user wants to sort by
Parameters: None
Return: String
 */
    public String readSortField(){
        while(true){
            System.out.println("\n Application Sorting Options");
            System.out.println("1. Company");
            System.out.println("2. Position");
            System.out.println("3. Salary");
            System.out.println("4. Date");
            System.out.println("Choose a menu number to sort applications by.");
            String choice = scanner.nextLine().trim();
            switch(choice){
                case "1":
                    return "company";
                case "2":
                    return "position";
                case "3":
                    return "salary";
                case "4":
                    return "date";
                default:
                    System.out.println("Error: Invalid choice, enter a number 1-4");
            }
        }
    }
/*
Method: displayAllApplications
Purpose: Display job application records passed in
Parameters: ArrayList<JobApplication>
Return: boolean - true after record or no application record found displayed
 */
    public boolean displayAllApplications(ArrayList<JobApplication> applications){
        System.out.println("\n*** Application Records***");
        if(applications == null || applications.isEmpty()){
            System.out.println("No application records found");
            return true;
        }
        for(JobApplication application : applications){
            System.out.println(application);
        }
        return true;
    }
/*
Method: showMessage
Purpose: Display a message to user
Parameters: String message
Return: boolean - true after a message is displayed
 */
    public boolean showMessage(String message){
        statusMessage = message;
        System.out.println(message);
        return true;
    }
/*
Method: showError
Purpose: Display error message to the user
Parameters: String error
Return: boolean - true after error message is displayed
 */
    public boolean showError(String error){
        statusMessage = error;
        System.out.println(error);
        return true;
    }
/*
Method: showFollowUp
Purpose: Display any applications that need a follow-up
Parameters: ArrayList<JobApplication> applications
Return: boolean - true after records or no alerts found displayed
 */
    public boolean showFollowUp(ArrayList<JobApplication> applications){
        System.out.println("\n*** Follow-UP ***");
        if(applications == null || applications.isEmpty()){
            System.out.println("No alerts found");
            return true;
        }
        for(JobApplication application : applications){
            System.out.println(application);
        }
        return true;
    }
/*
Method: deleteConfirmation
Purpose:  Display application record selected to be removed and ask for confirmation
Parameters: JobApplication application
Return: boolean - true after user confirms removal, false if the user decides to cancel or doesnt select a record
 */
    public boolean deleteConfirmation(JobApplication application){
        if(application == null){
            System.out.println("No application selected.");
            return false;
        }
        System.out.println("Here is the application you selected.");
        System.out.println(application);
        while(true){
            System.out.println("Remove this application? yes/no?");
            String input = scanner.nextLine().trim().toLowerCase();
                if(input.equals("yes") || input.equals("y")){
                    return true;
                }
                else if(input.equals("no") || input.equals("n")){
                    return false;
                }
                System.out.println("Error: Please choose yes or no, or y/n");
            }
        }
/*
Method: readStatusFilter
Purpose: read and validate a status to filter chosen by the user
Parameters: None
Return: ApplicationStatus
 */
    public ApplicationStatus readStatusFilter(){
        while(true){
            System.out.println("\nApplication status Options");
            System.out.println("1. Applied");
            System.out.println("2. Awaiting Response");
            System.out.println("3. Interviewing");
            System.out.println("4. In progress");
            System.out.println("5. Denied");
            System.out.println("Choose a menu number option to choose a status.");
            String choice = scanner.nextLine().trim();
            switch(choice){
                case "1":
                    return ApplicationStatus.APPLIED;
                case "2":
                    return ApplicationStatus.AWAITING_RESPONSE;
                case "3":
                    return ApplicationStatus.INTERVIEWING;
                case "4":
                    return ApplicationStatus.IN_PROGRESS;
                case "5":
                    return ApplicationStatus.DENIED;
                default:
                    System.out.println("You must enter a status menu choice.  Choice must be 1-5");
            }
        }
    }
/*
Method: readFilePath
Purpose: Read the text file path that will be used to import job application records
Parameters: None
Return: String
 */
    public String readFilePath(){
        System.out.println("Enter file path to import text file");
        return scanner.nextLine().trim();
    }
}
