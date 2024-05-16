package com.github.chotkiymaster;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Match {
    private Player spieler1;
    private Player spieler2;
    private Field field;
    Map<Player, Integer> scores = new HashMap<>();
    Map<Player, Long> calculationTime = new HashMap<>();

    public Match(Player spieler1, Player spieler2, Field field){
        this.spieler1 = spieler1;
        this.spieler2 = spieler2;
        this.field = field;
        this.scores.put(spieler1, 0);
        this.scores.put(spieler2, 0);
        this.calculationTime.put(spieler1, 0L);
        this.calculationTime.put(spieler2, 0L);
    }

    private void keineAhnung(Player spieler){
        boolean rep;
        do{
            rep = false;
            long startTime = System.nanoTime();
            Wall curWall = spieler.step(this.field);
            long endTime = System.nanoTime();
            this.calculationTime.put(spieler, this.calculationTime.get(spieler) + endTime - startTime);
            if(null != curWall && !curWall.isClosed()){
                curWall.setClosed(true);
                for(int i = 0; i<this.field.getNeighbours(curWall).size(); i++){
                    var currentSquare = this.field.getNeighbours(curWall).get(i);
                    if(currentSquare.isClosed()){
                        currentSquare.setWinner(spieler);
                        this.scores.put(spieler, this.scores.get(spieler) + 1);
                        rep = true;
                    }
                }
            }
        }while(rep);
    }

    public void start(){
        display(this.field);
    }

    public void round() {

        if (this.field.isEnd()) {
            System.out.printf("Spieler %s: %d (%,d ns), Spieler %s: %d (%,d ns)%n",
                    this.spieler1.getName(),
                    this.scores.get(this.spieler1),
                    this.calculationTime.get(this.spieler1),
                    this.spieler2.getName(),
                    this.scores.get(this.spieler2),
                    this.calculationTime.get(this.spieler2)
            );
        } else {
            keineAhnung(this.spieler1);
            keineAhnung(this.spieler2);
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
