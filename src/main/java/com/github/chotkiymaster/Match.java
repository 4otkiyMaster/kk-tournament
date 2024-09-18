package com.github.chotkiymaster;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.UUID;

public class Match {
    // private Player spieler1;
    // private Player spieler2;
    // private Player spieler3;
    // private Player spieler4;
    private Field field;
    private UUID id;
    List<Player> players = new LinkedList<>();
    Map<Player, Integer> scores = new HashMap<>();
    Map<Player, Long> calculationTime = new HashMap<>();
    Map<UUID, Wall> idMap = new HashMap<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Field getField() {
        return this.field;
    }

    public Match(/* Player spieler1, Player spieler2, */List<Player> players, Field field) {
        // this.spieler1 = spieler1;
        // this.spieler2 = spieler2;
        this.players = players;
        this.field = field;
        for (Square square : field.getSquares()) {
            for (Wall wall : square.getWalls()) {
                idMap.putIfAbsent(wall.getId(), wall);
            }
        }
        for (Player player : players) {
            this.scores.put(player, 0);
            this.calculationTime.put(player, 0L);
        }
        /*
         * this.scores.put(spieler1, 0);
         * this.scores.put(spieler2, 0);
         * this.calculationTime.put(spieler1, 0L);
         * this.calculationTime.put(spieler2, 0L);
         */
        this.id = UUID.randomUUID();
        System.out.println(id);
    }

    private void matchStep(Player spieler) {
        boolean rep;
        do {
            Field fieldCopy = new Field(field);
            rep = false;
            long startTime = System.nanoTime();
            Wall curWall = spieler.step(fieldCopy);
            long endTime = System.nanoTime();
            this.calculationTime.put(spieler, this.calculationTime.get(spieler) + endTime - startTime);
            if (curWall != null) {
                UUID uuid = curWall.getId();
                Wall curWallOriginal = idMap.get(uuid);
                if (null != curWallOriginal && !curWallOriginal.isClosed()) {
                    curWallOriginal.setClosed(true);
                    for (int i = 0; i < this.field.getNeighbours(curWallOriginal).size(); i++) {
                        var currentSquare = this.field.getNeighbours(curWallOriginal).get(i);
                        if (currentSquare.isClosed()) {
                            currentSquare.setWinner(spieler);
                            this.scores.put(spieler, this.scores.get(spieler) + 1);
                            rep = true;
                        }
                    }
                }
            }

        } while (rep);
    }

    public void start() {
        display(this.field);
    }

    public void round() {

        if (this.field.isEnd()) {
            for(Player player : this.players){
                System.out.printf("Spieler %s: %d (%,d ns)%n", 
                    player.getName(), 
                    scores.get(player), 
                    this.calculationTime.get(player));
            }
            /*System.out.printf("Spieler %s: %d (%,d ns), Spieler %s: %d (%,d ns)%n",
                    
                    this.spieler1.getName(),
                    this.scores.get(this.spieler1),
                    this.calculationTime.get(this.spieler1),
                    this.spieler2.getName(),
                    this.scores.get(this.spieler2),
                    this.calculationTime.get(this.spieler2));*/
        } else {
            for(Player player : this.players){
                matchStep(player);
            }
            /*matchStep(this.spieler1);
            matchStep(this.spieler2);*/
            this.field.repaint();
        }
    }

    private void display(Field field) {

        JFrame frame = new JFrame("For Testing");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(field);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        field.addMouseListener(new KKMouseListener(this::round));
    }
}
