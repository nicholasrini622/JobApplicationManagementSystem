/*
Nicholas Rini
Software Development I
07/05/2026
Class: ConsoleView
Purpose: Handles the console input and output of the Job Application Management System
Menu Display, input validation, error and confirmation messages, and record displays
 */
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handle console input and output.  Class displays menu, reads user input, collects details,
 * validates input, and displays job application records with proper messages.
 */
public class ConsoleView {
    private ApplicationController controller;
    private Scanner scanner;
    private String statusMessage;

    /**
     * Create ConsoleView with a scanner
     * Controller is set using setController
     */
    public ConsoleView() {
        this.controller = null;
        this.scanner = new Scanner(System.in);
        this.statusMessage = "";
    }

    /**
     * Connect ConsoleView to ApplicationController
     * @param controller controller used by ConsoleView
     * @return boolean - true when controller is assigned.
     */
    public boolean setController(ApplicationController controller) {
        this.controller = controller;
        return true;
    }

    /**
     * Displays the main menu to the user
     * @return boolean - true when menu is displayed
     */
    public boolean displayMenu() {
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

    /**
     * Reads user choice and validates
     * @return selected option integer 1-9
     */
    public int menuChoice() {
        while (true) {
            System.out.println("Choose a menu option");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 9) {
                    return choice;
                }
                System.out.println("Invalid choice, enter a number 1-9.");
            } catch (NumberFormatException e) {
                System.out.println("Input must be a valid menu choice.  Numbers 1-9 only.");
            }
        }

    }

    /**
     * Collects user job application record information.  Validates user provided fields
     * @return JobApplication record based on user input
     */
    public JobApplication collectApplicationInput() {
        String company = "";
        while (company.isBlank()) {
            System.out.println("Enter the company name you applied to.");
            company = scanner.nextLine().trim();
            if (company.isBlank()) {
                System.out.println("Company name must not be blank");
            }
        }
        String position = "";
        while (position.isBlank()) {
            System.out.println("Enter the position you applied for.");
            position = scanner.nextLine().trim();
            if (position.isBlank()) {
                System.out.println("Position must not be blank.");
            }
        }
        ApplicationStatus status = readStatusFilter();
        double salary = -1;
        while (salary < 0) {
            System.out.println("Enter a salary amount or 0 for N/A");
            String input = scanner.nextLine();
            try {
                salary = Double.parseDouble(input);
                if (salary < 0) {
                    System.out.println("Salary must be a positive number >= 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Salary must be only numbers.");
            }
        }
        String location = "";
        while (location.isBlank()) {
            System.out.println("Enter the location of the job you applied for.");
            location = scanner.nextLine().trim();
            if (location.isBlank()) {
                System.out.println("Error: Location can not be left blank.");
            }
        }
        WorkStructure workStructure = null;
        while (workStructure == null) {
            System.out.println("\nWork Structure Options:");
            System.out.println("1. ONSITE");
            System.out.println("2. REMOTE");
            System.out.println("3. HYBRID");
            System.out.println("Choose a menu option number 1-3");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                workStructure = WorkStructure.ONSITE;
            } else if (choice.equals("2")) {
                workStructure = WorkStructure.REMOTE;
            } else if (choice.equals("3")) {
                workStructure = WorkStructure.HYBRID;
            } else {
                System.out.println("Error, Invalid Selection!  Choose a number 1-3");
            }
        }
        LocalDate applicationDate = null;
        while (applicationDate == null) {
            System.out.println("Enter application date yyyy/MM/dd formatting:");
            String input = scanner.nextLine().trim();
            String[] datePart = input.split("/");
            if (datePart.length != 3) {
                System.out.println("Date formatting is yyyy/MM/dd");
                continue;
            }
            try {
                int year = Integer.parseInt(datePart[0]);
                int month = Integer.parseInt(datePart[1]);
                int day = Integer.parseInt(datePart[2]);
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month.  Month can only be 1-12");
                    continue;
                }
                if (day < 1 || day > 31) {
                    System.out.println("Invalid day.  Day can only be 1-31");
                    continue;
                }
                if (year < 1926) {
                    System.out.println("Invalid year.  Application is over 100 years old.");
                    continue;
                }
                applicationDate = LocalDate.of(year, month, day);
                if (applicationDate.isAfter(LocalDate.now())) {
                    System.out.println("application date can not be from the future.");
                    applicationDate = null;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid format.  Date must use yyyy/MM/dd format");
            } catch (DateTimeException e) {
                System.out.println("Day does not exist for that month.");
            }
        }
            System.out.println("Enter optional URL or press enter.");
            String applicationUrl = scanner.nextLine().trim();
            return new JobApplication(0, company, position, status, salary, location, workStructure, applicationDate, LocalDate.now(),
                    applicationUrl, false);
        }

    /**
     * Reads application ID and validates
     * @return Application ID > 0
     */
        public int readApplicationId () {
            while (true) {
                System.out.println("Enter an application ID");
                String input = scanner.nextLine();
                try {
                    int applicationId = Integer.parseInt(input);
                    if (applicationId > 0) {
                        return applicationId;
                    }
                    System.out.println("Application ID cannot be negative or 0.  ID must be > 0");
                } catch (NumberFormatException e) {
                    System.out.println("Application ID must be numerical.  Enter numbers only.");
                }
            }
        }

    /**
     * Read and validate sort by field
     * @return selected field to sort by
     */
        public String readSortField () {
            while (true) {
                System.out.println("\n Application Sorting Options");
                System.out.println("1. Company");
                System.out.println("2. Position");
                System.out.println("3. Salary");
                System.out.println("4. Date");
                System.out.println("Choose a menu number to sort applications by.");
                String choice = scanner.nextLine().trim();
                switch (choice) {
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

    /**
     * Displays job application records
     * @param applications ArrayList of applications the user wants to display
     * @return boolean - true if records are displayed or message is displayed
     */
        public boolean displayAllApplications (ArrayList < JobApplication > applications) {
            System.out.println("\n*** Application Records***");
            if (applications == null || applications.isEmpty()) {
                System.out.println("No application records found");
                return true;
            }
            for (JobApplication application : applications) {
                System.out.println(application);
            }
            return true;
        }

    /**
     * Used to display a message to the user
     * @param message message to display
     * @return boolean - true when message is displayed
     */
        public boolean showMessage (String message){
            statusMessage = message;
            System.out.println(message);
            return true;
        }

    /**
     * Display and error message to the user
     * @param error error message to display
     * @return boolean - true when error message is displayed
     */
        public boolean showError (String error){
            statusMessage = error;
            System.out.println(error);
            return true;
        }

    /**
     * Displays application that need a follow-up
     * @param applications ArrayList of applications needing a follow-up
     * @return boolean - true if applications or message displayed
     */
        public boolean showFollowUp (ArrayList<JobApplication> applications) {
            System.out.println("\n*** Follow-UP ***");
            if (applications == null || applications.isEmpty()) {
                System.out.println("No alerts found");
                return true;
            }
            for (JobApplication application : applications) {
                System.out.println(application);
            }
            return true;
        }

    /**
     * Displays the selected application and prompts for confirmation
     * @param application application to remove
     * @return boolean - true if user confirms removal, false if canceled or nothing is selected
     */
        public boolean deleteConfirmation (JobApplication application){
            if (application == null) {
                System.out.println("No application selected.");
                return false;
            }
            System.out.println("Here is the application you selected.");
            System.out.println(application);
            while (true) {
                System.out.println("Remove this application? yes/no?");
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes") || input.equals("y")) {
                    return true;
                } else if (input.equals("no") || input.equals("n")) {
                    return false;
                }
                System.out.println("Error: Please choose yes or no, or y/n");
            }
        }

    /**
     * Read application status user chooses and validate
     * @return selected application status
     */
        public ApplicationStatus readStatusFilter () {
            while (true) {
                System.out.println("\nApplication status Options");
                System.out.println("1. Applied");
                System.out.println("2. Awaiting Response");
                System.out.println("3. Interviewing");
                System.out.println("4. In progress");
                System.out.println("5. Denied");
                System.out.println("Choose a menu number option to choose a status.");
                String choice = scanner.nextLine().trim();
                switch (choice) {
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

    /**
     * Read file path that is used to import application record
     * @return user entered file path
     */
        public String readFilePath () {
            System.out.println("Enter file path to import text file");
            return scanner.nextLine().trim();
        }

    /**
     * Collect updated values for an application record.  Copy record, select which field to update
     * Return updated application when updating is complete
     * @param currentApplication application record to update
     * @return updated JobApplication record
     */
    public JobApplication UpdatedApplicationInput (JobApplication currentApplication){
            JobApplication updatedApplication = new JobApplication(currentApplication.getApplicationID(), currentApplication.getCompany(),
                    currentApplication.getPosition(), currentApplication.getStatus(), currentApplication.getSalary(), currentApplication.getLocation(),
                    currentApplication.getWorkStructure(), currentApplication.getApplicationDate(), currentApplication.getLastUpdatedDate(),
                    currentApplication.getApplicationUrl(), currentApplication.isFollowUpNeeded());
            boolean isUpdating = true;
            while (isUpdating) {
                System.out.println("\nSelect a field to update");
                System.out.println("1. Company");
                System.out.println("2. Position");
                System.out.println("3. Status");
                System.out.println("4. Salary");
                System.out.println("5. Location");
                System.out.println("6. Work Structure");
                System.out.println("7. Application Date");
                System.out.println("8. Application URL");
                System.out.println("9. Done updating");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        String company = "";
                        while (company.isBlank()) {
                            System.out.println("Enter new company name.");
                            company = scanner.nextLine().trim();
                            if (company.isBlank()) {
                                System.out.println("Updated name cannot be blank");
                            }
                        }
                        updatedApplication.setCompany(company);
                        break;
                    case "2":
                        String position = "";
                        while (position.isBlank()) {
                            System.out.println("Enter updated position.");
                            position = scanner.nextLine().trim();
                            if (position.isBlank()) {
                                System.out.println("Updated fields can not be blank");
                            }
                        }
                        updatedApplication.setPosition(position);
                        break;
                    case "3":
                        updatedApplication.setStatus(readStatusFilter());
                        break;
                    case "4":
                        double salary = -1;
                        while (salary < 0) {
                            System.out.println("Enter updated salary amount greater than or equal to 0");
                            String input = scanner.nextLine().trim();
                            try {
                                salary = Double.parseDouble(input);
                                if (salary < 0) {
                                    System.out.println("Salary must be >= 0");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid salary integer >= 0");
                            }
                        }
                        updatedApplication.setSalary(salary);
                        break;
                    case "5":
                        String location = "";
                        while (location.isBlank()) {
                            System.out.println("Enter an updated location.");
                            location = scanner.nextLine().trim();
                            if (location.isBlank()) {
                                System.out.println("Updated location field cannot be blank");
                            }
                        }
                        updatedApplication.setLocation(location);
                        break;
                    case "6":
                        WorkStructure workStructure = null;
                        while (workStructure == null) {
                            System.out.println("\n Work Structure choices:");
                            System.out.println("1. ONSITE");
                            System.out.println("2. REMOTE");
                            System.out.println("3. HYBRID");
                            String structureChoice = scanner.nextLine().trim();
                            if (structureChoice.equals("1")) {
                                workStructure = WorkStructure.ONSITE;
                            } else if (structureChoice.equals("2")) {
                                workStructure = WorkStructure.REMOTE;
                            } else if (structureChoice.equals("3")) {
                                workStructure = WorkStructure.HYBRID;
                            } else {
                                System.out.println("Invalid choice.  Choose a number 1-3");
                            }
                        }
                        updatedApplication.setWorkStructure(workStructure);
                        break;
                    case "7":
                        LocalDate applicationDate = null;
                        while (applicationDate == null) {
                            System.out.println("Enter an updated application date.  Must use yyyy/MM/dd format");
                            String input = scanner.nextLine().trim();
                            String[] date = input.split("/");
                            if (date.length != 3) {
                                System.out.println("Date format must be yyyy/MM/dd");
                                continue;
                            }
                            try {
                                String yearFormat = date[0];
                                String monthFormat = date[1];
                                String dayFormat = date[2];
                                if (yearFormat.length() != 4) {
                                    System.out.println("Invalid year");
                                    continue;
                                }
                                int year = Integer.parseInt(yearFormat);
                                int month = Integer.parseInt(monthFormat);
                                int day = Integer.parseInt(dayFormat);
                                if (month < 1 || month > 12) {
                                    System.out.println("Invalid month.  Use 1-12 for month.");
                                    continue;
                                }
                                if (day < 1 || day > 31) {
                                    System.out.println("Invalid day. Use 1-31 for day");
                                    continue;
                                }
                                if (year < 1926) {
                                    System.out.println("Year is over 100 years old.  Application can not be 100 years in the past");
                                    continue;
                                }
                                applicationDate = LocalDate.of(year, month, day);
                                if (applicationDate.isAfter(LocalDate.now())) {
                                    System.out.println("Welcome to the future.");
                                    applicationDate = null;
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Invalid date.  Date must be numbers in yyyy/MM/dd format.");
                            } catch (DateTimeException e) {
                                System.out.println("Invalid day.  Day does not exist.");
                            }
                        }
                        updatedApplication.setApplicationDate(applicationDate);
                        break;
                    case "8":
                        System.out.println("Enter updated url or leave blank");
                        String applicationUrl = scanner.nextLine().trim();
                        updatedApplication.setApplicationUrl(applicationUrl);
                        break;
                    case "9":
                        isUpdating = false;
                        break;
                    default:
                        System.out.println("Invalid input.  Please enter a number 1-9");
                        break;
                }
            }
            return updatedApplication;
        }
    }
