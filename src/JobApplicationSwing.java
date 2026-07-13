import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

public class JobApplicationSwing {
    private ApplicationService applicationService;
    private JTable applicationTable;
    private DefaultTableModel tableModel;
    private JLabel confirmationLabel;
    private JLabel followUpLabel;
    private ImportService importService;
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            JobApplicationSwing app = new JobApplicationSwing();
            app.createGUI();
        });

    }
    private void createGUI(){
        applicationService = new ApplicationService(7);
        importService = new ImportService(applicationService);
        JFrame frame = new JFrame("Job Application Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(createTopPanel(),BorderLayout.NORTH);
        frame.add(createTable(),BorderLayout.CENTER);
        frame.add(bottomPanel(),BorderLayout.SOUTH);

        refreshTable();
        frame.setVisible(true);

    }
    private void addTestApplication(){
        JobApplication testApplication = new JobApplication(
                1,"Orlando Health","Data quality",ApplicationStatus.APPLIED,75000,"Orlando",WorkStructure.HYBRID, LocalDate.now().minusDays(1),LocalDate.now(),
                "",false
        );
        applicationService.addApplication(testApplication);
    }
    public void refreshTable(){
        tableModel.setRowCount(0);
        for(JobApplication application : applicationService.getAllApplications()){
            Object[] rowData = { application.getApplicationID(),application.getCompany(),application.getPosition(),application.getStatus().getDisplayName(),
                    application.getSalary(),application.getLocation(),application.getWorkStructure().getDisplayName(),application.getApplicationDate(),
            application.getLastUpdatedDate(),application.getApplicationUrl()
            };
            tableModel.addRow(rowData);
        }
        applicationTable.clearSelection();
    }
    public void refreshTable(ArrayList<JobApplication> displayApplications){
        tableModel.setRowCount(0);
        for(JobApplication application : displayApplications){
            Object[] rowData = { application.getApplicationID(),application.getCompany(),application.getPosition(),application.getStatus().getDisplayName(),
                    application.getSalary(),application.getLocation(),application.getWorkStructure().getDisplayName(),application.getApplicationDate(),
                    application.getLastUpdatedDate(),application.getApplicationUrl()
            };
            tableModel.addRow(rowData);
        }
        applicationTable.clearSelection();
    }
    public JScrollPane createTable(){
        String[] columns = {
                "ID","Company","Position","Status","Salary","Location","Work Structure","Application Date","Recently Updated Date"
                ,"URL"
        };
        tableModel = new DefaultTableModel(columns,0){
            @Override public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        applicationTable = new JTable(tableModel);
        return new JScrollPane(applicationTable);
    }
    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Job Application Record Management System");
        titleLabel.setFont(new Font("Verdana",Font.BOLD,20));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> statusFilter = new JComboBox<>();
        statusFilter.addItem("Statuses");
        statusFilter.addItem("Applied");
        statusFilter.addItem("Awaiting Response");
        statusFilter.addItem("Interviewing");
        statusFilter.addItem("In Progress");
        statusFilter.addItem("Denied");

        JComboBox<String> sortBy = new JComboBox<>();
        sortBy.addItem("Company");
        sortBy.addItem("Position");
        sortBy.addItem("Salary");
        sortBy.addItem("Date");

        JButton apply = new JButton("Apply Changes");
        apply.addActionListener(event -> {filterAndSort(statusFilter,sortBy);});
        JButton defaultViewButton = new JButton("Reset to default");
        defaultViewButton.addActionListener(event -> {statusFilter.setSelectedItem("Statuses");
        sortBy.setSelectedItem("Company");
        refreshTable();
        confirmationLabel.setText("View reset to default.");});
        filterPanel.add(new JLabel("Filter by Status"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Sort by"));
        filterPanel.add(sortBy);
        filterPanel.add(apply);
        filterPanel.add(defaultViewButton);
        topPanel.add(titleLabel,BorderLayout.NORTH);
        topPanel.add(filterPanel,BorderLayout.SOUTH);
        return topPanel;

    }
    private JPanel bottomPanel(){
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1,5));
        JButton addButton = new JButton("Add Application");
        JButton importButton = new JButton("Import File");
        JButton updateButton = new JButton("Update application");
        JButton removeButton = new JButton("Remove application");
        JButton followUpButton = new JButton("View Follow-Up Alerts");
        buttonPanel.add(addButton);
        buttonPanel.add(importButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(followUpButton);
        addButton.addActionListener(event ->{openAddApplicationForm();
        });
        importButton.addActionListener(event -> importApplicationFile());
        updateButton.addActionListener(event ->updateSelectedApplication());
        removeButton.addActionListener(event ->removeSelectedApplication());
        followUpButton.addActionListener(event ->{followUpLabel.setText("Follow Up Alerts Clicked");});
        confirmationLabel = new JLabel("Confirmation Message Here");
        followUpLabel = new JLabel("Follow Up Alert here");
        JPanel messagePanel = new JPanel(new GridLayout(1,2));
        messagePanel.add(confirmationLabel);
        messagePanel.add(followUpLabel);
        bottomPanel.add(buttonPanel,BorderLayout.NORTH);
        bottomPanel.add(messagePanel,BorderLayout.SOUTH);
        return bottomPanel;
    }
    private void openAddApplicationForm(){
        JTextField companyField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField applicationDateField = new JTextField();
        JTextField urlField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>();
        statusBox.addItem("Applied");
        statusBox.addItem("Awaiting Response");
        statusBox.addItem("Interviewing");
        statusBox.addItem("In Progress");
        statusBox.addItem("Denied");
        JComboBox<String> workStructureBox = new JComboBox<>();
        workStructureBox.addItem("Onsite");
        workStructureBox.addItem("Remote");
        workStructureBox.addItem("Hybrid");
        JPanel formPanel = new JPanel(new GridLayout(0,2,10,10));
        formPanel.add(new JLabel("Company"));
        formPanel.add(companyField);
        formPanel.add(new JLabel("Position"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Status: "));
        formPanel.add(statusBox);
        formPanel.add(new JLabel("Salary: "));
        formPanel.add(salaryField);
        formPanel.add(new JLabel("Location"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Work Structure: "));
        formPanel.add(workStructureBox);
        formPanel.add(new JLabel("Application Date yyyy/MM/dd"));
        formPanel.add(applicationDateField);
        formPanel.add(new JLabel("Optional Application URL"));
        formPanel.add(urlField);
        int result = JOptionPane.showConfirmDialog(applicationTable,formPanel,"Add application",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            try{
                String company = companyField.getText().trim();
                String position = positionField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String location = locationField.getText().trim();
                String applicationDateText = applicationDateField.getText().trim();
                String applicationUrl = urlField.getText().trim();
                double salary = Double.parseDouble(salaryText);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate applicationDate = LocalDate.parse(applicationDateText,dateFormatter);
                ApplicationStatus status = ApplicationStatus.fromString(statusBox.getSelectedItem().toString());
                WorkStructure workStructure = WorkStructure.fromString(workStructureBox.getSelectedItem().toString());
                JobApplication newApplication = new JobApplication(0,company,position,status,salary,location,workStructure,applicationDate,LocalDate.now(),
                        applicationUrl,false);
                boolean addedConfirm = applicationService.addApplication(newApplication);

                if(addedConfirm){
                    refreshTable();
                    confirmationLabel.setText("Application added.");
                }
                else{
                    confirmationLabel.setText("Application not added.");
                    JOptionPane.showMessageDialog(applicationTable,"Application not added.  Application must be unique.   Check for blank fields, negative salary, " +
                            "and correct date formatting.","Add Failed",JOptionPane.ERROR_MESSAGE);

                }
            }
            catch(DateTimeParseException e){
                confirmationLabel.setText("Application was not added.");
                JOptionPane.showMessageDialog(applicationTable,"Salary must be a positive valid number","Invalid Summary",JOptionPane.ERROR_MESSAGE);
            }
            catch(NumberFormatException e){
                confirmationLabel.setText("Application not added.");
                JOptionPane.showMessageDialog(applicationTable,"Salary must be a valid number.","Invalid Salary",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private void removeSelectedApplication(){
        int selectedRow = applicationTable.getSelectedRow();
        if(selectedRow == -1){
            confirmationLabel.setText("No application selected.");
            JOptionPane.showMessageDialog(applicationTable,"Please select an application before attempting to remove.","No application selected",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = applicationTable.convertRowIndexToModel(selectedRow);
        int applicationID = (int) tableModel.getValueAt(modelRow,0);
        String company = tableModel.getValueAt(modelRow,1).toString();
        String position = tableModel.getValueAt(modelRow,2).toString();
        int confirmation = JOptionPane.showConfirmDialog(applicationTable,"Confirm you'd like to remove the selected application. \n" +company + " - " + position, "Confirm Removal",
                JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            boolean removal = applicationService.removeApplication(applicationID);
            if(removal){
                refreshTable();
                confirmationLabel.setText("Application Removed");
            }
            else{
                confirmationLabel.setText("Application was not removed");
                JOptionPane.showMessageDialog(applicationTable,"Application was not removed","Application removed failed",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void updateSelectedApplication(){
        int selectedRow = applicationTable.getSelectedRow();
        if(selectedRow == -1){
            confirmationLabel.setText("No application selected to update");
            JOptionPane.showMessageDialog(applicationTable,"Select an application before clicking update","No application selected",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = applicationTable.convertRowIndexToModel(selectedRow);
        int applicationID = (int) tableModel.getValueAt(modelRow,0);
        JobApplication currentApplication = applicationService.findApplicationById(applicationID);
        if(currentApplication == null){
            confirmationLabel.setText("Application not found");
            JOptionPane.showMessageDialog(applicationTable,"Application not found","Update failed",JOptionPane.ERROR_MESSAGE);
            return;
        }
        JTextField companyField = new JTextField(currentApplication.getCompany());
        JTextField positionField = new JTextField(currentApplication.getPosition());
        JTextField salaryField = new JTextField(String.valueOf(currentApplication.getSalary()));
        JTextField locationField = new JTextField(currentApplication.getLocation());
        JTextField applicationDateField = new JTextField(currentApplication.getApplicationDate().toString().replace("-","/"));
        JTextField urlField = new JTextField(currentApplication.getApplicationUrl());
        JComboBox<String> statusBox = new JComboBox<>();
        statusBox.addItem("Applied");
        statusBox.addItem("Awaiting response");
        statusBox.addItem("Interviewing");
        statusBox.addItem("In Progress");
        statusBox.addItem("Denied");
        statusBox.setSelectedItem(currentApplication.getStatus().getDisplayName());

        JComboBox<String> workStructureBox = new JComboBox<>();
        workStructureBox.addItem("Onsite");
        workStructureBox.addItem("Remote");
        workStructureBox.addItem("Hybrid");
        workStructureBox.setSelectedItem(currentApplication.getWorkStructure().getDisplayName());
        JPanel formPanel = new JPanel(new GridLayout(0,2,10,10));
        formPanel.add(new JLabel("Company"));
        formPanel.add(companyField);
        formPanel.add(new JLabel("Position"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Status: "));
        formPanel.add(statusBox);
        formPanel.add(new JLabel("Salary: "));
        formPanel.add(salaryField);
        formPanel.add(new JLabel("Location"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Work Structure: "));
        formPanel.add(workStructureBox);
        formPanel.add(new JLabel("Application Date yyyy/MM/dd"));
        formPanel.add(applicationDateField);
        formPanel.add(new JLabel("Optional Application URL"));
        formPanel.add(urlField);
        int result = JOptionPane.showConfirmDialog(applicationTable,formPanel,"Update Application",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            try{
                String company = companyField.getText().trim();
                String position = positionField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String location = locationField.getText().trim();
                String applicationDateText = applicationDateField.getText().trim();
                String applicationUrl = urlField.getText().trim();
                double salary = Double.parseDouble(salaryText);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate applicationDate = LocalDate.parse(applicationDateText,dateFormatter);
                ApplicationStatus status = ApplicationStatus.fromString(statusBox.getSelectedItem().toString());
                WorkStructure workStructure = WorkStructure.fromString(workStructureBox.getSelectedItem().toString());
                JobApplication updatedApplication = new JobApplication(applicationID,company,position,status,salary,location,workStructure,applicationDate,
                        LocalDate.now(),applicationUrl,false);
                boolean updated = applicationService.updateApplication(updatedApplication);
                if(updated){
                    refreshTable();
                    confirmationLabel.setText("Application Updated");
                }
                else{
                    confirmationLabel.setText("Application Could not be updated");
                    JOptionPane.showMessageDialog(applicationTable,"Application was not updated, application must remain unique, no blank fields, no negative salary, or future dates","Update Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NumberFormatException e){
                confirmationLabel.setText("Application could not be updated");
                JOptionPane.showMessageDialog(applicationTable,"Salary must be a valid positive number","Invalid salary",JOptionPane.ERROR_MESSAGE);
            }
            catch(DateTimeParseException e){
                confirmationLabel.setText("Application could not be updated");
                JOptionPane.showMessageDialog(applicationTable,"Application date uses yyyy/MM/dd formatting","Invalid Date",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private void importApplicationFile(){
        JFileChooser filePicker = new JFileChooser();
        int result = filePicker.showOpenDialog(applicationTable);
        if(result == JFileChooser.APPROVE_OPTION){
            File fileChoice = filePicker.getSelectedFile();
            importService.importApplications(fileChoice.getAbsolutePath());
            refreshTable();
            int skippedRecord = importService.getInvalidRecordCount();
            confirmationLabel.setText("Import successful. Records skipped - " + skippedRecord);
            JOptionPane.showMessageDialog(applicationTable,"Import successful.  Records skipped - " + skippedRecord,"Import successful",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            confirmationLabel.setText("Import was cancelled");
        }
    }
    private void filterAndSort(JComboBox<String> statusFilter, JComboBox<String> sortField){
        String statusChoice = statusFilter.getSelectedItem().toString();
        String sortChoice = sortField.getSelectedItem().toString();
        ArrayList<JobApplication> displayApplications;
        if (statusChoice.equals("Statuses")) {
            displayApplications = new ArrayList<>(applicationService.getAllApplications());
        } else {
            ApplicationStatus status = ApplicationStatus.fromString(statusChoice);
            displayApplications = applicationService.filterByStatus(status);
        }
        switch (sortChoice) {
            case "Position" -> displayApplications.sort(Comparator.comparing(JobApplication::getPosition));
            case "Salary" -> displayApplications.sort(Comparator.comparing(JobApplication::getSalary));
            case "Date" -> displayApplications.sort(Comparator.comparing(JobApplication::getApplicationDate));
            default -> displayApplications.sort(Comparator.comparing(JobApplication::getCompany));
        }
        refreshTable(displayApplications);
        confirmationLabel.setText("Displaying " + displayApplications.size() + "records. Filter: " + statusChoice + ", Sort: " + sortChoice);
    }
}
