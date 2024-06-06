package com.github.chotkiymaster;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static com.github.chotkiymaster.Field.SQUARE_SIZE;

public class Square {
    private Wall upperWall;
    private Wall leftWall;
    private Wall rightWall;
    private Wall bottomWall;

    private Player winner = null;

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


    public Square(Wall leftWall, Wall upperWall, Wall rightWall, Wall bottomWall) {
        this.leftWall = leftWall;
        this.upperWall = upperWall;
        this.rightWall = rightWall;
        this.bottomWall = bottomWall;
    }

    public Square(Wall leftWall, Wall bottomWall, Square originalSquare) {
        if(leftWall != null){
            this.leftWall = leftWall;
        } else{
            this.leftWall = new Wall(originalSquare.getLeftWall());
        }
        this.upperWall = new Wall(originalSquare.getUpperWall());
        this.rightWall = new Wall(originalSquare.getRightWall());
        if(bottomWall != null){
            this.bottomWall = bottomWall;
        } else{
            this.bottomWall = new Wall(originalSquare.getBottomWall());
        }
        if (originalSquare.getWinner() != null) {
            this.winner = originalSquare.getWinner();
        }
    }

    public List<Wall> getWalls() {
        return List.of(this.getLeftWall(), this.getUpperWall(), this.getRightWall(), this.getBottomWall())
                .stream()
                .toList();
    }


    public boolean isClosed() {
        return this.leftWall.isClosed() && this.upperWall.isClosed() && this.rightWall.isClosed() && this.bottomWall.isClosed();
    }

    public long closedWalls() {
        return Arrays.asList(
            this.leftWall.isClosed(),
            this.upperWall.isClosed(), 
            this.rightWall.isClosed(),
            this.bottomWall.isClosed()
        ).stream().filter(b -> b).count();
    }

    @Override
    public String toString() {
        return String.format("{R: %d, U: %d, L: %d, B: %d}",
                this.rightWall.isClosed() ? 1 : 0,
                this.upperWall.isClosed() ? 1 : 0,
                this.leftWall.isClosed() ? 1 : 0,
                this.bottomWall.isClosed() ? 1 : 0
        );
    }

    public boolean equalStateWith(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (that instanceof Square thatSquare) {
            return this.rightWall.equalStateWith(thatSquare.rightWall)
                    && this.upperWall.equalStateWith(thatSquare.upperWall)
                    && this.leftWall.equalStateWith(thatSquare.leftWall)
                    && this.bottomWall.equalStateWith(thatSquare.bottomWall);
        }
        return false;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return this.winner;
    }

    void paint(Graphics2D graphics) {
        if (null != this.winner) {
            graphics.drawString(this.winner.getName().substring(0, 1), 8, -6);
        }
        List.of(this.bottomWall, this.rightWall, this.upperWall, this.leftWall)
                .forEach(wall -> {
                    wall.paint(graphics);
                    graphics.translate(SQUARE_SIZE, 0);
                    graphics.rotate(-1.57079633); // 90° anti clockwise
                });
    }
}
