/*
Nicholas Rini
Software Development I
07/05/2026
WorkStructure
This class will serve as the option choices for WorkStructure for the user.  The Enum shows the allowed WorkStructure options.
WorkStructure options include ONSITE,REMOTE,HYBRID.
 */
public enum WorkStructure {
    ONSITE,
    REMOTE,
    HYBRID;
    /*
    Method: getDisplayName()
    Purpose: Displays enum values as a more readable name
    Parameters: None
    Return: String
     */
    public String getDisplayName(){
        return switch(this){
            case ONSITE -> "Onsite";
            case REMOTE -> "Remote";
            case HYBRID -> "Hybrid";
        };
    }
    /*
Method: fromString(String statusChoice)
Purpose: Converts user input or text file input into an Enum WorkStructure value
Parameter: String workStructureChoice
Return: WorkStructure
 */
    public static WorkStructure fromString(String workStructureChoice){
        if(workStructureChoice== null){
            return null;
        }
        String cleanText = workStructureChoice.trim().toUpperCase().replace(" ","_")
                .replace("-","_");
        for(WorkStructure workstructure : WorkStructure.values()) {
            if (workstructure.name().equals(cleanText)) {
                return workstructure;
            }
        }
        return null;
    }
}
