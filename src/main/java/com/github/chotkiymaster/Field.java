package com.github.chotkiymaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.github.chotkiymaster.domain.FieldData;

import java.awt.*;
import java.beans.Transient;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.UUID;

public class Field extends JComponent {
    //TODO:eine Deep-Kopie des Feldes erstellen. Jede Objekt durch Selbstkostruktor kopieren
    //ID zu jede Wand. IDs in Map
    //Player gibt Wall in FieldCopy, wir nehmen davon eine ID und schließen Wand in Field mit gleichem ID 
    //UUID statt einfach Integer

    public static final int SQUARE_SIZE = 23;
    public static final int GAP_BETWEEN_SQUARES = -2;
    public static final int OUTER_BORDER = 3;

    private FieldData data = new FieldData();

    public Square getSquare(int x, int y) {
        return data.getSquares()[x][y];
    }

    //private Square[][] data.getSquares();

    public int getXDimension() {
        return this.data.getSquares().length;
    }

    public int getYDimension() {
        return this.data.getSquares()[0].length;
    }


    public List<Square> getSquares() {
        return Arrays.stream(this.data.getSquares())
                .flatMap(Arrays::stream)
                .toList();
    }

    public FieldData getFieldData(){
        return this.data;
    }
    
    private Map<Wall, List<Square>> walls = new HashMap<>();
    public Field(int countX, int countY) {
        this.setSize(
                countX * (SQUARE_SIZE + GAP_BETWEEN_SQUARES) - GAP_BETWEEN_SQUARES,
                countY * (SQUARE_SIZE + GAP_BETWEEN_SQUARES) - GAP_BETWEEN_SQUARES
        );
        setBorder(BorderFactory.createEmptyBorder(OUTER_BORDER, OUTER_BORDER, OUTER_BORDER, OUTER_BORDER));

        data.setSquares(new Square[countX][countY]);
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {

                this.data.getSquares()[x][y] = new Square(
                    x>0 ? this.data.getSquares()[x-1][y].getRightWall() : new Wall(), 
                    new Wall(), 
                    new Wall(), 
                    y>0 ? this.data.getSquares()[x][y-1].getUpperWall() : new Wall()
                );
                
                if(x > 0) {
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.data.getSquares()[x][y], this.data.getSquares()[x-1][y]));
                }
                else{
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.data.getSquares()[x][y]));
                }
                this.walls.put(getSquare(x, y).getUpperWall(), List.of(this.data.getSquares()[x][y]));
                this.walls.put(getSquare(x, y).getRightWall(), List.of(this.data.getSquares()[x][y]));
                if(y > 0) {
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.data.getSquares()[x][y], this.data.getSquares()[x][y-1]));
                }
                else{
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.data.getSquares()[x][y]));
                }

                if(x == 0) {
                    this.data.getSquares()[x][y].getLeftWall().setClosed(true);
                }
                if(x == countX - 1) {
                    this.data.getSquares()[x][y].getRightWall().setClosed(true);
                }
                if(y == countY - 1) {
                    this.data.getSquares()[x][y].getUpperWall().setClosed(true);
                }
                if(y == 0) {
                    this.data.getSquares()[x][y].getBottomWall().setClosed(true);
                }
            }
        }
        
    }

    public Field(Field originalField) {
        int countX = originalField.getXDimension();
        int countY = originalField.getYDimension();

        this.setSize(originalField.getSize());
        setBorder(originalField.getBorder());

        data.setSquares(new Square[countX][countY]);
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                this.data.getSquares()[x][y] = new Square(
                    x>0 ? this.data.getSquares()[x-1][y].getRightWall() : null, 
                    y>0 ? this.data.getSquares()[x][y-1].getUpperWall() : null,
                    originalField.getSquare(x, y)
                );


                if(x > 0) {
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.data.getSquares()[x][y], this.data.getSquares()[x-1][y]));
                }
                else{
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.data.getSquares()[x][y]));
                }
                this.walls.put(getSquare(x, y).getUpperWall(), List.of(this.data.getSquares()[x][y]));
                this.walls.put(getSquare(x, y).getRightWall(), List.of(this.data.getSquares()[x][y]));
                if(y > 0) {
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.data.getSquares()[x][y], this.data.getSquares()[x][y-1]));
                }
                else{
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.data.getSquares()[x][y]));
                }
            }
        }
    }

    @Transient
    public List<Square> getNeighbours(Wall wall) {
        return this.walls.get(wall);
    }


    public boolean isEnd() {
        for (int y = 0; y < getYDimension(); y++) {
            for (int x = 0; x < getXDimension(); x++) {
                if(!getSquare(x, y).isClosed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (that instanceof Field thatField) {
            if (getXDimension() != thatField.data.getSquares().length || getYDimension() != thatField.data.getSquares()[0].length) {
                return false;
            }
            for (int y = 0; y < getYDimension(); y++) {
                for (int x = 0; x < getXDimension(); x++) {
                    if (!(this.getSquare(x,y) == null ? thatField.getSquare(x,y) == null : this.getSquare(x,y).equalStateWith(thatField.getSquare(x,y)))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {

        var height = getYDimension();
        var transposesquares = new Square[height][getXDimension()];
        for (int y = 0; y < getYDimension(); y++) {
            for (int x = 0; x < getXDimension(); x++) {
                transposesquares[height - 1 - y][x] = this.data.getSquares()[x][y];
            }
        }
        return String.format("{%n%s%n}",
                Arrays.stream(transposesquares)
                        .map(Arrays::toString)
                        .collect(Collectors.joining(String.format(",%n")))
        );
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (graphics instanceof Graphics2D graphics2D) {
            var initialTransform = graphics2D.getTransform();
            int countX = data.getSquares().length;
            int countY = data.getSquares()[0].length;

            graphics.setColor(Color.BLACK);
            for (int y = 0; y < countY; y++) {
                for (int x = 0; x < countX; x++) {
                    graphics2D.translate(OUTER_BORDER + x * (SQUARE_SIZE + GAP_BETWEEN_SQUARES), this.getHeight() - OUTER_BORDER - y * (SQUARE_SIZE + GAP_BETWEEN_SQUARES));
                    this.data.getSquares()[x][y].paint(graphics2D);
                    graphics2D.setTransform(initialTransform);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        final var insets = getBorder().getBorderInsets(this);
        return new Dimension(getWidth() + insets.left + insets.right, getHeight() + insets.bottom + insets.top);
    }
}
