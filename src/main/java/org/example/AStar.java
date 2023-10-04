package org.example;

import java.util.*;
import java.time.LocalDateTime;

public class AStar {

//    public  SearchResult astar(BlockMap blockMap, int[] start, int[] goal, LocalDateTime currentTime) {
//        PriorityQueue<int[]> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> fScore(node, goal)));
//        openSet.add(start);
//        Map<int[], int[]> cameFrom = new HashMap<>();
//        Map<int[], Integer> gScore = new HashMap<>();
//
//        for (Bloqueo bloqueo : blockMap.getBlockedNodes()) {
//            for (int[] nodo : bloqueo.getListaNodos()) {
//                gScore.put(nodo, Integer.MAX_VALUE);
//            }
//        }
//
//        gScore.put(start, 0);
//
//        while (!openSet.isEmpty()) {
//            int[] current = openSet.poll();
//
//            if (Arrays.equals(current, goal)) {
//                List<int[]> path = reconstructPath(cameFrom, current);
//                int distance = path.size() - 1;
//                return new SearchResult(path, distance);
//            }
//
//            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
//            Arrays.sort(directions, (d1, d2) -> heuristic(add(current, d1), goal) - heuristic(add(current, d2), goal));
//
//            for (int[] direction : directions) {
//                int[] neighborNode = add(current, direction);
//
//                if (isValidNode(neighborNode, blockMap) && !isBlocked(neighborNode, blockMap, currentTime)) {
//                    System.out.println("Del nodo "+ neighborNode[0] + " , "+neighborNode[1]   + " No se encontró un camino."+ direction[0] + " , " + direction[1]);
//                    int tentativeGScore = gScore.get(current) + 1;
//
//                    if (tentativeGScore < gScore.getOrDefault(neighborNode, Integer.MAX_VALUE)) {
//                        cameFrom.put(neighborNode, current);
//                        gScore.put(neighborNode, tentativeGScore);
//                        openSet.add(neighborNode);
//                    }
//                }
//            }
//        }
//
//        System.out.println("No se encontró un camino.");
//        return null;
//    }
public SearchResult astar(BlockMap blockMap, int[] start, int[] goal, LocalDateTime currentTime) {
    PriorityQueue<int[]> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> fScore(node, goal)));
    openSet.add(start);
    Set<int[]> visitedNodes = new HashSet<>(); // Conjunto para rastrear los nodos visitados
    Map<int[], int[]> cameFrom = new HashMap<>();
    Map<int[], Integer> gScore = new HashMap<>();

    for (Bloqueo bloqueo : blockMap.getBlockedNodes()) {
        for (int[] nodo : bloqueo.getListaNodos()) {
            gScore.put(nodo, Integer.MAX_VALUE);
        }
    }

    gScore.put(start, 0);

    while (!openSet.isEmpty()) {
        int[] current = openSet.poll();
        visitedNodes.add(current); // Marcar el nodo actual como visitado

        if (Arrays.equals(current, goal)) {
            List<int[]> path = reconstructPath(cameFrom, current);
            int distance = path.size() - 1;
            return new SearchResult(path, distance,currentTime);
        }

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] direction : directions) {
            int[] neighborNode = add(current, direction);

            if (!visitedNodes.contains(neighborNode) && isValidNode(neighborNode, blockMap) && !isBlocked(neighborNode, blockMap, currentTime)) {
                int tentativeGScore = gScore.get(current) + 1;
                System.out.println("Del nodo "+ neighborNode[0] + " , "+neighborNode[1]   + " No se encontró un camino."+ direction[0] + " , " + direction[1]);

                if (tentativeGScore < gScore.getOrDefault(neighborNode, Integer.MAX_VALUE)) {
                    cameFrom.put(neighborNode, current);
                    gScore.put(neighborNode, tentativeGScore);
                    openSet.add(neighborNode);
                }
            }
        }
    }

    System.out.println("No se encontró un camino.");
    return null;
}


    private static int[] add(int[] a, int[] b) {
        return new int[]{a[0] + b[0], a[1] + b[1]};
    }

    private static int heuristic(int[] node, int[] goal) {
        return Math.abs(node[0] - goal[0]) + Math.abs(node[1] - goal[1]);
    }

    private static boolean isValidNode(int[] node, BlockMap blockMap) {
        return node[0] >= 0 && node[0] < blockMap.getWidth() && node[1] >= 0 && node[1] < blockMap.getHeight();
    }

    private static boolean isBlocked(int[] node, BlockMap blockMap, LocalDateTime currentTime) {
        return blockMap.isBlocked(node[0], node[1], currentTime);
    }

    private static List<int[]> reconstructPath(Map<int[], int[]> cameFrom, int[] current) {
        List<int[]> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static int fScore(int[] node, int[] goal) {
        return heuristic(node, goal);
    }
}

