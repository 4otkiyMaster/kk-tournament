package com.github.chotkiymaster.domain;

import com.github.chotkiymaster.Player;
import com.github.chotkiymaster.Wall;


public class SquareData {
    private Wall upperWall;
    private Wall leftWall;
    private Wall rightWall;
    private Wall bottomWall;

    private Player winner;

    public Wall getUpperWall() {
        return upperWall;
    }

    public void setUpperWall(Wall upperWall) {
        this.upperWall = upperWall;
    }

    public Wall getLeftWall() {
        return leftWall;
    }

    public void setLeftWall(Wall leftWall) {
        this.leftWall = leftWall;
    }

    public Wall getRightWall() {
        return rightWall;
    }

    public void setRightWall(Wall rightWall) {
        this.rightWall = rightWall;
    }

    public Wall getBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(Wall bottomWall) {
        this.bottomWall = bottomWall;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return this.winner;
    }

    public boolean isClosed() {
        return this.leftWall.isClosed() && this.upperWall.isClosed() && this.rightWall.isClosed() && this.bottomWall.isClosed();
    }
}
