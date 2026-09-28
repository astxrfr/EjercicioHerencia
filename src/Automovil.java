public class Automovil extends Vehiculo {
    private int cantidadDePasajeros;
    private boolean transmisionAutomatica;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int cantidadDePasajeros, boolean transmisionAutomatica) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cantidadDePasajeros = cantidadDePasajeros;
        this.transmisionAutomatica = transmisionAutomatica;
    }

    public int getCantidadDePasajeros() {
        return cantidadDePasajeros;
    }

    public boolean isTransmisionAutomatica() {
        return transmisionAutomatica;
    }

    @Override
    public double getTarifaDiaria() {
        return isTransmisionAutomatica() ? this.tarifaDiaria + 50 : this.tarifaDiaria;
    }

    @Override
    protected String getDetalles() {
        return "capacidad de pasajeros: " + cantidadDePasajeros +
                ", transmisión: " + (transmisionAutomatica?"automática":"manual");
    }
}
