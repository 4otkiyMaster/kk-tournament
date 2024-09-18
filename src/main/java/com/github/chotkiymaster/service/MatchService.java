package com.github.chotkiymaster.service;

import com.github.chotkiymaster.Match;
import com.github.chotkiymaster.Field;
import com.github.chotkiymaster.Player;
import com.github.chotkiymaster.Square;
import com.github.chotkiymaster.Wall;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MatchService implements IMatchService {
    private final Map<UUID, Match> matches = new HashMap<>();
    private final Map<UUID, Wall> walls = new HashMap<>();

    @Override
    public Match getMatchById(UUID id) {
        return matches.get(id);
    }

    public Collection<Match> getMatches(){
        return matches.values();
    }

    @Override
    public UUID createMatch(List<Player> players, int countX, int countY) {
        Field field = new Field(countX, countY);
        Match match = new Match(players, field);
        matches.put(match.getId(), match);
        for(Square square: field.getSquares()){
            for(Wall wall: square.getWalls()){
                walls.putIfAbsent(wall.getId(), wall);
            }
        }
        return match.getId();
    }

    @Override
    public Wall changeWall(UUID id, boolean closed){
        Wall wall = walls.get(id);
        wall.setClosed(closed);
        return wall;
    }

    public List<Square> getNeighboursService(UUID id, UUID matchId){
        Field field = matches.get(matchId).getField();
        Wall wall = walls.get(id);
        return field.getNeighbours(wall);
    }
}