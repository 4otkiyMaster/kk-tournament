package com.github.chotkiymaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Field extends JComponent {
    //TODO:eine Deep-Kopie des Feldes erstellen. Jede Objekt durch Selbstkostruktor kopieren
    //ID zu jede Wand. IDs in Map

    public static final int SQUARE_SIZE = 23;
    public static final int GAP_BETWEEN_SQUARES = -2;
    public static final int OUTER_BORDER = 3;

    public Square getSquare(int x, int y) {
        return squares[x][y];
    }

    private Square[][] squares;

    public int getXDimension() {
        return this.squares.length;
    }

    public int getYDimension() {
        return this.squares[0].length;
    }


    public List<Square> getSquares() {
        return Arrays.stream(this.squares)
                .flatMap(Arrays::stream)
                .toList();
    }
    
    private Map<Wall, List<Square>> walls = new HashMap<>();
    public Map<Wall, Integer> idMap = new HashMap<>();
    public Field(int countX, int countY) {
        this.setSize(
                countX * (SQUARE_SIZE + GAP_BETWEEN_SQUARES) - GAP_BETWEEN_SQUARES,
                countY * (SQUARE_SIZE + GAP_BETWEEN_SQUARES) - GAP_BETWEEN_SQUARES
        );
        setBorder(BorderFactory.createEmptyBorder(OUTER_BORDER, OUTER_BORDER, OUTER_BORDER, OUTER_BORDER));

        this.squares = new Square[countX][countY];
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {

                this.squares[x][y] = new Square(
                    x>0 ? this.squares[x-1][y].getRightWall() : new Wall(), 
                    new Wall(), 
                    new Wall(), 
                    y>0 ? this.squares[x][y-1].getUpperWall() : new Wall()
                );
                
                idMap.putIfAbsent(this.squares[x][y].getLeftWall(), idMap.size() + 1);
                idMap.putIfAbsent(this.squares[x][y].getUpperWall(), idMap.size() + 1);
                idMap.putIfAbsent(this.squares[x][y].getRightWall(), idMap.size() + 1);
                idMap.putIfAbsent(this.squares[x][y].getBottomWall(), idMap.size() + 1);


                if(x > 0) {
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.squares[x][y], this.squares[x-1][y]));
                }
                else{
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.squares[x][y]));
                }
                this.walls.put(getSquare(x, y).getUpperWall(), List.of(this.squares[x][y]));
                this.walls.put(getSquare(x, y).getRightWall(), List.of(this.squares[x][y]));
                if(y > 0) {
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.squares[x][y], this.squares[x][y-1]));
                }
                else{
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.squares[x][y]));
                }

                if(x == 0) {
                    this.squares[x][y].getLeftWall().setClosed(true);
                }
                if(x == countX - 1) {
                    this.squares[x][y].getRightWall().setClosed(true);
                }
                if(y == countY - 1) {
                    this.squares[x][y].getUpperWall().setClosed(true);
                }
                if(y == 0) {
                    this.squares[x][y].getBottomWall().setClosed(true);
                }
            }
        }
        
    }

    public Map<Wall, Integer> idMapCopy = new HashMap<>();
    public Field(Field originalField) {
        int countX = originalField.getXDimension();
        int countY = originalField.getYDimension();

        this.setSize(originalField.getSize());
        setBorder(originalField.getBorder());

        this.squares = new Square[countX][countY];
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                this.squares[x][y] = new Square(
                    x>0 ? this.squares[x-1][y].getRightWall() : null, 
                    y>0 ? this.squares[x][y-1].getUpperWall() : null,
                    originalField.getSquare(x, y)
                );

                idMapCopy.putIfAbsent(this.squares[x][y].getLeftWall(), idMapCopy.size() + 1);
                idMapCopy.putIfAbsent(this.squares[x][y].getUpperWall(), idMapCopy.size() + 1);
                idMapCopy.putIfAbsent(this.squares[x][y].getRightWall(), idMapCopy.size() + 1);
                idMapCopy.putIfAbsent(this.squares[x][y].getBottomWall(), idMapCopy.size() + 1);

                if(x > 0) {
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.squares[x][y], this.squares[x-1][y]));
                }
                else{
                    this.walls.put(getSquare(x, y).getLeftWall(), List.of(this.squares[x][y]));
                }
                this.walls.put(getSquare(x, y).getUpperWall(), List.of(this.squares[x][y]));
                this.walls.put(getSquare(x, y).getRightWall(), List.of(this.squares[x][y]));
                if(y > 0) {
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.squares[x][y], this.squares[x][y-1]));
                }
                else{
                    this.walls.put(getSquare(x, y).getBottomWall(), List.of(this.squares[x][y]));
                }
            }
        }
    }

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
            if (getXDimension() != thatField.squares.length || getYDimension() != thatField.squares[0].length) {
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
        var transposeSquares = new Square[height][getXDimension()];
        for (int y = 0; y < getYDimension(); y++) {
            for (int x = 0; x < getXDimension(); x++) {
                transposeSquares[height - 1 - y][x] = this.squares[x][y];
            }
        }
        return String.format("{%n%s%n}",
                Arrays.stream(transposeSquares)
                        .map(Arrays::toString)
                        .collect(Collectors.joining(String.format(",%n")))
        );
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (graphics instanceof Graphics2D graphics2D) {
            var initialTransform = graphics2D.getTransform();
            int countX = squares.length;
            int countY = squares[0].length;

            graphics.setColor(Color.BLACK);
            for (int y = 0; y < countY; y++) {
                for (int x = 0; x < countX; x++) {
                    graphics2D.translate(OUTER_BORDER + x * (SQUARE_SIZE + GAP_BETWEEN_SQUARES), this.getHeight() - OUTER_BORDER - y * (SQUARE_SIZE + GAP_BETWEEN_SQUARES));
                    this.squares[x][y].paint(graphics2D);
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
