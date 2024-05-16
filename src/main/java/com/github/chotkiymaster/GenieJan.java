package com.github.chotkiymaster;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class GenieJan extends PlayerJan {
    private List<List<Wall>> rooms;

    /**
     * Wählt eine Wand für ein unsafe step().
     * @return Erste Wand aus der kleinsten Liste von Ketten
     */
    @Override
    protected Wall unsafe(Field field) {

        init(field);
        return this.rooms
                .stream()
                .min(Comparator.comparing(List::size))
                .orElse(List.of())
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Sammelt alle Ketten durch Aufrufen von "getRoomsOf3" und "getRoomsOf2"
     */
    public void init(Field field) { //umbenennen?
        StepGenieJan stepGenieJan = new StepGenieJan(field);
        this.rooms = Stream.concat(stepGenieJan.getRoomsOf3().stream(), stepGenieJan.getRoomsOf2().stream())
                .toList();
    }

    /**
     * Gibt den Namen des Spielers zurück
     */
    @Override
    public String getName() {
        return "GenieJan";
    }
}
