package com.xmart.banana;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
        Banana banana = new Banana(map, "5,3");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |O  |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test()
    public void bananaCanMove() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "5,3");
        banana.move("2");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |  O|").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test()
    public void bananaCanTurnRightAndLeft() throws Exception {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "5,3");
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
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |O  |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test(expected = FenceExeption.class)
    public void bananaCanTMoveThrowFence() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.left();
        banana.move("3");
    }

    @Test(expected = OutOfBoundsExeption.class)
    public void bananaCanTMoveOutOfMap() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").build();
        Banana banana = new Banana(map, "4,3");
        banana.move("3");
    }

    /**
     * a daemon is represented by X
     * Whenever Banana moves or turn right or left, daemon move foreword
     * The daemon starts moving toward east
     * If daemon is facing a fence, he automatically turns right, he keeps turning right until he can move foreword
     * The daemon sees Banana when they are in the same line or same column and there is no fence between them
     * When the daemon sees Banana, he starts following him
     * He goes to the position where he saw Banana and starts following his path
     * If the daemon catches Banana, Banana is dead
     * When Banana dies, he cant move anymore
     */
    @Test()
    public void daemonTurnRightWhenHeCantMoveForward() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").deamon("1,1").build();
        Banana banana = new Banana(map, "5,3");
        banana.move("2");
        banana.left();
        banana.move("2");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |  O|").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("|X|   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }

    @Test(expected = DeadBananaException.class)
    public void daemonFollowsBananaAndKillsHim() {
        Map map = new MapBuilder("5,5").horizontalFence("1,2-4").verticalFence("3-5,2").deamon("1,1-2,3").build();
        Banana banana = new Banana(map, "5,3");
        String expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("|X--- |").append(LINE_SEPARATOR)
                .append("|  X  |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |O  |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.move("2"); // second daemon sees banana he turn right then forward
        expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|X    |").append(LINE_SEPARATOR)
                .append("| |X  |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| |  O|").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.left();
        banana.move("1"); // second daemon sees banana again and turn left
        expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("|X|X O|").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.left();
        banana.move("1");
        expectedMap = new StringBuilder()
                .append("-------").append(LINE_SEPARATOR)
                .append("| --- |").append(LINE_SEPARATOR)
                .append("|     |").append(LINE_SEPARATOR)
                .append("| |   |").append(LINE_SEPARATOR)
                .append("| | X |").append(LINE_SEPARATOR)
                .append("|X|   |").append(LINE_SEPARATOR)
                .append("-------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.move("1");
    }

    /**
     * When Banana eats an enhancer(*), he kills daemons when he meets them for the next 10 movements(turning right or left are counted)
     * When Banana eats an freezer(F), daemons can't move for the next five movements
     */
    @Test
    public void enhancerAndFreezer() {
        Map map = new MapBuilder("7,7").freezer("5-1").enhancer("4-1").deamon("1,1").build();
        Banana banana = new Banana(map, "7,1");
        String expectedMap = new StringBuilder()
                .append("---------").append(LINE_SEPARATOR)
                .append("|X      |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|*      |").append(LINE_SEPARATOR)
                .append("|F      |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|O      |").append(LINE_SEPARATOR)
                .append("---------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.left();
        banana.move("2");
        expectedMap = new StringBuilder()
                .append("---------").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|X      |").append(LINE_SEPARATOR)
                .append("|*      |").append(LINE_SEPARATOR)
                .append("|O      |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("---------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.move("1");// daemon cant move because he's frozen
        expectedMap = new StringBuilder()
                .append("---------").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|X      |").append(LINE_SEPARATOR)
                .append("|O      |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("---------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
        banana.move("1");
        expectedMap = new StringBuilder()
                .append("---------").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|O       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("|       |").append(LINE_SEPARATOR)
                .append("---------")
                .toString();
        assertThat(map.draw(), equalTo(expectedMap));
    }


}
