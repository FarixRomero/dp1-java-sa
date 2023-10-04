package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockMap {
    private int width;
    private int height;
    private List<Bloqueo> blockedNodes;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Bloqueo> getBlockedNodes() {
        return blockedNodes;
    }

    public void setBlockedNodes(List<Bloqueo> blockedNodes) {
        this.blockedNodes = blockedNodes;
    }

    public BlockMap(int width, int height, List<Bloqueo> blockedNodes) {
        this.width = width;
        this.height = height;
        this.blockedNodes = blockedNodes;
    }
    public boolean isBlocked(int x, int y, LocalDateTime time) {
        for (Bloqueo bloqueo : blockedNodes) {
            for (int[] node : bloqueo.getListaNodos()) {
                if (node[0] == x && node[1] == y && bloqueo.getFechaIni().compareTo(time) <= 0 && bloqueo.getFechaFin().compareTo(time) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    public static List<Bloqueo> leerBloqueos(String nombreArchivo) {
        List<Bloqueo> bloqueos = new ArrayList<>();

        // Extraer el año y mes del nombre del archivo
        Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})\\.");
        Matcher matcher = pattern.matcher(nombreArchivo);

        int year = 0;
        int mes = 0;

        if (matcher.find()) {
            year = Integer.parseInt(matcher.group(1));
            mes = Integer.parseInt(matcher.group(2));
        } else {
            System.out.println("El nombre del archivo no tiene el formato esperado (YYYYMM.bloqueos.txt).");
            return bloqueos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en sus componentes
                String[] elementos = linea.split(",");
                String[] numeros = elementos[0].replaceAll("\\D", " ").trim().split("\\s+");
                int diasI = Integer.parseInt(numeros[0]);
                int horasI = Integer.parseInt(numeros[1]);
                int minutosI = Integer.parseInt(numeros[2]);
                int diasF = Integer.parseInt(numeros[3]);
                int horasF = Integer.parseInt(numeros[4]);
                int minutosF = Integer.parseInt(numeros[5]);
                LocalDateTime fechaIni = LocalDateTime.of(year, mes, diasI, horasI, minutosI);
                LocalDateTime fechaFin = LocalDateTime.of(year, mes, diasF, horasF, minutosF);

                List<int[]> listaTuplas = new ArrayList<>();
                int xInicioT = Integer.parseInt(numeros[6]);
                int yInicioT = Integer.parseInt(elementos[1]);
                int[] tupla1 = {xInicioT, yInicioT};
                listaTuplas.add(tupla1);

                for (int i = 2; i < elementos.length - 1; i += 2) {
                    int elemento1 = Integer.parseInt(elementos[i]);
                    int elemento2 = Integer.parseInt(elementos[i + 1]);
                    int[] tupla = {elemento1, elemento2};
                    listaTuplas.add(tupla);
                }

                List<int[]> listaNodos = new ArrayList<>();
                for (int i = 0; i < listaTuplas.size() - 1; i++) {
                    int[] puntoInicio = listaTuplas.get(i);
                    int[] puntoFin = listaTuplas.get(i + 1);
                    int xInicio = puntoInicio[0];
                    int yInicio = puntoInicio[1];
                    int xFin = puntoFin[0];
                    int yFin = puntoFin[1];

                    if (!listaNodos.contains(puntoInicio)) {
                        listaNodos.add(puntoInicio);
                    }

                    if (xInicio == xFin) {
                        for (int y = yInicio + 1; y <= yFin; y++) {
                            int[] nodo = {xInicio, y};
                            listaNodos.add(nodo);
                        }
                    } else {
                        for (int x = xInicio + 1; x < xFin; x++) {
                            int[] nodo = {x, yFin};
                            listaNodos.add(nodo);
                        }
                    }

                    if (!listaNodos.contains(puntoFin)) {
                        listaNodos.add(puntoFin);
                    }
                }

                Bloqueo bloqueo = new Bloqueo(fechaIni, fechaFin, listaNodos);
                bloqueos.add(bloqueo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bloqueos;
    }
}
