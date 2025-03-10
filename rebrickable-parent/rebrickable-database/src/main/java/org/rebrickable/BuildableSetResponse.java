package org.rebrickable;

import java.util.List;

public class BuildableSetResponse {
    private Set set;
    private double completionPercentage;
    private List<String> missingParts;

    public BuildableSetResponse(Set set, double completionPercentage, List<String> missingParts) {
        this.set = set;
        this.completionPercentage = completionPercentage;
        this.missingParts = missingParts;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<String> getMissingParts() {
        return missingParts;
    }

    public void setMissingParts(List<String> missingParts) {
        this.missingParts = missingParts;
    }
}