package org.rebrickable.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ColorDetailTest {

    @Test
    public void testGettersAndSetters() {
        ColorDetail colorDetail = new ColorDetail();
        colorDetail.setId(1);
        colorDetail.setName("Red");
        colorDetail.setRgb("#FF0000");
        colorDetail.setTrans(false);

        assertThat(colorDetail.getId()).isEqualTo(1);
        assertThat(colorDetail.getName()).isEqualTo("Red");
        assertThat(colorDetail.getRgb()).isEqualTo("#FF0000");
        assertThat(colorDetail.isTrans()).isFalse();
    }

    @Test
    public void testToString() {
        ColorDetail colorDetail = new ColorDetail();
        colorDetail.setId(2);
        colorDetail.setName("Blue");
        colorDetail.setRgb("#0000FF");
        colorDetail.setTrans(true);

        String str = colorDetail.toString();
        assertThat(str).contains("ColorDetail").contains("Blue");
    }
}