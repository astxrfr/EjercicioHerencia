private static final Scanner in = new Scanner(System.in);

void main() {
    RentaMovil empresa = new RentaMovil();
    cargarDatosIniciales(empresa);

    boolean salir = false;
    while (!salir) {
        mostrarMenu();
        int opcion = leerEntero("Opción: ");
        salir = ejecutarOpcion(opcion, empresa);
    }
    IO.println("Hasta luego.");
}

private static void mostrarMenu() {
    IO.println();
    IO.println("===== RentaMovil =====");
    IO.println("1. Registrar vehículo");
    IO.println("2. Consultar flota");
    IO.println("3. Cotizar alquiler");
    IO.println("4. Alquilar vehículo");
    IO.println("5. Registrar devolución");
    IO.println("6. Reporte general");
    IO.println("7. Salir");
}

private static boolean ejecutarOpcion(int opcion, RentaMovil empresa) {
    switch (opcion) {
        case 1:
            registrarVehiculo(empresa);
            break;
        case 2:
            consultarFlota(empresa);
            break;
        case 3:
            cotizarAlquiler(empresa);
            break;
        case 4:
            alquilarVehiculo(empresa);
            break;
        case 5:
            registrarDevolucion(empresa);
            break;
        case 6:
            mostrarReporte(empresa);
            break;
        case 7:
            return true;
        default:
            IO.println("Opción inválida. Elija un número del 1 al 7.");
    }
    return false;
}

private static void registrarVehiculo(RentaMovil empresa) {
    IO.println("\n--- Registrar vehículo ---");
    IO.println("Categoría: 1. Automóvil  2. Motocicleta  3. Camioneta de carga");
    int categoria = leerEntero("Categoría: ");
    if (categoria < 1 || categoria > 3) {
        IO.println("Categoría inválida. No se registró nada.");
        return;
    }

    String placa = leerTexto("Placa: ");
    if (placa.isEmpty()) {
        IO.println("La placa no puede estar vacía.");
        return;
    }
    if (empresa.existe(placa)) {
        IO.println("Ya existe un vehículo con la placa " + placa + ".");
        return;
    }

    String marca = leerTexto("Marca: ");
    String modelo = leerTexto("Modelo: ");
    double tarifa = leerDoublePositivo("Tarifa diaria (Q): ");

    try {
        Vehiculo v;
        if (categoria == 1) {
            v = leerAutomovil(placa, marca, modelo, tarifa);
        } else if (categoria == 2) {
            v = leerMotocicleta(placa, marca, modelo, tarifa);
        } else {
            v = leerCamioneta(placa, marca, modelo, tarifa);
        }
        empresa.registrar(v);
        IO.println("Vehículo registrado y disponible: " + placa);
    } catch (IllegalArgumentException e) {
        IO.println("No se pudo registrar: " + e.getMessage());
    }
}

private static void consultarFlota(RentaMovil empresa) {
    IO.println("\n--- Flota ---");
    System.out.println(empresa.listarFlota());
}

private static void cotizarAlquiler(RentaMovil empresa) {
    IO.println("\n--- Cotizar alquiler ---");
    Vehiculo v = pedirVehiculo(empresa);
    if (v == null) return;

    int dias = leerEnteroPositivo("Días de alquiler: ");
    System.out.println(empresa.cotizar(v.getPlaca(), dias));
}

private static void alquilarVehiculo(RentaMovil empresa) {
    IO.println("\n--- Alquilar vehículo ---");
    Vehiculo v = pedirVehiculo(empresa);
    if (v == null) return;

    if (!v.isDisponible()) {
        IO.println("El vehículo " + v.getPlaca() + " está ocupado. No se puede alquilar.");
        return;
    }

    int dias = leerEnteroPositivo("Días de alquiler: ");
    System.out.println(empresa.cotizar(v.getPlaca(), dias));

    if (!confirmar("¿Confirmar el alquiler? (s/n): ")) {
        IO.println("Alquiler cancelado. Nada fue modificado.");
        return;
    }

    try {
        double monto = empresa.alquilar(v.getPlaca(), dias);
        System.out.printf("Alquiler confirmado. Total cobrado: Q%.2f%n", monto);
    } catch (IllegalStateException | IllegalArgumentException e) {
        IO.println("No se pudo alquilar: " + e.getMessage());
    }
}

