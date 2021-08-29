package com.nusky.iress01;

import com.nusky.iress01.model.*;
import com.nusky.iress01.model.enums.Action;
import com.nusky.iress01.model.enums.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToyBotTest {

    Table table = SquareTable.builder().length(5).width(5).build();

    @ParameterizedTest
    @CsvSource(value = {
            "SOUTH,3,3",
            "WEST,0,1",
            "EAST,0,0",
            "NORTH,4,4",
    })
    public void walk_place_valid_pass(String direction, int x, int y) {
        Robot robot = Robot.builder().build();
        ToyBot toyBot = ToyBot.builder().table(table).robot(robot).build();
        Command command = Command.builder().action(Action.PLACE)
                .direction(Direction.valueOf(direction))
                .point(Point.builder()
                        .x(x)
                        .y(y)
                        .build())
                .build();
        toyBot.walk(command);
        assertEquals(x, toyBot.getRobot().getPoint().getX());
        assertEquals(y, toyBot.getRobot().getPoint().getY());
        assertEquals(Direction.valueOf(direction), toyBot.getRobot().getCurrentDirection());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "SOUTH,-1,3",
            "WEST,0,5",
            "EAST,5,5",
            "NORTH,4,-1",
    })
    public void walk_place_invalid_fail(String direction, int x, int y) {
        Robot robot = Robot.builder().build();
        ToyBot toyBot = ToyBot.builder().table(table).robot(robot).build();
        Command command = Command.builder().action(Action.PLACE)
                .direction(Direction.valueOf(direction))
                .point(Point.builder()
                        .x(x)
                        .y(y)
                        .build())
                .build();
        toyBot.walk(command);
        assertNull(toyBot.getRobot().getPoint());
        assertNull(toyBot.getRobot().getCurrentDirection());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "SOUTH,3,3,LEFT,EAST",
            "WEST,0,1,LEFT,SOUTH",
            "WEST,1,1,LEFT,SOUTH",
            "EAST,0,0,LEFT,NORTH",
            "EAST,2,3,LEFT,NORTH",
            "NORTH,4,4,LEFT,WEST",

            "SOUTH,3,3,RIGHT,WEST",
            "WEST,0,1,RIGHT,NORTH",
            "NORTH,4,4,RIGHT,EAST",
            "EAST,0,0,RIGHT,SOUTH",
    })
    public void walk_left_right_valid_pass(String currentDirection, int x, int y, String action, String expectedDirection) {
        Robot robot = Robot.builder().point(Point.builder().x(x).y(y).build()).currentDirection(Direction.valueOf(currentDirection)).build();
        ToyBot toyBot = ToyBot.builder().table(table).robot(robot).build();
        Command command = Command.builder().action(Action.valueOf(action))
                .direction(Direction.valueOf(currentDirection))
                .point(Point.builder()
                        .x(x)
                        .y(y)
                        .build())
                .build();
        toyBot.walk(command);
        assertEquals(x, toyBot.getRobot().getPoint().getX());
        assertEquals(y, toyBot.getRobot().getPoint().getY());
        assertEquals(Direction.valueOf(expectedDirection), toyBot.getRobot().getCurrentDirection());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "SOUTH,0,0,0,0",
            "SOUTH,1,0,1,0",
            "SOUTH,4,0,4,0",
            "SOUTH,0,1,0,0",
            "SOUTH,0,4,0,3",

            "WEST,0,0,0,0",
            "WEST,0,1,0,1",
            "WEST,0,4,0,4",
            "WEST,1,0,0,0",

            "NORTH,0,4,0,4",
            "NORTH,1,4,1,4",
            "NORTH,4,4,4,4",
            "NORTH,2,3,2,4",

            "EAST,4,0,4,0",
            "EAST,4,1,4,1",
            "EAST,4,4,4,4",
            "EAST,3,3,4,3",
    })
    public void walk_move_valid_pass(String currentDirection, int x, int y, int expectedX, int expectedY) {
        Robot robot = Robot.builder().point(Point.builder().x(x).y(y).build()).currentDirection(Direction.valueOf(currentDirection)).build();
        ToyBot toyBot = ToyBot.builder().table(table).robot(robot).build();
        Command command = Command.builder().action(Action.MOVE)
                .direction(Direction.valueOf(currentDirection))
                .point(Point.builder()
                        .x(x)
                        .y(y)
                        .build())
                .build();
        toyBot.walk(command);
        assertEquals(expectedX, toyBot.getRobot().getPoint().getX());
        assertEquals(expectedY, toyBot.getRobot().getPoint().getY());
        assertEquals(Direction.valueOf(currentDirection), toyBot.getRobot().getCurrentDirection());
    }

    @Test
    public void walk_report_dont_change_point() {
        Robot robot = Robot.builder().point(Point.builder().x(3).y(3).build()).currentDirection(Direction.SOUTH).build();
        ToyBot toyBot = ToyBot.builder().table(table).robot(robot).build();
        Command command = Command.builder().action(Action.REPORT)
                .direction(Direction.SOUTH)
                .point(Point.builder()
                        .x(3)
                        .y(3)
                        .build())
                .build();
        toyBot.walk(command);
        assertEquals(3, toyBot.getRobot().getPoint().getX());
        assertEquals(3, toyBot.getRobot().getPoint().getY());
        assertEquals(Direction.SOUTH, toyBot.getRobot().getCurrentDirection());
    }
}