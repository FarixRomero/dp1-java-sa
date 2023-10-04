package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;
import java.util.Random;
public class Main {
    public static void main(String[] args) {
        String rutaArchivo = "C:/Users/Farix Romero/Documents/Dp1/Dp1ExpNum/ventas202310.txt";

        List<Pedido> pedidos = leerPedidos(rutaArchivo);
        List<Pedido> pedidosOrdenados = new ArrayList<>(pedidos);
        pedidosOrdenados.sort(Comparator.comparing(Pedido::getFecha).thenComparing(Pedido::getFechaFin));


        // Imprimir los pedidos
//        for (Pedido pedido : pedidos) {
//            System.out.println("Fecha: " + pedido.getFecha());
//            System.out.println("Posición: " + pedido.getPosicion()[0] + ", " + pedido.getPosicion()[1]);
//            System.out.println("Código de Cliente: " + pedido.getCodCliente());
//            System.out.println("Demanda (m³): " + pedido.getDemanda());
//            System.out.println("Tiempo Límite (horas): " + pedido.getTiempoLimite());
//            System.out.println("Fecha límite: " + pedido.getFechaFin());
//            System.out.println("Estado: " + pedido.getEstado());
//            System.out.println();
//        }
        //Bloqueos
        String rutaArchivoB = "C:/Users/Farix Romero/Documents/Dp1/Dp1ExpNum/202310.bloqueos.txt";

        List<Bloqueo> bloqueos = leerBloqueos(rutaArchivoB);
        BlockMap blockMap = new BlockMap(71, 51, bloqueos);

        // Imprimir los bloqueos
//        for (Bloqueo bloqueo : bloqueos) {
//            System.out.println("FechaIni: " + bloqueo.getFechaIni());
//            System.out.println("FechaFin: " + bloqueo.getFechaFin());
//            for (int[] nodo : bloqueo.getListaNodos()) {
//                System.out.println("Nodo: " + nodo[0] + ", " + nodo[1]);
//            }
//            System.out.println();
//        }
////        BlockMap block_map = new BlockMap(71, 51, bloqueos);
////        int[] punto_ini = {37, 12};
////        int[] punto_fin = {8, 17 };
////        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 2, 1, 55, 0);
////        SearchResult result= distanciaEntrePuntos(block_map, punto_ini,punto_fin,dateTime);
////
////        List<int[]> path = result.path;
////        int distance = result.distance;
////
////        System.out.println("Camino:");
////        for (int[] node : path) {
////            System.out.println("[" + node[0] + ", " + node[1] + "]");
////        }
//////
//        System.out.println("Distancia: " + distance + result.currentTime); // Imprime la distancia

        // Crear una lista de Camiones
        List<Camion> camiones = new ArrayList<>();
        // Crear varios objetos Camion y agregarlos a la lista
        for (int i = 0; i < 20; i++) {
            int capacidadMaxima = (i < 2) ? 25 : (i < 6) ? 15 : (i < 10) ? 10 : 5;
            int[] posicionActual = (i == 9) ? new int[]{12, 10} : new int[]{10, 12};
            LocalDateTime tiempoActual = LocalDateTime.of(2023, 10, 1, 0, 0, 0);
            List<Pedido> ruta = new ArrayList<>();
            Camion camion = new Camion(capacidadMaxima, capacidadMaxima, posicionActual, tiempoActual, ruta);
            camiones.add(camion);
        }
////
        List<Camion> solucion = new ArrayList<>();
//        solucion = asignarPedidosInicialmente(pedidos, camiones);
//
//
        int[] almacenCentral = {12, 8};
        int[] almacenNorte = {42, 42};
        int[] almacenEste = {63, 3};
        double temperaturaInicial = 100;
        double enfriamiento = 0.1;
        int iteraciones = 1;


        solucion = simulatedAnnealing(camiones,pedidos,blockMap,almacenCentral,almacenNorte,almacenEste,temperaturaInicial,enfriamiento,iteraciones);
//
        calcularEnergia(solucion, blockMap, almacenCentral, almacenNorte, almacenEste);
//
        LocalDateTime fechaIniSim = LocalDateTime.of(2023, 10, 1, 0, 0);
        imprimirSolucion(solucion, fechaIniSim);
    }

    public static double calcularEnergia(List<Camion> camiones, BlockMap blockMap, int[] almacenCentral, int[] almacenNorte, int[] almacenEste) {
        double costoTotal = 0;
        for (Camion camion : camiones) {
            costoTotal += calcularCostoRuta(camion, blockMap, almacenCentral, almacenNorte, almacenEste);
            break;
        }
        return costoTotal;
    }

