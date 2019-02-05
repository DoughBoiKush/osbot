package com.thatgamerblue.osbot.dax_walker;

import org.osbot.rs07.api.map.Position;

public class PathLocs {

    private final Position start;
    private final Position end;

    public PathLocs(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PathLocs && ((PathLocs) obj).getStart().equals(getStart()) && ((PathLocs) obj).getEnd().equals(getEnd());
    }
}