private static void registrarDevolucion(RentaMovil empresa) {
    IO.println("\n--- Registrar devolución ---");
    String placa = leerTexto("Placa: ");
    try {
        empresa.devolver(placa);
        IO.println("Devolución registrada. El vehículo " + placa + " está disponible.");
    } catch (IllegalStateException | IllegalArgumentException e) {
        IO.println("No se pudo registrar la devolución: " + e.getMessage());
    }
}

private static void mostrarReporte(RentaMovil empresa) {
    IO.println("\n--- Reporte general ---");
    System.out.println(empresa.reporte());
}

private static Vehiculo leerAutomovil(String placa, String marca, String modelo, double tarifa) {
    int pasajeros = leerEnteroPositivo("Cantidad de pasajeros: ");
    boolean automatico = confirmar("¿Transmisión automática? (s/n): ");
    return new Automovil(placa, marca, modelo, tarifa, pasajeros, automatico);
}

private static Vehiculo leerMotocicleta(String placa, String marca, String modelo, double tarifa) {
    double cilindraje = leerDoublePositivo("Cilindraje (cc): ");
    return new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
}

private static Vehiculo leerCamioneta(String placa, String marca, String modelo, double tarifa) {
    double capacidad = leerDoublePositivo("Capacidad máxima (toneladas): ");
    return new CamionetaDeCarga(placa, marca, modelo, tarifa, capacidad);
}

private static void cargarDatosIniciales(RentaMovil empresa) {

    empresa.registrar(new Automovil("P101ABC", "Toyota", "Corolla", 250.00, 5, true));
    empresa.registrar(new Automovil("P102DEF", "Honda", "Fit", 180.00, 5, false));

    empresa.registrar(new Motocicleta("M201GHI", "Yamaha", "FZ150", 90.00, 150));
    empresa.registrar(new Motocicleta("M202JKL", "Kawasaki", "Ninja 300", 140.00, 300));

    empresa.registrar(new CamionetaDeCarga("C301MNO", "Isuzu", "NHR", 200.00, 1.5));
    empresa.registrar(new CamionetaDeCarga("C302PQR", "Hino", "300", 320.00, 3.0));
}

private static Vehiculo pedirVehiculo(RentaMovil empresa) {
    String placa = leerTexto("Placa: ");
    if (!empresa.existe(placa)) {
        IO.println("No existe un vehículo con la placa " + placa + ".");
        return null;
    }
    return empresa.buscar(placa);
}

private static String leerTexto(String mensaje) {
    IO.print(mensaje);
    return in.nextLine().trim();
}

private static int leerEntero(String mensaje) {
    while (true) {
        try {
            return Integer.parseInt(leerTexto(mensaje));
        } catch (NumberFormatException e) {
            IO.println("Entrada inválida. Ingrese un número entero.");
        }
    }
}

private static int leerEnteroPositivo(String mensaje) {
    while (true) {
        int valor = leerEntero(mensaje);
        if (valor > 0) return valor;
        IO.println("El valor debe ser un entero mayor que cero.");
    }
}

private static double leerDoublePositivo(String mensaje) {
    while (true) {
        try {
            double valor = Double.parseDouble(leerTexto(mensaje));
            if (valor > 0) return valor;
            IO.println("El valor debe ser mayor que cero.");
        } catch (NumberFormatException e) {
            IO.println("Entrada inválida. Ingrese un número (ej. 1.5).");
        }
    }
}

private static boolean confirmar(String mensaje) {
    while (true) {
        String r = leerTexto(mensaje).toLowerCase();
        if (r.equals("s") || r.equals("si") || r.equals("sí")) return true;
        if (r.equals("n") || r.equals("no")) return false;
        IO.println("Responda con 's' o 'n'.");
    }
}