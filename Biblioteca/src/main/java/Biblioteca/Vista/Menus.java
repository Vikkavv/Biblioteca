package Biblioteca.Vista;

public class Menus {

    //MENUS PRINCIPALES

    public static String menuIniciarSesion() {
        return """
                ####################
                |  Iniciar Sesión  |
                ####################
                Correo electronico:""";
    }

    public static String menuPrincipalAd() {
        return """
                ############################
                |        Biblioteca        |
                ############################
                | 1. Gestionar Libros      |
                | 2. Gestionar Ejemplares  |
                | 3. Gestionar Usuarios    |
                | 4. Gestionar Prestamos   |
                | 5. Visualizar Prestamos  |
                | 6. Salir del programa    |
                ############################""";
    }

    public static String menuPrincipalNor() {
        return """
                ############################
                |        Biblioteca        |
                ############################
                | 1. Visualizar Prestamos  |
                | 2. Salir del programa    |
                ############################""";
    }

    //GESTIONES

    public static String gestionLibros(){
        return """
                ############################
                |      Gestión Libros      |
                ############################
                | 1. Registrar libro       |
                | 2. Volver atrás          |
                ############################""";
    }

    public static String gestionEjemplares(){
        return """
                ############################
                |    Gestión Ejemplares    |
                ############################
                | 1. Registrar ejemplar    |
                | 2. Volver atrás          |
                ############################
                """;
    }

    public static String gestionUsuarios(){
        return """
                ##############################
                |      Gestión Usuarios      |
                ##############################
                | 1. Registrar usuario       |
                | 2. Volver atrás            |
                ##############################
                """;
    }

    public static String gestionPrestamos() {
        return """
                ###############################
                |      Gestión Prestamos      |
                ###############################
                | 1. Registrar prestamo       |
                | 2. Gestión devoluciones     |
                | 3. Volver atrás             |
                ###############################
                """;
    }

    public static String gestionDevolucion() {
        return """
                ################################
                |     Gestión Devoluciones     |
                ################################
                | 1. Registrar devolución      |
                | 2. Volver atrás              |
                ################################
                """;
    }

    //REGISTROS

    public static String registrarLibro(){
        return """
                #############################
                |      Registrar libro      |
                #############################
                ISBN: """;
    }

    public static String registrarEjemplar(){
        return """
                ##############################
                |     Registrar ejemplar     |
                ##############################""";
    }

    public static String registrarUsuario() {
        return """
                #############################
                |     Registrar usuario     |
                #############################
                DNI: """;
    }

    public static String registrarPrestamo() {
        return """
                ##############################
                |     Registrar Prestamo     |
                ##############################
                Elige usuario al que
                conceder el prestamo: """;
    }

    public static String registrarDevolucion() {
        return """
                ##############################
                |    Registrar Devolución    |
                ##############################
                Elige usuario que
                efectua la devolución: """;
    }

    //VARIOS

    public static String visualizarPrestamos() {
        return """
                ##############################
                |    Visualizar prestamos    |
                ##############################""";
    }

    public static String elegirPrestamo(String nombre) {
        String menu = "";
        String separador = "";
        String header = String.format("|    Prestamos de %s    |", nombre);
        for (int i = 0; i < header.length(); i++) {
            separador += "#";
        }
        menu += separador + "\n" + header + "\n" + separador + "\nElige el ejemplar a devolver:";
        return menu;
    }
}
