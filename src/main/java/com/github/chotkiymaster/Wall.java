package com.github.chotkiymaster;

import java.awt.*;
import java.util.UUID;

import static com.github.chotkiymaster.Field.SQUARE_SIZE;

public class Wall {
    private boolean closed = false;
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Wall() {
        this.id = UUID.randomUUID();
    }

    public Wall(Wall originalWall) {
        this.closed = originalWall.closed;
        this.id = originalWall.getId();
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean equalStateWith(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (that instanceof Wall thatWall) {
            return this.closed == thatWall.closed;
        }
        return false;
    }

    void paint(Graphics2D graphics) {
        graphics.drawLine(1,-1, SQUARE_SIZE - 1, -1);
        if (this.closed) {
            graphics.drawLine(2,-2, SQUARE_SIZE - 2, -2);
            graphics.drawLine(2,0, SQUARE_SIZE - 2, 0);
        }
    }
}
