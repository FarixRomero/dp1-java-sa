package org.example;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
public class Bloqueo {
    private LocalDateTime fechaIni;
    private LocalDateTime fechaFin;
    private List<int[]> listaNodos;

    public Bloqueo(LocalDateTime fechaIni, LocalDateTime fechaFin, List<int[]> listaNodos) {
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.listaNodos = listaNodos;
    }

    public LocalDateTime getFechaIni() {
        return fechaIni;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public List<int[]> getListaNodos() {
        return listaNodos;
    }
}
