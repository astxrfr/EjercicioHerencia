public class CamionetaDeCarga extends Vehiculo{
    private double capacidad;

    public CamionetaDeCarga(String placa, String marca, String modelo, double tarifaDiaria, double capacidad) {
        super(placa, marca, modelo, tarifaDiaria);
        this.capacidad = capacidad;
    }

    public double getCapacidad() {
        return capacidad;
    }

    @Override
    public double getTarifaDiaria() {
        return (capacidad*100) + tarifaDiaria;
    }

    @Override
    protected String getDetalles() {
        return "capacidad de carga: " + capacidad;
    }
}
