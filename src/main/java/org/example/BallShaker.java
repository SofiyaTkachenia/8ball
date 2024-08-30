package org.example;

import java.util.Random;

public class BallShaker {

    public Answer shakeTheBall() {
        var random = new Random();
        var randomNumber = random.nextInt(Answer.values().length - 1);
        return Answer.values()[randomNumber];
    }
}
