package org.example;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pedido {
    public LocalDateTime fecha;
    public int[] posicion;
    public String codCliente;
    public int demanda;
    public int tiempoLimite;

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int[] getPosicion() {
        return posicion;
    }

    public void setPosicion(int[] posicion) {
        this.posicion = posicion;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public int getDemanda() {
        return demanda;
    }

    public void setDemanda(int demanda) {
        this.demanda = demanda;
    }

    public int getTiempoLimite() {
        return tiempoLimite;
    }

    public void setTiempoLimite(int tiempoLimite) {
        this.tiempoLimite = tiempoLimite;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public List<int[]> getListaRecorrido() {
        return listaRecorrido;
    }

    public void setListaRecorrido(List<int[]> listaRecorrido) {
        this.listaRecorrido = listaRecorrido;
    }

    public LocalDateTime fechaFin;
    public char estado;
    public List<int[]> listaRecorrido;

    public Pedido(LocalDateTime fecha, int[] posicion, String codCliente, int demanda, int tiempoLimite, LocalDateTime fechaFin, char estado, List<int[]> listaRecorrido) {
        this.fecha = fecha;
        this.posicion = posicion;
        this.codCliente = codCliente;
        this.demanda = demanda;
        this.tiempoLimite = tiempoLimite;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.listaRecorrido = listaRecorrido;
    }
    public static List<Pedido> leerPedidos(String nombreArchivo) {
        List<Pedido> pedidos = new ArrayList<>();

        // Extraer el año y mes del nombre del archivo
        Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})");
        Matcher matcher = pattern.matcher(nombreArchivo);

        int year = 0;
        int mes = 0;

        if (matcher.find()) {
            year = Integer.parseInt(matcher.group(1));
            mes = Integer.parseInt(matcher.group(2));
        } else {
            System.out.println("El nombre del archivo no tiene el formato esperado (ventasYYYYMM.txt).");
            return pedidos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en sus componentes
                String[] elementos = linea.split(",");
                String[] numeros = elementos[0].replaceAll("\\D", " ").trim().split("\\s+");
                int dias = Integer.parseInt(numeros[0]);
                int horas = Integer.parseInt(numeros[1]);
                int minutos = Integer.parseInt(numeros[2]);
                int[] posicion = {Integer.parseInt(numeros[3]), Integer.parseInt(elementos[1])};
                LocalDateTime fecha = LocalDateTime.of(year, mes, dias, horas, minutos);
                String codCliente = elementos[2];
                int demanda = Integer.parseInt(elementos[3].replace("m3", ""));
                int tiempoLimite = Integer.parseInt(elementos[4].replace("h", ""));
                LocalDateTime fechaFin = fecha.plusMinutes(60 * tiempoLimite);

                // Crear objetos Pedido y agregarlos a la lista
                while (demanda > 0) {
                    int cantidad = Math.min(25, demanda);
                    Pedido pedido = new Pedido(fecha, posicion, codCliente, cantidad, tiempoLimite, fechaFin, 'E', new ArrayList<>());
                    pedidos.add(pedido);
                    demanda -= cantidad;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pedidos;
    }
}
