import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.Period;
import java.io.*;
import java.util.InputMismatchException;

public class Juego {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("Bienvenido al juego de cartas por turnos.");
            System.out.println("1. Ver resultados de partidas anteriores");
            System.out.println("2. Eliminar todos los logs de partidas");
            System.out.println("3. Comenzar una nueva partida");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    mostrarPartidasAnteriores();
                    break;
                case 2:
                    eliminarLogs();
                    break;
                case 3:
                    iniciarNuevaPartida(scanner);
                    break;
                case 4:
                    salir = true;
                    System.out.println("Saliendo del juego. ¡Hasta la próxima!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
            }
        }
    }

    public static void iniciarNuevaPartida(Scanner scanner) {
        System.out.print("¿Desea ingresar las características de las cartas manualmente? ( si / no ): ");
        boolean manual = scanner.next().toLowerCase().equals("si");

        // Crear cartas para cada jugador
        List<Carta> cartasJugador1 = crearCartas(manual);
        List<Carta> cartasJugador2 = crearCartas(manual);

        Jugador jugador1 = new Jugador(cartasJugador1);
        Jugador jugador2 = new Jugador(cartasJugador2);

        // Iniciar partida
        jugar(jugador1, jugador2);
    }

    public static void mostrarPartidasAnteriores() {
        String nombreArchivo = "partidas_jugadas.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            System.out.println("Resultados de las partidas anteriores:");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de partidas anteriores o no existen partidas guardadas.");
        }
    }

    public static void eliminarLogs() {
        String nombreArchivo = "partidas_jugadas.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            System.out.println("\n Todos los logs de partidas han sido eliminados. \n");
        } catch (IOException e) {
            System.out.println("\n No se pudo eliminar el contenido del archivo de logs. \n");
        }
    }


    public static List<Carta> crearCartas(boolean manual) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<String> nombresPredeterminados = List.of("Tripto", "Morfur", "Xernes", "Khaan", "Jormun", "Delpho", "Mikesh",
                "Tork", "Ulumbar", "Sarsana", "Tylisk", "Khlel", "Daker", "Ermalin", "Nester", "Torlan");

        List<String> apodosPredeterminados = List.of("Programador", "Logico", "Fullstack", "Backend",
                "Frontend", "Investigador", "Resolutivo");

        List<Carta> cartas = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String nombre = manual ? pedirCadena("\nNombre de la nueva carta: ") : nombresPredeterminados.get(new Random().nextInt(nombresPredeterminados.size()));
            TipoCarta tipo = manual ? pedirTipoCarta() : TipoCarta.values()[new Random().nextInt(TipoCarta.values().length)];
            int velocidad = manual ? pedirEntero("Velocidad (1-10): ", 1, 10) : new Random().nextInt(10) + 1;
            int destreza = manual ? pedirEntero("Destreza (1-5): ", 1, 5) : new Random().nextInt(5) + 1;
            int fuerza = manual ? pedirEntero("Fuerza (1-10): ", 1, 10) : new Random().nextInt(10) + 1;
            int nivel = manual ? pedirEntero("Nivel (1-10): ", 1, 10) : new Random().nextInt(10) + 1;
            int armadura = manual ? pedirEntero("Armadura (1-10): ", 1, 10) : new Random().nextInt(10) + 1;
            String apodo = manual ? pedirCadena("Apodo: ") : apodosPredeterminados.get(new Random().nextInt(apodosPredeterminados.size()));
            LocalDate fechaNacimiento = manual ? pedirFecha("Fecha de nacimiento (dd/MM/yyyy): ")
                    : LocalDate.of(new Random().nextInt(40) + 1980, new Random().nextInt(12) + 1, new Random().nextInt(28) + 1);
            int edad = calcularEdad(fechaNacimiento);

            cartas.add(new Carta(nombre, tipo, velocidad, destreza, fuerza, nivel, armadura, apodo, fechaNacimiento, edad));
        }

        return cartas;
    }

    private static int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private static int pedirEntero(String mensaje, int min, int max) {
        int valor = 0;
        while (true) {
            try {
                System.out.print(mensaje);
                valor = scanner.nextInt();
                scanner.nextLine();
                if (valor < min || valor > max) {
                    throw new IllegalArgumentException("El valor debe estar entre " + min + " y " + max + ".");
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número. Intente de nuevo.");
                scanner.next(); // Limpiar el input inválido
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return valor;
    }

    private static String pedirCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static TipoCarta pedirTipoCarta() {
        TipoCarta tipo = null;
        while (true) {
            try {
                System.out.print("Tipo de carta (HUMANO, ORCO, ELFO): ");
                tipo = TipoCarta.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo no válido. Intente de nuevo.");
            }
        }
        return tipo;
    }

    private static LocalDate pedirFecha(String mensaje) {
        LocalDate fecha = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print(mensaje);
                String fechaStr = scanner.nextLine();
                fecha = LocalDate.parse(fechaStr, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use dd/MM/yyyy. Intente de nuevo.");
            }
        }
        return fecha;
    }


    public static void jugar(Jugador jugador1, Jugador jugador2) {
        StringBuilder log = new StringBuilder();
        int ronda = 1;

        System.out.println("\nCartas del Jugador 1:");
        log.append("Cartas del Jugador 1:\n");
        for (Carta carta : jugador1.getCartas()) {
            log.append(carta).append("\n");
            System.out.println(carta);
        }

        System.out.println("\nCartas del Jugador 2:");
        log.append("\nCartas del Jugador 2:\n");
        for (Carta carta : jugador2.getCartas()) {
            log.append(carta).append("\n");
            System.out.println(carta);
        }

        while (!jugador1.haPerdido() && !jugador2.haPerdido()) {
            log.append("\nRonda ").append(ronda).append("\n");
            System.out.println("\nRonda " + ronda);
            Carta carta1 = jugador1.obtenerCartaActual();
            Carta carta2 = jugador2.obtenerCartaActual();

            for (int turno = 0; turno < 7; turno++) {
                double dañoA2 = carta1.atacar(carta2);
                carta2.recibirDaño(dañoA2);
                String ataque1 = String.format("%s ataca a %s y le quita %.2f de salud. %s queda con %.2f de salud.%n",
                        carta1.getNombre(), carta2.getNombre(), dañoA2, carta2.getNombre(), carta2.getSalud());
                log.append(ataque1);
                System.out.print(ataque1);

                if (carta2.getSalud() <= 0) {
                    String muerte2 = String.format("La carta %s ha muerto.%n", carta2.getNombre());
                    log.append(muerte2);
                    System.out.print(muerte2);
                    carta1.mejorarSalud(10);
                    String mejora1 = String.format("La carta %s gana 10 de salud y queda con %.2f de salud.%n",
                            carta1.getNombre(), carta1.getSalud());
                    log.append(mejora1);
                    System.out.print(mejora1);
                    break;
                }

                double dañoA1 = carta2.atacar(carta1);
                carta1.recibirDaño(dañoA1);
                String ataque2 = String.format("%s ataca a %s y le quita %.2f de salud. %s queda con %.2f de salud.%n",
                        carta2.getNombre(), carta1.getNombre(), dañoA1, carta1.getNombre(), carta1.getSalud());
                log.append(ataque2);
                System.out.print(ataque2);

                if (carta1.getSalud() <= 0) {
                    String muerte1 = String.format("La carta %s ha muerto.%n", carta1.getNombre());
                    log.append(muerte1);
                    System.out.print(muerte1);
                    carta2.mejorarSalud(10);
                    String mejora2 = String.format("La carta %s gana 10 de salud y queda con %.2f de salud.%n",
                            carta2.getNombre(), carta2.getSalud());
                    log.append(mejora2);
                    System.out.print(mejora2);
                    break;
                }
            }

            if (carta1.getSalud() <= 0) {
                jugador1.siguienteCarta();
            }
            if (carta2.getSalud() <= 0) {
                jugador2.siguienteCarta();
            }

            ronda++;
        }

        if (jugador1.haPerdido()) {
            log.append("\nJugador 2 vence con ").append(jugador2.cartasRestantes()).append(" cartas restantes.\n");
            log.append("\n :\\\\\\\\ JUGADOR 2 SE SIENTA EN EL TRONO DE HIERRO ////////:\n");
            System.out.println("\n                        ==============");
            System.out.println("                      <////////\\\\\\\\\\\\\\\\>");
            System.out.println("<\\\\\\\\\\\\\\\\ JUGADOR 2 SE SIENTA EN EL TRONO DE HIERRO ////////>");
            System.out.println("                      <\\\\\\\\\\\\\\\\////////>");
            System.out.println("                        ==============\n");
        } else {
            log.append("\nJugador 1 vence con ").append(jugador1.cartasRestantes()).append(" cartas restantes.\n");
            log.append("\n :\\\\\\\\ JUGADOR 1 SE SIENTA EN EL TRONO DE HIERRO ////////:\n");
            System.out.println("\n                        ==============");
            System.out.println("                      <////////\\\\\\\\\\\\\\\\>");
            System.out.println("<\\\\\\\\\\\\\\\\ JUGADOR 1 SE SIENTA EN EL TRONO DE HIERRO ////////>");
            System.out.println("                      <\\\\\\\\\\\\\\\\////////>");
            System.out.println("                        ==============\n");
        }

        guardarLog(log.toString());
    }

    public static void guardarLog(String contenido) {
        String nombreArchivo = "partidas_jugadas.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            bw.write(contenido);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("No se pudo guardar el log de la partida.");
        }
    }
}