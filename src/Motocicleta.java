public class Motocicleta extends Vehiculo{
    private double cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, double cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cilindraje = cilindraje;
    }

    public double getCilindraje() {
        return cilindraje;
    }

    @Override
    public double getTarifaTotal(int dias) {
        return this.cilindraje > 250 ? super.getTarifaTotal(dias) + 75 : super.getTarifaTotal(dias);
    }

    @Override
    protected String getDetalles() {
        return "cilindraje: " + cilindraje;
    }
}
