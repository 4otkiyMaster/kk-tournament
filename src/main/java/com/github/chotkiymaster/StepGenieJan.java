package com.github.chotkiymaster;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StepGenieJan {
    public Set<Square> squaresInChains = new HashSet<>();
    private Field field;
    public StepGenieJan(Field field){
        this.field = field;
    }

    /**
     * Gibt alle Feldketten, die von einem Kästchen mit 3 offenen Wänden ausgehen.
     * @return Ketten
     */
    public List<List<Wall>> getRoomsOf3() {
        var squaresOf3 = getSquaresOfX(1);// Or move it to for-loop without variable?
        return getRooms(squaresOf3, 1);
    }

    /**
     * Gibt alle Feldketten, die von einem Kästchen mit 2 offenen Wänden ausgehen.
     * Überprüft nur die Kästchen, die noch nicht überprüft wurden.
     * @return Ketten
     */
    public List<List<Wall>> getRoomsOf2() {
        var squaresOf2 = getSquaresOfX(2);// Or move it to for-loop without variable?
        return getRooms(squaresOf2, 2);
    }

    /**
     * Gibt eine Liste aller Kästchen, mit der gegebenen Anzahl an geschlossenen Wänden zurück.
     * @param closedWallsOfStartSquare - Anzahl geschlossener Wände
     * @return Liste von Kästchen 
     */
    private List<Square> getSquaresOfX(int closedWallsOfStartSquare) {
        return field.getSquares()
                .stream()
                .filter(square -> square.closedWalls() == closedWallsOfStartSquare)
                .toList();
    }

    /**
    * Hier fehlt der Kommentar.
    */
    private List<List<Wall>> getRooms(List<Square> squaresOfX, int closedWallsOfStartSquare) {
        List<List<Wall>> currentRooms = new LinkedList<>();
        for (Square currentSquare : squaresOfX) {
            if (closedWallsOfStartSquare == 1 || !squaresInChains.contains(currentSquare)) {
                var wallsOfXSquare = currentSquare.getWalls()
                        .stream()
                        .filter(wall -> !wall.isClosed())
                        .toList();
                for (Wall currentWall : wallsOfXSquare) {
                    currentRooms.add(getChain(currentSquare, currentWall, currentSquare, currentWall));
                }
            }
        }
        return currentRooms;
    }

    /**
     * Findet eine Kette von Squares vom Startkästchen zu einem anderen Kästchen, 
     * von dem aus Sie keine neue Kästchen mehr schließen können.
     * 
     * @param startSquare eine Kästchen, von welchem die Prüfung startet. In Rekursion ist gleiche bei jedem Aufruf
     * @param startWall eine Wand, von welche Prüfung startet. In Rekursion ist gleiche bei jeder Aufruf
     * @param square aktuelles Kästchen, das jetzt geprüft wird
     * @param wall aktuelle Wand, die jetzt geprüft wird
     * @return die Kette von Squares
     */
    public List<Wall> getChain(Square startSquare, Wall startWall, Square square, Wall wall) {
        squaresInChains.add(square);

        return field.getNeighbours(wall)
                .stream()
                .filter(nextSquare -> nextSquare != square
                        && ((nextSquare.closedWalls() == 2 && nextSquare != startSquare)
                                || (nextSquare.closedWalls() == 1 && nextSquare == startSquare)))
                .findFirst()
                .map(nextSquare -> {
                    Wall nextWall = nextSquare.getWalls() // Ist das richtig?
                            .stream()
                            .filter(nextwall -> !nextwall.isClosed() && nextwall != wall && nextwall != startWall)
                            .findFirst()
                            .orElse(null);
                    List<Wall> result = new LinkedList<>();
                    result.add(wall);
                    result.addAll(getChain(startSquare, startWall, nextSquare, nextWall));
                    return result;
                })
                .orElse(List.of(wall));
    }
}
