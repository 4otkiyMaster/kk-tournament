package com.github.chotkiymaster.rest;

import com.github.chotkiymaster.Field;
import com.github.chotkiymaster.GenieJan;
import com.github.chotkiymaster.Match;
import com.github.chotkiymaster.Player;
import com.github.chotkiymaster.PlayerJan;
import com.github.chotkiymaster.Square;
import com.github.chotkiymaster.Wall;
import com.github.chotkiymaster.domain.FieldData;
import com.github.chotkiymaster.service.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:5173/")
public class MatchController {

    private final IMatchService matchService;

    @Autowired
    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/matches/{id}/field")
    public FieldData getFieldByMatchId(@PathVariable UUID id) {
        var match = matchService.getMatchById(id);
        if (match != null) {
            return match.getField().getFieldData();
        } else {
            throw new MatchNotFoundException("Match with ID " + id + " not found");
        }
    }

    @GetMapping("/matches/{id}/field/squares")
    public List<Square> getSquaresByMatchId(@PathVariable UUID id) {
        var match = matchService.getMatchById(id);
        if (match != null) {
            return match.getField().getSquares();
        } else {
            throw new MatchNotFoundException("Match with ID " + id + " not found");
        }
    }

    @GetMapping("/matches")
    public List<FieldData> getFields() {
        List<FieldData> fields = new LinkedList<>();
        var matches = matchService.getMatches();
        for (Match match : matches) {
            fields.add(match.getField().getFieldData());
        }
        return fields;
    }

    @GetMapping("/matches/{matchId}/field/walls/{id}/neighbours")
    public List<Square> getNeighbours(@PathVariable UUID matchId, @PathVariable UUID id) {
        return matchService.getNeighboursService(id, matchId);
    }
    

    @PostMapping
    public UUID createMatch(@RequestBody MatchRequest request) {
        Player player1 = new GenieJan();
        Player player2 = new PlayerJan();
        return matchService.createMatch(player1, player2, request.getCountX(), request.getCountY());
    }

    // PutMapping (id, setclosed)
    @PutMapping("walls/{id}")
    public Wall putWall(@PathVariable UUID id, @RequestBody WallRequest request) {
        return matchService.changeWall(id, request.isClosed());
    }

    @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
    public static class MatchNotFoundException extends RuntimeException {
        public MatchNotFoundException(String message) {
            super(message);
        }
    }

    public static class MatchRequest {
        private String player1;
        private String player2;
        private int countX;
        private int countY;

        public String getPlayer1() {
            return player1;
        }

        public void setPlayer1(String player1) {
            this.player1 = player1;
        }

        public String getPlayer2() {
            return player2;
        }

        public void setPlayer2(String player2) {
            this.player2 = player2;
        }

        public int getCountX() {
            return countX;
        }

        public void setCountX(int countX) {
            this.countX = countX;
        }

        public int getCountY() {
            return countY;
        }

        public void setCountY(int countY) {
            this.countY = countY;
        }
    }

    public static class WallRequest {
        private boolean closed;

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }
    }
}
