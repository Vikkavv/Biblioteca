package Biblioteca.Vista;

public class Menus {

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
}
