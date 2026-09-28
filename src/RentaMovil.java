import java.util.LinkedHashMap;
import java.util.Map;

public class RentaMovil {

    private final Map<String, Vehiculo> flota = new LinkedHashMap<>();
    private double ingresos = 0;

    private static String clave(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }

    public void registrar(Vehiculo v) {
        if (v == null) throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        String clave = clave(v.getPlaca());
        if (flota.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa " + v.getPlaca() + ".");
        }
        flota.put(clave, v);
    }

    public boolean existe(String placa) {
        return flota.containsKey(clave(placa));
    }

    public Vehiculo buscar(String placa) {
        Vehiculo v = flota.get(clave(placa));
        if (v == null) throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        return v;
    }

    public String listarFlota() {
        if (flota.isEmpty()) return "No hay vehículos registrados.";
        StringBuilder sb = new StringBuilder();
        for (Vehiculo v : flota.values()) {
            sb.append(v).append('\n');
        }
        return sb.toString().trim();
    }

    public String cotizar(String placa, int dias) {
        validarDias(dias);
        Vehiculo v = buscar(placa);
        return String.format("Cotización por %d día(s):%n%s%nTotal: Q%.2f", dias, v, v.getTarifaTotal(dias));
    }

    public double alquilar(String placa, int dias) {
        validarDias(dias);
        Vehiculo v = buscar(placa);
        if (!v.isDisponible()) {
            throw new IllegalStateException("El vehículo " + v.getPlaca() + " está ocupado.");
        }
        double monto = v.getTarifaTotal(dias);
        v.rentar();
        ingresos += monto;
        return monto;
    }

    public void devolver(String placa) {
        Vehiculo v = buscar(placa);
        if (v.isDisponible()) {
            throw new IllegalStateException("El vehículo " + v.getPlaca() + " no está alquilado.");
        }
        v.liberar();
    }

    public double getIngresos() {
        return ingresos;
    }

    public String reporte() {
        Map<Class<?>, int[]> porCategoria = new LinkedHashMap<>(); // {clase, {disponibles, alquilados}}
        int disponibles = 0;
        for (Vehiculo v : flota.values()) {
            var c = porCategoria.computeIfAbsent(v.getClass(), k -> new int[2]);
            if (v.isDisponible()) {
                c[0]++;
                disponibles++;
            } else {
                c[1]++;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Vehículos registrados: ").append(flota.size()).append('\n');
        sb.append("Disponibles: ").append(disponibles).append('\n');
        sb.append("Alquilados: ").append(flota.size() - disponibles).append('\n');
        for (Map.Entry<Class<?>, int[]> e : porCategoria.entrySet()) {
            sb.append(String.format("  %s -> disponibles: %d, alquilados: %d%n",
                    e.getKey().getSimpleName(), e.getValue()[0], e.getValue()[1]));
        }
        sb.append(String.format("Ingresos acumulados: Q%.2f", ingresos));
        return sb.toString();
    }

    private void validarDias(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser un entero positivo.");
    }
}
