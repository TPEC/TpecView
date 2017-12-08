package pers.tpec.tpecview.utils.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStarPathFinding2d implements PathFinding2d<Integer> {
    public static final int MAP_NULL = -1;
    public static final int MAP_ACCESSIBLE = 0;
    public static final int MAP_BLOCKED = 1;

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
        boolean[] mapEnabled = new boolean[map.length];
        boolean[] mapChecked = new boolean[map.length];
        for (int i = 0; i < mapEnabled.length; i++) {
            mapEnabled[i] = true;
            mapChecked[i] = false;
        }
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                return Integer.compare(node1.value, node2.value);
            }
        };
        List<Integer> r = new ArrayList<>();
        Node n = new Node(end, 0, getDistance(start, end), null);
        mapEnabled[n.i] = false;
        mapChecked[n.i] = true;
        List<Node> pool = new ArrayList<>();
        while (n.i != start) {
            int t;
            for (int j = 0; j < 4; j++) {
                if ((t = getInterfacing(n.i, j)) != DIRECTION_ERROR) {
                    if (mapEnabled[t] && map[t] == MAP_ACCESSIBLE) {
                        int value = getDistance(start, t) + n.step + 1;
                        if (mapChecked[t]) {
                            for (int k = 0; k < pool.size(); k++) {
                                if (pool.get(k).i == t) {
                                    if (value < pool.get(k).value) {
                                        pool.set(k, new Node(t, n.step + 1, value, n));
                                    }
                                    break;
                                }
                            }
                        } else {
                            pool.add(new Node(t, n.step + 1, value, n));
                            mapChecked[t] = true;
                        }
                    }
                }
            }
            if (pool.size() == 0) {
                return r;
            }
            Collections.sort(pool, comparator);
            n = pool.get(0);
            pool.remove(0);
            mapEnabled[n.i] = false;
        }
        r.add(n.i);
        while (n.father != null) {
            n = n.father;
            r.add(n.i);
        }
        return r;
    }

    private class Node {
        public int i;
        int step;
        int value;
        Node father;

        public Node(int i, int step, int value, Node father) {
            this.i = i;
            this.step = step;
            this.value = value;
            this.father = father;
        }
    }

    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_TOP = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_BOTTOM = 3;
    private static final int DIRECTION_ERROR = -1;

    private int getInterfacing(int i, int direction) {
        switch (direction) {
            case DIRECTION_LEFT:
                if (i % width == 0) {
                    return DIRECTION_ERROR;
                }
                i--;
                break;
            case DIRECTION_TOP:
                if (i / width == 0) {
                    return DIRECTION_ERROR;
                }
                i -= width;
                break;
            case DIRECTION_RIGHT:
                if (i % width == width - 1) {
                    return DIRECTION_ERROR;
                }
                i++;
                break;
            case DIRECTION_BOTTOM:
                if (i + width >= map.length) {
                    return DIRECTION_ERROR;
                }
                i += width;
                break;
            default:
                return DIRECTION_ERROR;
        }
        return i;
    }

    private int getDistance(int a, int b) {
        int ax = a % width, ay = a / width;
        int bx = b % width, by = b / width;
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }
}
