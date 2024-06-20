package com.github.chotkiymaster;

public class Tournier {
    private Player[] players = new Player[]{new PlayerJan(), new GenieJan(), new SmarterPlayerDirk()};

    public void start() {
        for(int x = 0; x < 1 ; x++) {
            
            new Match(players[1],players[0], new Field(5, 5)).start();
            new Match(players[0],players[2], new Field(3, 3)).start();
            new Match(players[2],players[1], new Field(2, 2)).start();
        }
    }
    
}
