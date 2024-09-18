package com.github.chotkiymaster;

import java.util.LinkedList;
import java.util.List;

public class Tournier {
    private Player[] players = new Player[]{new PlayerJan(), new GenieJan(), new SmarterPlayerDirk(), new PlayerDirk()};

    public void start() {
        List<Player> playersLinkedList = new LinkedList<>();
        for(int i = 0; i < players.length; i++){
            playersLinkedList.add(players[i]);
        }
        for(int x = 0; x < 1 ; x++) {
            
            new Match(playersLinkedList, new Field(15, 13)).start();
            //new Match(players[0],players[2], new Field(5, 5)).start();
            //new Match(players[2],players[1], new Field(5, 5)).start();
        }
    }
    
}