    public static double calcularCostoRuta(Camion camion, BlockMap blockMap, int[] almacen_central, int[] almacen_norte, int[] almacen_este) {
        double costoTotal = 0;
        LocalDateTime tiempoActual = camion.getTiempoActual();
        int[] ubicacionActual = camion.getPosicionActual();
        int capacidadActual = camion.getCapacidadActual();

        for (Pedido pedido : camion.getRuta()) {
            System.out.println("Pedido estado: " + pedido.estado);
            if (pedido.getEstado() == 'E') {
                int demanda = pedido.getDemanda();
                int[] ubicacionPedido = pedido.getPosicion();

                if (pedido.getFecha().isAfter(tiempoActual)) {
                    tiempoActual = pedido.getFecha();
                }
                System.out.println("LLego a buscar distancia: " + ubicacionActual[0] + ubicacionActual[1] + "Y la ubicacion del pedido es " + ubicacionPedido[0] + ubicacionPedido[1]);
                if (capacidadActual < demanda) {
                    // Calcular la distancia al almacén más cercano (norte o este)
                    SearchResult caminoNorte = distanciaEntrePuntos(blockMap, ubicacionActual, almacen_norte, tiempoActual);
                    SearchResult caminoEste = distanciaEntrePuntos(blockMap, ubicacionActual, almacen_este, tiempoActual);

                    // Elegir el almacén más cercano...
                    double distanciaAlmacenMasCercano;
                    int[] ubicacionAlmacenMasCercano;
                    List<int[]> caminoAlmacenMasCercano;
                    if (caminoNorte.getDistance() < caminoEste.getDistance()) {
                        distanciaAlmacenMasCercano = caminoNorte.getDistance();
                        ubicacionAlmacenMasCercano = almacen_norte;
                        caminoAlmacenMasCercano = caminoNorte.getPath();
                    } else {
                        distanciaAlmacenMasCercano = caminoEste.getDistance();
                        ubicacionAlmacenMasCercano = almacen_este;
                        caminoAlmacenMasCercano = caminoEste.getPath();
                    }

                    // Comprobar si el almacén central es más cercano que el norte o este
                    SearchResult caminoCentral = distanciaEntrePuntos(blockMap, ubicacionActual, almacen_central, tiempoActual);
                    if (caminoCentral.getDistance() < distanciaAlmacenMasCercano) {
                        distanciaAlmacenMasCercano = caminoCentral.getDistance();
                        ubicacionAlmacenMasCercano = almacen_central;
                        caminoAlmacenMasCercano = caminoCentral.getPath();
                    }

                    // Agregar la distancia al almacén y actualizar la capacidad actual
                    costoTotal += distanciaAlmacenMasCercano;
                    capacidadActual = camion.getCapacidadMaxima();  // Recargar completamente el camión
                    ubicacionActual = ubicacionAlmacenMasCercano;

                    // Aquí está el camino solo como nodos x,y
                    List<int[]> camino = new ArrayList<>(caminoAlmacenMasCercano);

                    // Actualizar tiempo con camino recorrido hasta el almacén
                    tiempoActual = tiempoActual.plusMinutes((long) (1.2 * distanciaAlmacenMasCercano));
                    System.out.println("LLego recargar: " +  ubicacionActual[0] + ubicacionActual[1]);

                }


                SearchResult searchResult = distanciaEntrePuntos(blockMap, ubicacionActual, ubicacionPedido, tiempoActual);

                List<int[]> caminoPedido = searchResult.getPath();
                double distancia = searchResult.getDistance();

                costoTotal += distancia;

                capacidadActual -= demanda;
                ubicacionActual = ubicacionPedido;
                pedido.setListaRecorrido(caminoPedido);

                tiempoActual = tiempoActual.plusMinutes((long) (1.2 * distancia));
            }
        }

        return costoTotal;
    }


    public static List<Camion> asignarPedidosInicialmente(List<Pedido> pedidos, List<Camion> camiones) {
        List<Camion> camionesSinPedidos = new ArrayList<>(camiones);
        List<Camion> camionesConPedidos = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            List<Camion> camionesDisponibles = new ArrayList<>();
            for (Camion camion : camionesSinPedidos) {
                if (camion.capacidadMaxima >= pedido.demanda) {
                    camionesDisponibles.add(camion);
                }
            }

            Camion camionAsignado;
            if (camionesDisponibles.isEmpty()) {
                // If no trucks are available for the order, add the order to an existing truck randomly
                // But it also has to see that it can handle the demand
                List<Camion> camionesDentroDemanda = new ArrayList<>();
                for (Camion c : camionesConPedidos) {
                    if (c.capacidadMaxima >= pedido.demanda) {
                        camionesDentroDemanda.add(c);
                    }
                }

                if (!camionesDentroDemanda.isEmpty()) {
                    Random rand = new Random();
                    camionAsignado = camionesDentroDemanda.get(rand.nextInt(camionesDentroDemanda.size()));
                } else {
                    System.out.println("The demand of the order that does not fit into the maximum capacity is " + pedido.demanda);
                    continue;
                }
            } else {
                // If there are trucks available, assign the order to the first one that has no orders
                camionAsignado = camionesDisponibles.get(0);
                // Move the truck from the list without orders to the list with orders
                camionesSinPedidos.remove(camionAsignado);
                camionesConPedidos.add(camionAsignado);
            }

            // Add the order to the assigned truck
            camionAsignado.ruta.add(pedido);
        }

