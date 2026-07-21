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

/**
 * Imports job application records from a text file.  Reads  and converts each row in to valid objects using
 * ApplicationService, count invalid records
 */
public class ImportService {
    private ApplicationService applicationService;
    private int invalidRecordCount;

    /**
     * Create ImportService object using ApplicationService validation and add imported records
     * @param applicationService service object used to add imported applications
     */
    public ImportService(ApplicationService applicationService){
        this.applicationService = applicationService;
        this.invalidRecordCount = 0;
    }

    /**
     * Read a text file and import valid records.  Invalid rows/Duplicates are skipped and count is given
     * @param filePath path to text file for import
     * @return ArrayList of applications that were imported
     */
    public ArrayList<JobApplication> importApplications(String filePath) {
        ArrayList<JobApplication> importList = new ArrayList<>();
        invalidRecordCount = 0;
        if (filePath == null || filePath.isBlank()) {
            System.out.println(" Error in file path.  File path cannot be empty.");
            return importList;
        }
        try {
            File file = new File(filePath.trim());
            Scanner scanner = new Scanner(file);
            int rowNumber = 0;
            while (scanner.hasNextLine()) {
                rowNumber++;
                String line = scanner.nextLine();
                try {
                    JobApplication application = parseApplicationLine(line);
                    if (applicationService.addApplication(application)) {
                        importList.add(application);
                    } else {
                        invalidRecordCount++;
                        System.out.println("Row Skipped: " + rowNumber + " Invalid or duplicate record");
                    }
                } catch (InvalidImportException e) {
                    invalidRecordCount++;
                    System.out.println("Row skipped: " + rowNumber + e.getMessage());
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return importList;
    }

    /**
     * Converts a row from imported text file into a JobApplication object.  A row contains 7 fields company, position, status,
     * salary, location, work structure, and an application date. Optional 8th fields for applicationURL
     * @param line text file row to convert
     * @return created JobApplication record from row in text file
     * @throws InvalidImportException throws exception if rows blank, incomplete, or has an invalid date
     */
            public JobApplication parseApplicationLine (String line) throws InvalidImportException {
                if (!validateFile(line)) {
                    throw new InvalidImportException(" A job application Record row must have 7 fields");
                }
                String[] parts = line.split(",");
                String company = parts[0].trim();
                String position = parts[1].trim();
                ApplicationStatus status = ApplicationStatus.fromString(parts[2].trim());
                double salary;
                try {
                    salary = Double.parseDouble(parts[3].trim());
                    if (salary < 0) {
                        throw new InvalidImportException(" Salary can't be a negative value");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidImportException(" Salary is an invalid number");
                }
                String location = parts[4].trim();
                WorkStructure workStructure = WorkStructure.fromString(parts[5].trim());
                LocalDate applicationDate;
                try {
                    String dateDisplay = parts[6].trim();
                    if (dateDisplay.contains("/")) {
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        applicationDate = LocalDate.parse(dateDisplay, dateFormat);
                    } else {
                        applicationDate = LocalDate.parse(dateDisplay);
                    }
                } catch (DateTimeParseException e) {
                    throw new InvalidImportException(" Invalid date format. Use yyyy/MM/dd");
                }
                String applicationUrl = "";
                if (parts.length >= 8) {
                    applicationUrl = parts[7].trim();
                }
                if (status == null || workStructure == null) {
                    throw new InvalidImportException(" Invalid application status or work structure");
                }
                return new JobApplication(0, company, position, status, salary, location, workStructure, applicationDate, LocalDate.now(), applicationUrl, false);
            }

    /**
     * Checks if an imported row has the correct format.  Valid rows cant be blank and must contain seven fields
     * @param line row from imported text file
     * @return boolean - true if row has all fields, false if blank or incomplete
     */
            public boolean validateFile (String line){
                if (line == null || line.isBlank()) {
                    return false;
                }
                String[] parts = line.split(",");
                return parts.length >= 7;
            }
            public int getInvalidRecordCount () {
                return invalidRecordCount;
            }

    }