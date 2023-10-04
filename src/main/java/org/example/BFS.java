package org.example;

import java.time.LocalDateTime;
import java.util.*;

public class BFS {
    public SearchResult bfs(BlockMap blockMap, int[] start, int[] goal, LocalDateTime currentTime) {
        Queue<int[]> queue = new LinkedList<>();
        Set<int[]> visitedNodes = new HashSet<>();
        LocalDateTime inicial = currentTime;
//       System.out.println("Inicio -  pedido: "+Arrays.toString(start) + " - "+ Arrays.toString(goal) + " Hora - " + currentTime);

        queue.offer(start);
        visitedNodes.add(start);

        Map<int[], int[]> cameFrom = new HashMap();

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            // Incrementar el tiempo actual en 72 segundos
//            currentTime = currentTime.plusSeconds(72);

            if (Arrays.equals(current, goal)) {
                List<int[]> path = reconstructPath(cameFrom, current);
                int distance = path.size() - 1;
                return new SearchResult(path, distance,currentTime);
            }

            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            Map<int[], Integer> distances = new HashMap<>();
            for (int[] direction : directions) {
                int[] neighborNode = add(current, direction);
                int distance = Math.abs(neighborNode[0] - goal[0]) + Math.abs(neighborNode[1] - goal[1]);
                distances.put(direction, distance);
            }
            Arrays.sort(directions, Comparator.comparingInt(direction -> distances.get(direction)));


            for (int[] direction : directions) {
                int[] neighborNode = add(current, direction);

                boolean b = !this.isVisited(neighborNode, visitedNodes) && isValidNode(neighborNode, blockMap) && !isBlocked(neighborNode, blockMap, currentTime);
                if (b) {
                    // Agrega el nodo a visitedNodes tan pronto como lo descubras
                    visitedNodes.add(neighborNode);

                    queue.offer(neighborNode);

                    cameFrom.put(neighborNode, current);

                    // Calcular la longitud del camino desde el inicio hasta el nodo actual
                    List<int[]> path = reconstructPath(cameFrom, neighborNode);
                    int pathLength = path.size();

                    // Incrementar el tiempo actual en la longitud del camino multiplicado por 1.2
                    currentTime = inicial.plusMinutes((long) (pathLength * 1.2));

//                    System.out.println("Nodo visitado: " + Arrays.toString(neighborNode));
//                    for (int[] node : visitedNodes) {
//                        System.out.println(Arrays.toString(node));
//                    }
                }
            }

        }

        System.out.println("No se encontró un camino." + currentTime);
        return null;
    }
    public boolean isVisited(int[] node, Set<int[]> visitedNodes) {
        for (int[] visitedNode : visitedNodes) {
            if (Arrays.equals(node, visitedNode)) {
                return true;
            }
        }
        return false;
    }

//    public SearchResult bfs(BlockMap blockMap, int[] start, int[] goal, LocalDateTime currentTime) {
//        Queue<int[]> queue = new LinkedList<>();
//        Set<int[]> visitedNodes = new HashSet<>();
//
//        queue.offer(start);
//        visitedNodes.add(start);
//
//        Map<int[], int[]> cameFrom = new HashMap();
//
//        while (!queue.isEmpty()) {
//            int[] current = queue.poll();
//
//            // Incrementar el tiempo actual en 72 segundos
//            currentTime = currentTime.plusSeconds(72);
//
//            if (Arrays.equals(current, goal)) {
//                List<int[]> path = reconstructPath(cameFrom, current);
//                int distance = path.size() - 1;
//                return new SearchResult(path, distance);
//            }
//
//            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
//
//            for (int[] direction : directions) {
//                int[] neighborNode = add(current, direction);
//
//                if (!visitedNodes.contains(neighborNode) && isValidNode(neighborNode, blockMap) && !isBlocked(neighborNode, blockMap, currentTime)) {
//                    visitedNodes.add(neighborNode);  // Agrega el nodo a visitedNodes tan pronto como lo descubras
//                    queue.offer(neighborNode);
//                    cameFrom.put(neighborNode, current);
//                    System.out.println("Se explora: " + neighborNode[0] +" - "+ neighborNode[1]);
//                }
//            }
//        }
//
//        System.out.println("No se encontró un camino.");
//        return null;
//    }

//    public SearchResult bfs(BlockMap blockMap, int[] start, int[] goal, LocalDateTime currentTime) {
//        Queue<int[]> queue = new LinkedList<>();
//        Set<int[]> visitedNodes = new HashSet<>();
//
//        queue.offer(start);
//        visitedNodes.add(start);
//
//        Map<int[], int[]> cameFrom = new HashMap();
//
//        while (!queue.isEmpty()) {
//            int[] current = queue.poll();
//
//            // Incrementar el tiempo actual en 72 segundos
//            currentTime = currentTime.plusSeconds(72);
//
//            if (Arrays.equals(current, goal)) {
//                List<int[]> path = reconstructPath(cameFrom, current);
//                int distance = path.size() - 1;
//                return new SearchResult(path, distance);
//            }
//
//            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
//
//            for (int[] direction : directions) {
//                int[] neighborNode = add(current, direction);
//
//                if (!visitedNodes.contains(neighborNode) && isValidNode(neighborNode, blockMap) && !isBlocked(neighborNode, blockMap, currentTime)) {
//                    visitedNodes.add(neighborNode);
//                    queue.offer(neighborNode);
//                    cameFrom.put(neighborNode, current);
//                }
//            }
//        }
//
//        System.out.println("No se encontró un camino.");
//        return null;
//    }

    // Métodos auxiliares para verificar si un nodo es válido y si está bloqueado
    private boolean isValidNode(int[] node, BlockMap blockMap) {
        return node[0] >= 0 && node[0] < blockMap.getWidth() && node[1] >= 0 && node[1] < blockMap.getHeight();
    }

    private boolean isBlocked(int[] node, BlockMap blockMap, LocalDateTime currentTime) {
        return blockMap.isBlocked(node[0], node[1], currentTime);
    }

    // Método para reconstruir el camino desde el nodo objetivo hasta el nodo inicial
    private List<int[]> reconstructPath(Map<int[], int[]> cameFrom, int[] current) {
        List<int[]> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    // Método para sumar dos arreglos de enteros
    private int[] add(int[] a, int[] b) {
        return new int[]{a[0] + b[0], a[1] + b[1]};
    }
}
