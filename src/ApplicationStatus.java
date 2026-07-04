public enum ApplicationStatus {
    APPLIED,
    AWAITING_RESPONSE,
    INTERVIEWING,
    IN_PROGRESS,
    DENIED;

public String getDisplayName(){
    return switch (this) {
        case APPLIED -> "Applied";
        case AWAITING_RESPONSE -> "Awaiting Response";
        case INTERVIEWING -> "Interviewing";
        case IN_PROGRESS -> "In Progress";
        case DENIED -> "Denied";
    };
}
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
