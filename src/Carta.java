import java.util.Random;
import java.time.LocalDate;

public class Carta {
    private String nombre;
    private TipoCarta tipo;
    private int velocidad;
    private int destreza;
    private int fuerza;
    private int nivel;
    private int armadura;
    private double salud;
    private Random random;
    private String apodo;
    private LocalDate fechaNacimiento;
    private int edad;
    //private double factorCansancio;

    public Carta(String nombre, TipoCarta tipo, int velocidad, int destreza, int fuerza, int nivel, int armadura,
                 String apodo, LocalDate fechaNacimiento, int edad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.velocidad = velocidad;
        this.destreza = destreza;
        this.fuerza = fuerza;
        this.nivel = nivel;
        this.armadura = armadura;
        this.salud = 100.0;
        this.random = new Random();
        this.apodo = apodo;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
    }

    public double getSalud() {
        return salud;
    }

    public void recibirDaño(double cantidad) {
        this.salud -= cantidad;
    }

    public void mejorarSalud(double cantidad) {
        this.salud += cantidad;
    }

    public double atacar(Carta oponente) {
        double PD = destreza * fuerza * nivel;
        double ED = random.nextDouble();
        if (ED == 0) {
            ED = 0.01;  // evito que el valor sea cero para ED
        }

        double VA = PD * ED;
        double PDD = oponente.armadura * oponente.velocidad;
        double dañoProvocado = ((VA * ED) - PDD) / 500 * 100;

        if (dañoProvocado < 0) {
            dañoProvocado = 0;  // no puede causar daño negativo
        }

        switch (tipo) {
            case ELFO:
                dañoProvocado *= 1.05;
                break;
            case ORCO:
                dañoProvocado *= 1.1;
                break;
            case HUMANO:
                break;
        }

        return dañoProvocado;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "~~~[nombre:" + nombre + ", tipo:" + tipo + ", velocidad:" + velocidad + ", destreza:" + destreza +
                ", fuerza:" + fuerza + ", nivel:" + nivel + ", armadura:" + armadura + ", salud:" + salud +
                ", fechaNacimiento:" + fechaNacimiento + ", edad:" + edad + "]~~~";
    }
}