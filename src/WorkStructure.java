public enum WorkStructure {
    ONSITE,
    REMOTE,
    HYBRID;

    public String getDisplayName(){
        return switch(this){
            case ONSITE -> "Onsite";
            case REMOTE -> "Remote";
            case HYBRID -> "Hybrid";
        };
    }
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
