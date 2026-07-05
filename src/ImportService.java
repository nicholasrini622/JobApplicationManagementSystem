/*
Nicholas Rini
Software Development I
07/05/2026
Class: ImportService
Purpose: Handle importing job application records from a text file.  Adds valid records to current list
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class ImportService {
    private ApplicationService applicationService;


    private int invalidRecordCount;
    public ImportService(ApplicationService applicationService){
        this.applicationService = applicationService;
        this.invalidRecordCount = 0;
    }
/*
Method: importApplications
Purpose: Read text file, parse each row, import any valid job application records
Parameters: String filePath
Return: ArrayList<JobApplication> of imported applications
 */
    public ArrayList<JobApplication> importApplications(String filePath) {
        ArrayList<JobApplication> importList = new ArrayList<>();
        invalidRecordCount = 0;
        if (filePath == null || filePath.isBlank()) {
            System.out.println("Error in file path.  File path cannot be empty.");
            return importList;
        }
        try {
            File file = new File(filePath.trim());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                JobApplication application = parseApplicationLine(line);
                if (application != null && applicationService.addApplication(application)) {
                    importList.add(application);
                } else {
                    invalidRecordCount++;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not located.");
        }
        return importList;
    }
/*
Method: parseApplicationLine
Purpose: Convert a row from an imported text file in to a JobApplication object
Parameters: String line
Return: JobApplication
 */
    public JobApplication parseApplicationLine(String line){
        if(!validateFile(line)){
            System.out.println("Invalid format!  A row must have 7 fields.");
            return null;
        }
        String[] parts = line.split(",");
        String company = parts[0].trim();
        String position = parts[1].trim();
        ApplicationStatus status = ApplicationStatus.fromString(parts[2].trim());
        double salary;
        try{
            salary = Double.parseDouble(parts[3].trim());
            if(salary< 0){
                System.out.println("Salary cannot be negative.  Salary should be >=0");
                return null;
            }
        }
        catch(NumberFormatException e){
            System.out.println("Invalid salary.");
            return null;
        }
        String location = parts[4].trim();
        WorkStructure workStructure = WorkStructure.fromString(parts[5].trim());
        LocalDate applicationDate;
        try {
            String dateDisplay = parts[6].trim();
            if (dateDisplay.contains("/")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                applicationDate = LocalDate.parse(dateDisplay, dateFormat);
            }
            else {
                applicationDate = LocalDate.parse(dateDisplay);
            }
        }catch (DateTimeParseException e) {
            System.out.println("Invalid date formatting.  Use yyyy/MM/dd.");
            return null;
        }
        String applicationUrl = "";
        if(parts.length >= 8){
            applicationUrl = parts[7].trim();
        }
        if(status == null || workStructure == null){
            System.out.println("Invalid status or workStructure");
            return null;
        }
        return new JobApplication(0,company,position,status,salary,location,workStructure,applicationDate,LocalDate.now(),applicationUrl,false);
        }

/*
Method: validateFile
Purpose: Check if a row from imported file has the required format
Parameters: String line
Return: boolean - true if a row has 7 fields, false if the row is blank or incomplete
 */
    public boolean validateFile(String line){
        if(line == null || line.isBlank()){
            return false;
        }
        String[] parts = line.split(",");
        return parts.length >= 7;
    }
    public int getInvalidRecordCount() {
        return invalidRecordCount;
    }
        }