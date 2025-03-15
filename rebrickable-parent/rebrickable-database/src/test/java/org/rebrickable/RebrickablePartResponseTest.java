package org.rebrickable;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.rebrickable.dto.SetPart;
import java.util.Collections;

public class RebrickablePartResponseTest {

    @Test
    public void testGettersAndSetters() {
        RebrickablePartResponse response = new RebrickablePartResponse();
        response.setCount(1);
        response.setNext("nextPage");
        response.setPrevious("prevPage");

        SetPart setPart = new SetPart();
        setPart.setId(1);
        response.setResults(Collections.singletonList(setPart));

        assertThat(response.getCount()).isEqualTo(1);
        assertThat(response.getNext()).isEqualTo("nextPage");
        assertThat(response.getPrevious()).isEqualTo("prevPage");
        assertThat(response.getResults()).hasSize(1);
    }

    @Test
    public void testToString() {
        RebrickablePartResponse response = new RebrickablePartResponse();
        response.setCount(2);
        String str = response.toString();
        assertThat(str).contains("RebrickablePartResponse");
    }
}
