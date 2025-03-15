package org.rebrickable.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SetPartTest {

    @Test
    public void testGettersAndSetters() {
        SetPart setPart = getSetPart();

        assertThat(setPart.getId()).isEqualTo(1);
        assertThat(setPart.getPart().getName()).isEqualTo("Brick 2x4");
        assertThat(setPart.getColor().getName()).isEqualTo("Red");
        assertThat(setPart.getSetNum()).isEqualTo("75211-1");
        assertThat(setPart.getQuantity()).isEqualTo(4);
        assertThat(setPart.isSpare()).isFalse();
        assertThat(setPart.getElementId()).isEqualTo("elem1");
        assertThat(setPart.getNumSets()).isEqualTo(1);
    }

    private static SetPart getSetPart() {
        SetPart setPart = new SetPart();
        setPart.setId(1);

        PartDetail partDetail = new PartDetail();
        partDetail.setPartNum("3001");
        partDetail.setName("Brick 2x4");
        partDetail.setPartImgUrl("https://example.com/brick.jpg");

        ColorDetail colorDetail = new ColorDetail();
        colorDetail.setId(1);
        colorDetail.setName("Red");
        colorDetail.setRgb("#FF0000");
        colorDetail.setTrans(false);

        setPart.setPart(partDetail);
        setPart.setColor(colorDetail);
        setPart.setSetNum("75211-1");
        setPart.setQuantity(4);
        setPart.setSpare(false);
        setPart.setElementId("elem1");
        setPart.setNumSets(1);
        return setPart;
    }

    @Test
    public void testToString() {
        SetPart setPart = new SetPart();
        setPart.setId(2);
        // Minimal field setup for toString check
        String str = setPart.toString();
        assertThat(str).contains("SetPart");
    }
}
