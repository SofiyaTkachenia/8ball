package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class BallShakerTest {

    private final BallShaker ballShaker = new BallShaker();

    @Test
    void shakeTheBallReturnsValidAnswer() {
        var possibleAnswers = EnumSet.allOf(Answer.class);
        var ballAnswer = ballShaker.shakeTheBall();
        assertTrue(possibleAnswers.contains(ballAnswer));
    }
}