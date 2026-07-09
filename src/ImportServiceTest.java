import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ImportServiceTest {
    ApplicationService applicationService;
    ImportService importService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(7);
        importService = new ImportService(applicationService);
    }

    @Test
    @DisplayName("Import File test")
    void importApplicationTest(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("jobApplicationTest.txt");
        DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String testDate = LocalDate.now().minusDays(1).format(dateFormatting);
        String testFileContent = "HCA,HealthCareAnalytics,APPLIED,75000,Orlando FL,HYBRID," + testDate + ",test.com";
        Files.writeString(testFile,testFileContent);
        ArrayList<JobApplication> importedApplications = importService.importApplications(testFile.toString());
        assertEquals(1,importedApplications.size(), "Error an application should be imported");
        assertEquals(1,applicationService.getAllApplications().size(),"Error application service should contain a record");
        assertEquals(0,importService.getInvalidRecordCount(),"Error: valid records should not be skipped");

        JobApplication testImportApplication = applicationService.getAllApplications().getFirst();
        assertEquals("HCA",testImportApplication.getCompany(),"Error Company name should match");
        assertEquals("HealthCareAnalytics",testImportApplication.getPosition(),"Error position should match");
    }
    @org.junit.jupiter.api.Test
    @DisplayName("Invalid row Import test")
    void invalidRowTest(@TempDir Path tempDir) throws IOException{
        Path testFile = tempDir.resolve("invalidJobApplicationTest.txt");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String testDate = LocalDate.now().minusDays(1).format(dateFormat);
        String validRecord = "HCA,HealthCareAnalytics,APPLIED,75000,Orlando FL,HYBRID," + testDate + ",test.com";
        String invalidRow = "Orlando Health,Data Analyst,AppleSauce,Jimmy,Orlando FL,HYBRID," + testDate + ",test.com";
        String testFileContent = validRecord + "\n" + invalidRow;
        Files.writeString(testFile,testFileContent);
        ArrayList<JobApplication> importedApplicationFile = importService.importApplications(testFile.toString());

        assertEquals(1,importedApplicationFile.size(),"Error only 1 valid row should be added");
        assertEquals(1, applicationService.getAllApplications().size(),"Error application service should only have 1 record");
        assertEquals(1,importService.getInvalidRecordCount(),"Error one row should be skipped");
    }

}