package org.rebrickable;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;

public class RebrickableResponseTest {

    @Test
    public void testGettersAndSetters() {
        RebrickableResponse response = new RebrickableResponse();
        response.setCount(5);
        response.setNext("nextPage");
        response.setPrevious("prevPage");

        Set legoSet = new Set();
        legoSet.setSetNum("75211-1");
        legoSet.setName("Millennium Falcon");

        response.setResults(Collections.singletonList(legoSet));

        assertThat(response.getCount()).isEqualTo(5);
        assertThat(response.getNext()).isEqualTo("nextPage");
        assertThat(response.getPrevious()).isEqualTo("prevPage");
        assertThat(response.getResults()).hasSize(1);
        assertThat(response.getResults().getFirst().getName()).isEqualTo("Millennium Falcon");
    }

    @Test
    public void testToString() {
        RebrickableResponse response = new RebrickableResponse();
        response.setCount(3);
        String str = response.toString();
        assertThat(str).contains("RebrickableResponse");
    }
}