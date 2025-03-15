package org.rebrickable.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PartDetailTest {

    @Test
    public void testGettersAndSetters() {
        PartDetail partDetail = new PartDetail();
        partDetail.setPartNum("1234");
        partDetail.setName("Brick 2x4");
        partDetail.setPartImgUrl("https://example.com/brick.jpg");

        assertThat(partDetail.getPartNum()).isEqualTo("1234");
        assertThat(partDetail.getName()).isEqualTo("Brick 2x4");
        assertThat(partDetail.getPartImgUrl()).isEqualTo("https://example.com/brick.jpg");
    }

    @Test
    public void testToString() {
        PartDetail partDetail = new PartDetail();
        partDetail.setPartNum("5678");
        partDetail.setName("Plate 2x2");
        partDetail.setPartImgUrl("https://example.com/plate.jpg");

        String str = partDetail.toString();
        assertThat(str).contains("PartDetail").contains("Plate 2x2");
    }
}
