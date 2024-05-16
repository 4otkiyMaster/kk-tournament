package com.github.chotkiymaster;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.LinkedList;


class STepGenieJanTest {


    @Test
    void testMinimumField_getRooms() {

        var field = new Field(2, 2);
        var stepUnderTest = new StepGenieJan(field);
            
            var list = stepUnderTest.getRoomsOf2();
            assertThat(list.size(), equalTo(2));
    }

    @Test
    void testMinimumField_squaresInChains() {

        var field = new Field(2, 2);
        var stepUnderTest = new StepGenieJan(field);
            
            var size = stepUnderTest.getChain(field.getSquare(0, 0), field.getSquare(0, 0).getUpperWall(), field.getSquare(0, 0), field.getSquare(0, 0).getUpperWall()).size();
            assertThat("Field has 4 open walls", size, equalTo(4));
    }

    @Test
    void testExactField_Rooms() {

        var field = new Field(5, 5);
        field.getSquare(1, 0).getUpperWall().setClosed(true);
        field.getSquare(2, 0).getRightWall().setClosed(true);
        field.getSquare(0, 1).getUpperWall().setClosed(true);
        field.getSquare(1, 1).getUpperWall().setClosed(true);
        field.getSquare(2, 1).getUpperWall().setClosed(true);
        field.getSquare(2, 1).getRightWall().setClosed(true);
        field.getSquare(3, 1).getUpperWall().setClosed(true);
        field.getSquare(4, 1).getUpperWall().setClosed(true);
        field.getSquare(1, 2).getUpperWall().setClosed(true);
        field.getSquare(2, 2).getRightWall().setClosed(true);
        field.getSquare(1, 3).getRightWall().setClosed(true);
        field.getSquare(2, 3).getRightWall().setClosed(true);
        field.getSquare(3, 3).getUpperWall().setClosed(true);
        field.getSquare(1, 4).getRightWall().setClosed(true);
        var stepUnderTest = new StepGenieJan(field);
            
            var size3 = stepUnderTest.getRoomsOf3().size();
            var size2 = stepUnderTest.getRoomsOf2().size();
            
            assertThat(size3, equalTo(6));
            assertThat(size2, equalTo(4));

    }
}
