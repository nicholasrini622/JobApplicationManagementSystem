/*
Nicholas Rini
Software Development I
07/05/2026
*/

/**
 * All possible application status options
 * Status options are consistent throughout job application management system
 */
public enum ApplicationStatus {
    APPLIED,
    AWAITING_RESPONSE,
    INTERVIEWING,
    IN_PROGRESS,
    DENIED;

    /**
     * Get a formatted status name for job application status
     * @return formatted status
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

    /**
     * Converts status in to its proper ApplicationStatus
     * Converts spaces and hyphens to underscore before checking for a match
     * @param statusChoice application status represented as test
     * @return a matching ApplicationStatus, or null if nothing is found
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
