package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Camion {
    public int capacidadMaxima;
    public int capacidadActual;
    public int[] posicionActual;

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(int capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    public int[] getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(int[] posicionActual) {
        this.posicionActual = posicionActual;
    }



    public void setTiempoActual(LocalDateTime tiempoActual) {
        this.tiempoActual = tiempoActual;
    }

    public List<Pedido> getRuta() {
        return ruta;
    }

    public void setRuta(List<Pedido> ruta) {
        this.ruta = ruta;
    }

    public LocalDateTime tiempoActual;
    public List<Pedido> ruta;

    public Camion(int capacidadMaxima, int capacidadActual, int[] posicionActual, LocalDateTime tiempoActual, List<Pedido> ruta) {
        this.capacidadMaxima = capacidadMaxima;
        this.capacidadActual = capacidadActual;
        this.posicionActual = posicionActual;
        this.tiempoActual = tiempoActual;
        this.ruta = new ArrayList<>(ruta);
    }

    public boolean cargar(int cantidad) {
        if (capacidadActual + cantidad <= capacidadMaxima) {
            capacidadActual += cantidad;
            return true;
        } else {
            return false;
        }
    }

    public boolean descargar(int cantidad) {
        if (cantidad <= capacidadActual) {
            capacidadActual -= cantidad;
            return true;
        } else {
            return false;
        }
    }

    public void moverA(int[] nuevaPosicion) {
        posicionActual = nuevaPosicion;
    }

    public LocalDateTime getTiempoActual() {
        return this.tiempoActual;
    }
}
