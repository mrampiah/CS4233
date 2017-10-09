package util;

import gettysburg.common.Coordinate;
import model.GameCoordinate;
import model.GameState;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class PathFinder {

    private Coordinate target, src;
    private int maxDistance;
    public Collection<List<Coordinate>> paths;
    private Collection<Coordinate> visited;
    private GameState state;

    public PathFinder(GameState state) {
        this.state = state;
    }

    public void run(Coordinate where, Coordinate target, int maxDistance) {
        this.maxDistance = maxDistance;
        this.src = where;
        this.target = target;
        paths = new HashSet<>();
        List<Coordinate> currentPath = new LinkedList<>();
        visited = new HashSet<>();

        recursive(src, currentPath);
    }

    public void recursive(Coordinate current, List<Coordinate> pathTo) {
        List<Coordinate> path = new LinkedList<>(pathTo);
        path.add(current);
//        visited.add(current);
        //if unreachable, don't continue
        if (path.size() - 1 > maxDistance)
            return;

        //if found, return
        if (current.equals(target)) {
            paths.add(path);
            return;
        }

        //check neighbors (only free squares) and recurse
        for (Coordinate c : GameCoordinate.makeCoordinate(current).getNeighbors()) {
            if(state.board.getActiveZonesOfControl(state.step).contains(c) && !c.equals(target)) //check for zones of control
                continue;

                recursive(c, path);
        }

    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
