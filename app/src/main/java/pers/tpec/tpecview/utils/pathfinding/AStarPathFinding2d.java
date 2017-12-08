package pers.tpec.tpecview.utils.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AStarPathFinding2d implements PathFinding2d<Integer> {
    public static final int MAP_NULL = 0;
    public static final int MAP_ACCESSIBLE = 1;
    public static final int MAP_BLOCKED = 2;

    private int[] map;
    private int width;

    @Override
    public void setMap(Integer[][] map) {
        width = map[0].length;
        this.map = new int[map.length * width];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < width; j++) {
                this.map[i * width + j] = map[i][j];
            }
        }
    }

    public void setMap(int[][] map) {
        width = map[0].length;
        this.map = new int[map.length * width];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, this.map, i * width, width);
        }
    }

    public void setMap(int[] map, int width) {
        this.width = width;
        this.map = map;
    }

    @Override
    public List<Integer> getPath(Integer start, Integer end) {
        return getPath((int) start, (int) end);
    }

    @Override
    public List<Integer> getPath(int start, int end) {
        List<Integer> r = new ArrayList<>();
        return r;
    }
}
