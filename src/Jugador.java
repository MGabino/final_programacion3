import java.util.List;

public class Jugador {
    private List<Carta> cartas;
    private int cartaActual;

    public Jugador(List<Carta> cartas) {
        this.cartas = cartas;
        this.cartaActual = 0;
    }

    public boolean haPerdido() {
        return cartaActual >= cartas.size();
    }

    public Carta obtenerCartaActual() {
        if (haPerdido()) {
            return null;
        }
        return cartas.get(cartaActual);
    }

    public void siguienteCarta() {
        cartaActual++;
    }

    public int cartasRestantes() {
        return cartas.size() - cartaActual;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    @Override
    public String toString() {
        return "Jugador [cartas restantes=" + cartasRestantes() + "]";
    }
}