        camiones.clear();
        camiones.addAll(camionesConPedidos);
        camiones.addAll(camionesSinPedidos);

        // Only for the first entry of each truck
        for (Camion c : camiones) {
            if (!c.ruta.isEmpty()) { // If it has orders, we see if it has Completed and Waiting
                List<Pedido> pedidosC = new ArrayList<>();
                List<Pedido> pedidosE = new ArrayList<>();
                for (Pedido p : c.ruta) {
                    if (p.estado == 'C') {
                        pedidosC.add(p);
                    } else if (p.estado == 'E') {
                        pedidosE.add(p);
                    }
                }

                if (!pedidosE.isEmpty() || !pedidosC.isEmpty()) {
                    Pedido p = new Pedido(c.getTiempoActual(), c.getPosicionActual(), "Ficticio", 0, 0, c.getTiempoActual(), 'P', Arrays.asList(new int[]{10, 12}));
                    c.ruta.add(0, p);
                }
            }
        }

        return camiones;
    }


    public static SearchResult distanciaEntrePuntos(BlockMap blockMap, int[] punto1, int[] punto2, LocalDateTime tiempoActual) {
//        AStar aStar = new AStar();
//        SearchResult result = aStar.astar(blockMap, punto1, punto2, tiempoActual);
        BFS bfs = new BFS();
        SearchResult result = bfs.bfs(blockMap, punto1, punto2, tiempoActual);

        return result;
    }

    public static List<Bloqueo> leerBloqueos(String nombreArchivo) {
        // El código para leer bloqueos debe estar aquí
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

    public static void imprimirSolucion(List<Camion> camiones, LocalDateTime tiempo_sim) {
        System.out.println("\nMejor solución encontrada:\n");
        for (int i = 0; i < camiones.size(); i++) {
            Camion camion = camiones.get(i);
            System.out.println("Camión: " + (i + 1) + "\n");
            System.out.println("Ruta: ");
            for (Pedido pedido : camion.ruta) {
                System.out.println("Fecha: " + pedido.fecha);
                System.out.println("Posición: " + Arrays.toString(pedido.posicion));
                System.out.println("Código de Cliente: " + pedido.codCliente);
                System.out.println("Demanda: " + pedido.demanda);
                System.out.println("Tiempo Límite: " + pedido.tiempoLimite);
                System.out.println("Fecha de Fin: " + pedido.fechaFin);
                System.out.println("Estado: " + pedido.estado);
                System.out.println("Lista de Recorrido: ");
                for (int[] n : pedido.listaRecorrido) {
//                    LocalDateTime fecha = LocalDateTime.of(n[2], n[1], n[0], 0, 0, 0);
//                    LocalDateTime t_temp = tiempo_sim.minusMinutes(60);
//                    if (fecha.isBefore(t_temp) || fecha.isEqual(t_temp)) {
                    System.out.println(Arrays.toString(n));
//                    } else {
////                        break;
//                    }
                }
                System.out.println("-".repeat(30) + "\n");  // Separador entre pedidos
            }
        }
    }
    public static double enfriamientoLineal(double temperaturaInicial, int iteracion, int iteracionesTotales) {
        return temperaturaInicial * (1 - (double) iteracion / iteracionesTotales);
    }
    private static final Random random = new Random();

    public static List<Camion> simulatedAnnealing(List<Camion> camiones, List<Pedido> pedidos,BlockMap blockMap , int[] almacenCentral, int[] almacenNorte, int[] almacenEste,
                                              double temperaturaInicial, double enfriamiento, int iteraciones) {
        List<Camion> mejorSolucion = asignarPedidosInicialmente(pedidos, camiones);
        double mejorCosto = calcularEnergia(mejorSolucion, blockMap,almacenCentral, almacenNorte, almacenEste);
        List<Camion> solucionActual = mejorSolucion;
        double costoActual = mejorCosto;
        for (int iteracion = 0; iteracion < iteraciones; iteracion++) {
            double temperatura = enfriamientoLineal(temperaturaInicial, iteracion, iteraciones);
            // Generar un vecino
//            List<Camion> vecino = generarVecino(solucionActual);
            List<Camion> vecino = mejorSolucion;

            // Calcular la energía del vecino
            double costoVecino = calcularEnergia(vecino,blockMap, almacenCentral, almacenNorte, almacenEste);
            // Calcular el cambio en la energía
            double deltaEnergia = costoVecino - costoActual;
            // Decidir si se acepta el vecino
            if (deltaEnergia < 0 || random.nextDouble() < Math.exp(-deltaEnergia / temperatura)) {
                solucionActual = vecino;
                costoActual = costoVecino;
                // Actualizar la mejor solución si es necesario
                if (costoActual < mejorCosto) {
                    mejorSolucion = solucionActual;
                    mejorCosto = costoActual;
                }
            }
        }
        return mejorSolucion;
    }
}