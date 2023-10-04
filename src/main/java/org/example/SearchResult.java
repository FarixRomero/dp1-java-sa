package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class SearchResult {
    public List<int[]> path;
    public int distance;
    public LocalDateTime currentTime;

    public List<int[]> getPath() {
        return path;
    }

    public void setPath(List<int[]> path) {
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public SearchResult(List<int[]> path, int distance, LocalDateTime currentTime) {
        this.path = path;
        this.distance = distance;
        this.currentTime = currentTime;
    }
}
