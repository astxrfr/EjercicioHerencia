
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

public abstract class Vehiculo {
    private static final Set<String> REGISTRO_PLACAS = new HashSet<>();

    private String placa;
    private String marca;
    private String modelo;

    protected double tarifaDiaria;
    protected boolean disponible;

    public Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        if (REGISTRO_PLACAS.contains(placa)) throw new InvalidParameterException("Field 'placa' cannot be duplicate");
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;
    }

    public Vehiculo rentar() {
        this.disponible = false;
        return this;
    }

    public Vehiculo liberar() {
        this.disponible = true;
        return this;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public double getTarifaTotal(int dias) {
        return getTarifaDiaria() * dias;
    }

    public boolean isDisponible() {
        return disponible;
    }

    protected abstract String getDetalles();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                ": placa: " + placa +
                ", marca: " + marca +
                ", modelo: " + modelo +
                ", tarifa diaria: " + tarifaDiaria +
                ", disponible: " + (disponible?"si":"no") + ", " +
                this.getDetalles();
    }
}
