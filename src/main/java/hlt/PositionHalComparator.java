package hlt;


import java.util.Comparator;

public class PositionHalComparator implements Comparator<MapCell> {


    @Override
    public int compare(MapCell first, MapCell second) {
        return first.compareTo(second);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
