package com.xmart.banana;

import static com.xmart.banana.Map.LINE_SEPARATOR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class BananaTest {
    @Test
    public void buildMap() {
        Map map = new MapBuilder("5,5").build();
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test
    public void mapCanHaveFences() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test
    public void bananaCanStartAnyWhereInMap() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |O  |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test()
    public void bananaCanMove() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.move("2");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |  O|").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test()
    public void bananaCanTurnRightAndLeft() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.move("2");
        banana.left();
        banana.move("1");
        banana.left();
        banana.move("2");
        banana.right();
        banana.move("1");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|  O  |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test(expected = FenceExeption.class)
    @Ignore
    public void bananaCanTMoveThrowFence() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.left();
        banana.move("3");
    }
    @Test(expected = OutOfBoundsExeption.class)
    @Ignore
    public void bananaCanTMoveOutOfMap() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.move("3");
    }

}
