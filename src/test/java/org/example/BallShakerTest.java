package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BallShakerTest {

    private final BallShaker ballShaker = new BallShaker();

    @Test
    @DisplayName("Shake the ball returns valid answer")
    void testCase01(){
        var possibleAnswers = EnumSet.allOf(Answer.class);
        var ballAnswer = ballShaker.shakeTheBall();
        assertTrue(possibleAnswers.contains(ballAnswer));
    }
}