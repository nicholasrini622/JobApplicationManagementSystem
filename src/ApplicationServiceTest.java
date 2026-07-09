import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
class ApplicationServiceTest {
    ApplicationService applicationService;
    JobApplication application;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        applicationService = new ApplicationService(7);
        application = new JobApplication(1,"Winnie Palmer","Data quality informatic", ApplicationStatus.APPLIED,45000,"Orlando,FL",
                WorkStructure.HYBRID, LocalDate.now().minusDays(1), LocalDate.now(),"",false);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Add application test")
    void addApplicationTest() {
        boolean result = applicationService.addApplication(application);
        assertTrue(result, "Error: Job application not added");
        assertEquals(1,applicationService.getAllApplications().size(), "Error: Application record list count should be 1");
        JobApplication testApplication = applicationService.getAllApplications().getFirst();
        assertEquals(1, testApplication.getApplicationID(), "Error: Application ID for first record should be 1");
        assertEquals("Data quality informatic", testApplication.getPosition());
    }
    @org.junit.jupiter.api.Test
    @DisplayName("Add duplicate application test")
    void duplicateApplicationTest(){
        applicationService.addApplication(application);
        JobApplication duplicateApplication = new JobApplication(0,"Winnie Palmer","Data quality informatic",ApplicationStatus.APPLIED,75000,"Orlando",WorkStructure.HYBRID,
                LocalDate.now().minusDays(1),LocalDate.now(),"",false);
        boolean result = applicationService.addApplication(duplicateApplication);
        assertFalse(result,"Error Duplicate applications should not be added");
        assertEquals(1,applicationService.getAllApplications().size(),"Error application record list should only have one record");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Remove application test")
    void removeApplicationTest(){
        applicationService.addApplication(application);
        int applicationID = application.getApplicationID();
        boolean result = applicationService.removeApplication(applicationID);
        assertTrue(result, "Error: Application could not be removed");
        assertEquals(0,applicationService.getAllApplications().size(), "Error: No records should exist after removal");

    }
    @org.junit.jupiter.api.Test
    @DisplayName("Remove application test - ID Doesnt Exist")
    void removeMissingApplication(){
        applicationService.addApplication(application);
        boolean result = applicationService.removeApplication(2);
        assertFalse(result,"Error Non Existing ID ");
        assertEquals(1,applicationService.getAllApplications().size(),"Error application list should still contain one record.");
    }
    @org.junit.jupiter.api.Test
    @DisplayName("Update application test")
    void updateApplicationTest(){
        applicationService.addApplication(application);
        int applicationID = application.getApplicationID();
        JobApplication updatedApplication = new JobApplication(applicationID,"Orlando Health", "Data Analyst I", ApplicationStatus.APPLIED,75000,"Remote",WorkStructure.REMOTE,LocalDate.now().minusDays(1),
                LocalDate.now(),"",false);
        boolean result = applicationService.updateApplication(updatedApplication);
        assertTrue(result,"Error application was not updated");
        assertEquals(1,applicationService.getAllApplications().size(), "Error updating should not add or remove a record");
        JobApplication testApplication = applicationService.findApplicationById(applicationID);
        assertNotNull(testApplication, "Error updated application should still exist");
        assertEquals(75000,testApplication.getSalary(), "Error salary should of updated");
        assertEquals("Remote", testApplication.getLocation());
    }
    @org.junit.jupiter.api.Test
    @DisplayName("Update Non-Existing application test")
    void updateMissingApplication(){
        applicationService.addApplication(application);
        JobApplication missingApplication = new JobApplication(123,"Company","Position",ApplicationStatus.IN_PROGRESS,50000,"Orlando",WorkStructure.REMOTE,
                LocalDate.now().minusDays(1),LocalDate.now(),"",false);
        boolean result = applicationService.updateApplication(missingApplication);
        assertFalse(result,"Error no application to update should return false");
        assertEquals(1,applicationService.getAllApplications().size(),"Error record count should not be updated by a missing application");
    }
}