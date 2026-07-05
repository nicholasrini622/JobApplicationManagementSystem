/*
Nicholas Rini
Software Development I
07/05/2026
ApplicationStatus
This class will serve as the option choices for Application Status for the user.  The enum class shows the allowed status responses.
Application Status reponses include APPLIED, AWAITING_RESPONSE,INTERVIEWING,IN_PROGRESS,DENIED.
 */
public enum ApplicationStatus {
    APPLIED,
    AWAITING_RESPONSE,
    INTERVIEWING,
    IN_PROGRESS,
    DENIED;
/*
Method: getDisplayName()
Purpose: Displays enum values as a more readable name
Parameters: None
Return: String
 */
public String getDisplayName(){
    return switch (this) {
        case APPLIED -> "Applied";
        case AWAITING_RESPONSE -> "Awaiting Response";
        case INTERVIEWING -> "Interviewing";
        case IN_PROGRESS -> "In Progress";
        case DENIED -> "Denied";
    };
}
/*
Method: fromString(String statusChoice)
Purpose: Converts user input or text file input into an Enum ApplicationStatus value
Parameter: String statusChoice
Return: ApplicationStatus
 */
public static ApplicationStatus fromString(String statusChoice){
    if(statusChoice== null){
        return null;
    }
    String cleanText = statusChoice.trim().toUpperCase().replace(" ","_").replace("-","_");
    for(ApplicationStatus status : ApplicationStatus.values()) {
        if (status.name().equals(cleanText)) {
            return status;
        }
    }
    return null;
}

        }
