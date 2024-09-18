package com.github.chotkiymaster.service;

import com.github.chotkiymaster.Field;
import com.github.chotkiymaster.Match;
import com.github.chotkiymaster.Player;
import com.github.chotkiymaster.Square;
import com.github.chotkiymaster.Wall;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IMatchService {
    Match getMatchById(UUID id);
    Collection<Match> getMatches();
    UUID createMatch(List<Player> players, int countX, int countY);
    Wall changeWall(UUID id, boolean closed);
    List<Square> getNeighboursService(UUID id, UUID matchId);
}