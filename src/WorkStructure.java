/*
Nicholas Rini
Software Development I
07/05/2026.
 */

/**
 * All possible workStructure application options
 * WorkStructure options are consistent throughout.  Contains methods to display workStructure
 */
public enum WorkStructure {
    ONSITE,
    REMOTE,
    HYBRID;

    /**
     * Get display name for work structure choices
     * @return proper display name for work structure
     */
    public String getDisplayName(){
        return switch(this){
            case ONSITE -> "Onsite";
            case REMOTE -> "Remote";
            case HYBRID -> "Hybrid";
        };
    }

    /**
     * Converts work structure to proper enum choice.  Converts spaces and hypens to underscores
     * @param workStructureChoice work structure represented as text
     * @return matching choice for WorkStructure or null if none found
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
