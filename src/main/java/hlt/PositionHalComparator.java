package hlt;


import java.util.Comparator;

public class PositionHalComparator<MapCell> implements Comparator<MapCell> {


    @Override
    public int compare(MapCell o1, MapCell o2) {
        return o1.halite - o2.halite;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
