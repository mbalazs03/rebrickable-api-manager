package org.rebrickable;

import org.rebrickable.Set;
import java.util.List;

public class BuildableSetResponse {
    private Set legoSet;
    private double completionPercentage;
    private List<String> missingParts;

    public BuildableSetResponse(Set legoSet, double completionPercentage, List<String> missingParts) {
        this.legoSet = legoSet;
        this.completionPercentage = completionPercentage;
        this.missingParts = missingParts;
    }

    public Set getLegoSet() {
        return legoSet;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public List<String> getMissingParts() {
        return missingParts;
    }
}
