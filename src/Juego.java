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

public class Juego {

    private Jugador jugador1;
    private Jugador jugador2;
    private StringBuilder log;

    public Juego(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.log = new StringBuilder();
    }

    public void iniciar() {
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

        int ronda = 1;
        while (!jugador1.haPerdido() && !jugador2.haPerdido()) {
            String rondaTexto = "\nRonda " + ronda + "\n";
            log.append(rondaTexto);
            System.out.println(rondaTexto);

            Carta carta1 = jugador1.obtenerCartaActual();
            Carta carta2 = jugador2.obtenerCartaActual();

            for (int turno = 0; turno < 7; turno++) {
                double dañoA2 = carta1.atacar(carta2);
                carta2.recibirDaño(dañoA2);
                String ataque1Texto = String.format("%s ataca a %s y le quita %.2f de salud. %s queda con %.2f de salud.%n",
                        carta1.getNombre(), carta2.getNombre(), dañoA2, carta2.getNombre(), carta2.getSalud());
                log.append(ataque1Texto);
                System.out.println(ataque1Texto);

                if (carta2.getSalud() <= 0) {
                    String muerte2Texto = String.format("La carta %s ha muerto.%n", carta2.getNombre());
                    log.append(muerte2Texto);
                    System.out.println(muerte2Texto);

                    carta1.mejorarSalud(10);
                    String mejora1Texto = String.format("La carta %s gana 10 de salud y queda con %.2f de salud.%n",
                            carta1.getNombre(), carta1.getSalud());
                    log.append(mejora1Texto);
                    System.out.println(mejora1Texto);
                    break;
                }

                double dañoA1 = carta2.atacar(carta1);
                carta1.recibirDaño(dañoA1);
                String ataque2Texto = String.format("%s ataca a %s y le quita %.2f de salud. %s queda con %.2f de salud.%n",
                        carta2.getNombre(), carta1.getNombre(), dañoA1, carta1.getNombre(), carta1.getSalud());
                log.append(ataque2Texto);
                System.out.println(ataque2Texto);

                if (carta1.getSalud() <= 0) {
                    String muerte1Texto = String.format("La carta %s ha muerto.%n", carta1.getNombre());
                    log.append(muerte1Texto);
                    System.out.println(muerte1Texto);

                    carta2.mejorarSalud(10);
                    String mejora2Texto = String.format("La carta %s gana 10 de salud y queda con %.2f de salud.%n",
                            carta2.getNombre(), carta2.getSalud());
                    log.append(mejora2Texto);
                    System.out.println(mejora2Texto);
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
            System.out.println("                        ==============");
        } else {
            log.append("\nJugador 1 vence con ").append(jugador1.cartasRestantes()).append(" cartas restantes.\n");
            log.append("\n :\\\\\\\\ JUGADOR 1 SE SIENTA EN EL TRONO DE HIERRO ////////:\n");
            System.out.println("\n                        ==============");
            System.out.println("                      <////////\\\\\\\\\\\\\\\\>");
            System.out.println("<\\\\\\\\\\\\\\\\ JUGADOR 1 SE SIENTA EN EL TRONO DE HIERRO ////////>");
            System.out.println("                      <\\\\\\\\\\\\\\\\////////>");
            System.out.println("                        ==============");
        }

        mostrarEstadoFinal();
        escribirLogEnArchivo();
    }

    private void escribirLogEnArchivo() {
        try (FileWriter writer = new FileWriter("log_partidas.txt", true)) {
            writer.write(log.toString());
            writer.write("\n\n");  // estetica para separar las partidas
        } catch (IOException e) {
            System.err.println("Error al escribir el log en el archivo: " + e.getMessage());
        }
    }

    private void mostrarEstadoFinal() {
        System.out.println("\nEstado final de las cartas del Jugador 1:");
        log.append("\nEstado final de las cartas del Jugador 1:\n");
        for (Carta carta : jugador1.getCartas()) {
            System.out.println(carta + (carta.getSalud() <= 0 ? " xxASESINADAxx" : " <<SOBREVIVIO>>"));
            log.append(carta).append(carta.getSalud() <= 0 ? " xxASESINADAxx" : " <<SOBREVIVIO>>").append("\n");
        }

        System.out.println("\nEstado final de las cartas del Jugador 2:");
        log.append("\nEstado final de las cartas del Jugador 2:\n");
        for (Carta carta : jugador2.getCartas()) {
            System.out.println(carta + (carta.getSalud() <= 0 ? " xxASESINADAxx" : " <<SOBREVIVIO>>"));
            log.append(carta).append(carta.getSalud() <= 0 ? " xxASESINADAxx" : " <<SOBREVIVIO>>").append("\n");
        }
    }

    public static void borrarLog() {
        try (FileWriter writer = new FileWriter("log_partidas.txt")) {
            writer.write("");
            System.out.println("Se elimino el historial de partidas");
        } catch (IOException e) {
            System.err.println("Error al borrar el log del archivo: " + e.getMessage());
        }
    }

    public static List<Carta> crearCartas(boolean manual) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<String> nombresPredeterminados = List.of("Tripto", "Morfur", "Xernes", "Khaan", "Jormun", "Delpho", "Mikesh",
                "Tork", "Ulumbar", "Sarsana", "Tylisk", "Khlel", "Daker", "Ermalin",
                "Nester", "Torlan");
        List<String> apodosPredeterminados = List.of("Programador", "Logico", "Fullstack", "Backend",
                "Frontend", "Investigador", "Resolutivo");
        List<Carta> cartas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < 3; i++) {
            String nombre, apodo;
            TipoCarta tipo = null;
            int velocidad, destreza, fuerza, nivel, armadura, edad;
            LocalDate fechaNacimiento = null;

            if (manual) {
                System.out.println("Ingrese las características para la carta " + (i + 1) + ":");

                System.out.print("Nombre: ");
                nombre = scanner.next();

                System.out.print("Apodo: ");
                apodo = scanner.next();

                boolean tipoValido = false;
                while (!tipoValido) {
                    System.out.print("Tipo (HUMANO, ORCO, ELFO): ");
                    try {
                        tipo = TipoCarta.valueOf(scanner.next().toUpperCase());
                        tipoValido = true; // Si se ingresa un tipo válido, salimos del bucle
                    } catch (IllegalArgumentException e) {
                        System.out.println("Tipo de raza aun no disponible para tu nivel o inexistente, ingrese HUMANO, ORCO, o ELFO.");
                    }
                }

                velocidad = obtenerValorValido(scanner, "Velocidad (1-10): ", 1, 10);
                destreza = obtenerValorValido(scanner, "Destreza (1-5): ", 1, 5);
                fuerza = obtenerValorValido(scanner, "Fuerza (1-10): ", 1, 10);
                nivel = obtenerValorValido(scanner, "Nivel (1-10): ", 1, 10);
                armadura = obtenerValorValido(scanner, "Armadura (1-10): ", 1, 10);

                boolean fechaValida = false;
                while (!fechaValida) {
                    try {
                        System.out.print("Fecha de nacimiento (dia/mes/año): ");
                        String fecha = scanner.next();
                        fechaNacimiento = LocalDate.parse(fecha, formatter);
                        fechaValida = true; // Si la fecha es válida, salimos del bucle
                    } catch (DateTimeParseException e) {
                        System.out.println("Fecha no valida, ingrese la fecha en el formato dd/mm/yyyy.");
                    }
                }

                edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

            } else {
                nombre = nombresPredeterminados.get(random.nextInt(nombresPredeterminados.size()));
                tipo = TipoCarta.values()[random.nextInt(TipoCarta.values().length)];
                apodo = apodosPredeterminados.get(random.nextInt(apodosPredeterminados.size()));
                velocidad = random.nextInt(10) + 1;
                destreza = random.nextInt(5) + 1;
                fuerza = random.nextInt(10) + 1;
                nivel = random.nextInt(10) + 1;
                armadura = random.nextInt(10) + 1;
                fechaNacimiento = LocalDate.of(random.nextInt(40) + 1980, random.nextInt(12) + 1, random.nextInt(28) + 1);
                edad = 2024 - fechaNacimiento.getYear();
            }

            cartas.add(new Carta(nombre, tipo, velocidad, destreza, fuerza, nivel, armadura, apodo, fechaNacimiento, edad));
        }

        return cartas;
    }

    private static int obtenerValorValido(Scanner scanner, String mensaje, int min, int max) {
        int valor;
        do {
            System.out.print(mensaje);
            valor = scanner.nextInt();
            if (valor < min || valor > max) {
                System.out.println("Valor invalido. Debe estar entre " + min + " y " + max + ".");
            }
        } while (valor < min || valor > max);
        return valor;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n -)) BIENVENIDO A UNA NUEVA AVENTURA ((-  \n");
        
        System.out.print("Desea mantener el historial de las partidas anteriores? ( si / no ): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("no")) {
            borrarLog();
        }

        System.out.print("Desea ingresar las caracteristicas y datos de las cartas manualmente? ( si / no ): ");
        respuesta = scanner.nextLine();
        boolean manual = respuesta.equalsIgnoreCase("si");

        List<Carta> cartasJugador1 = crearCartas(manual);
        List<Carta> cartasJugador2 = crearCartas(manual);

        Jugador jugador1 = new Jugador(cartasJugador1);
        Jugador jugador2 = new Jugador(cartasJugador2);

        Juego juego = new Juego(jugador1, jugador2);
        juego.iniciar();
    }
}