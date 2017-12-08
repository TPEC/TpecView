package pers.tpec.tpecview.utils.pathfinding;

import java.util.List;

public interface PathFinding2d<T> {
    void setMap(T[][] map);

    List<T> getPath(final T start, final T end);

    List<T> getPath(final int start, final int end);
}
