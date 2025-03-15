package org.rebrickable;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;

public class BuildableSetResponseTest {

    @Test
    public void testGettersAndSetters() {
        Set legoSet = new Set();
        legoSet.setSetNum("75211-1");
        legoSet.setName("Millennium Falcon");

        List<String> missingParts = Arrays.asList("Brick (2 missing)", "Plate (1 missing)");
        BuildableSetResponse response = new BuildableSetResponse(legoSet, 75.0, missingParts);

        assertThat(response.getSet().getName()).isEqualTo("Millennium Falcon");
        assertThat(response.getCompletionPercentage()).isEqualTo(75.0);
        assertThat(response.getMissingParts()).contains("Brick (2 missing)");
    }
}
