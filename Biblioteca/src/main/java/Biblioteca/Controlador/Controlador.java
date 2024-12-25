package Biblioteca.Controlador;

import Biblioteca.Modelo.*;
import Biblioteca.Vista.Consola;
import Biblioteca.Vista.Menus;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controlador {

    //INICIO DE SESIÓN

    public static Usuario validaCredenciales(String email, String password) throws BibliotecaException {
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        Usuario usuario = null;
        try{
            if(!validadorEmail(email)) throw new BibliotecaException("Escribe un email con un formato válido ej: example@example.example");
            usuario = daoUsuario.findByUniqueValue("email", email);
        }catch (jakarta.persistence.NoResultException e){
            throw new BibliotecaException("El usuario no existe");
        }
        if(usuario != null){
            if(usuario.getPassword().equals(password)){
                return usuario;
            }
            throw new BibliotecaException("La contraseña es incorrecta");
        }
        return usuario;
    }

    public static boolean validadorEmail(String email){
        Pattern regularExp = Pattern.compile("[a-zA-Z0-9._]+@[a-zA-Z]+(([.][a-z]+)*)[.][a-z]{2,}");
        Matcher emailToCheck = regularExp.matcher(email);
        return emailToCheck.matches();
    }

    public static boolean validadorDNI(String dni) throws BibliotecaException {
        if(dni.length() != 9) throw new BibliotecaException("El DNI debe tener 9 caracteres");
        Pattern regularExp2 = Pattern.compile("[0-9]{8}[A-Za-z]");
        Matcher dniToCheck = regularExp2.matcher(dni);
        if(dniToCheck.matches()){
            String dniLetters = "TRWAGMYFPDXBNJZSQVHLCKE";
            String dniParamLetter = dni.substring(dni.length()-1);
                dni = dni.substring(0, dni.length()-1);
            int module = Integer.parseInt(dni) % 23;
            return Character.toString(dniLetters.charAt(module)).equalsIgnoreCase(dniParamLetter);
        }
        else return false;
    }

    //MENUS

    public static String selectorDeMenus(Usuario usuario) {
        if(usuario.getTipo().equals("administrador")){
            return Menus.menuPrincipalAd();
        }
        if(usuario.getTipo().equals("normal")){
            return Menus.menuPrincipalNor();
        }
        return null;
    }

    public static void opcionesMenuPrincipal(Usuario usuario, int opcion) throws BibliotecaException {
        if(!usuario.getTipo().equals("normal")){
            switch (opcion) {
                case 1: Consola.gestionLibros(); break;
                case 2: Consola.gestionEjemplares(); break;
                case 3: Consola.gestionUsuarios(); break;
                case 4: Consola.gestionPrestamos(); break;
                case 5: Consola.visualizacionPrestamos(); break;
                case 6: System.exit(0); break;
                default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones");
            }
        }
        else{
            switch (opcion) {
                case 1: Consola.visualizacionPrestamos(); break;
                case 2: System.exit(0); break;
                default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones");
            }
        }
    }

    public static Map<Integer, String> menuLibros(){
        String menu = "";
        DAO<Libro, String> daoLibro = new DAO<>(Libro.class, String.class);
        DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);
        List<Libro> libros = daoLibro.findAll();
        Map<Integer, String> librosMap = new HashMap<>();
        for (int i = 0; i < libros.size(); i++) {
            librosMap.put(i+1, libros.get(i).getIsbn());
            List<Ejemplar> ejemplares = daoEjemplar.findAllWhere("e.isbn.isbn", libros.get(i).getIsbn());
            int stock = 0;
            for (Ejemplar ejemplar : ejemplares) {
                if(ejemplar.getEstado().equalsIgnoreCase("Disponible")){
                    stock++;
                }
            }
            menu += String.format("| %s. %s   Stock: %s\n", i+1, libros.get(i).getTitulo(), stock);
        }
        menu += String.format("-----------\n| %s. Salir", libros.size()+1);
        librosMap.put(libros.size()+1, "----------------salir-----------------");
        librosMap.put(0, menu);
        return librosMap;
    }

    public static Map<Integer, String> menuUsuarios() {
        String menu = "";
        Map<Integer, String> usuariosMap = new HashMap<>();
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        List<Usuario> usuarios = daoUsuario.findAll();
        for (int i = 0; i < usuarios.size(); i++) {
            String penalizacion = "";
            if(usuarios.get(i).getPenalizacionHasta() != null){
                if(usuarios.get(i).getPenalizacionHasta().isAfter(LocalDate.now())){
                    int daysRemain = (int) LocalDate.now().datesUntil(usuarios.get(i).getPenalizacionHasta()).count();
                    String plural = daysRemain > 1 ? "s" : "";
                    penalizacion = String.format(" -Penalización hasta: %s (%s día%s)",usuarios.get(i).getPenalizacionHasta(), daysRemain, plural);
                }else{
                    usuarios.get(i).setPenalizacionHasta(null);
                    daoUsuario.update(usuarios.get(i));
                }
            }
            usuariosMap.put(i+1, usuarios.get(i).getEmail());
            menu += String.format("| %s. %s%s\n", i+1, usuarios.get(i).getNombre(), penalizacion);
        }
        menu += String.format("-----------\n| %s. Salir", usuarios.size()+1);
        usuariosMap.put(usuarios.size()+1, "----------------salir-----------------");
        usuariosMap.put(0, menu);
        return usuariosMap;
    }

    public static Map<Integer, String> menuEjemplaresDeUnUsuario(Usuario usuario) {
        String menu = "";
        DAO<Prestamo, Integer> daoPrestamo = new DAO<>(Prestamo.class, Integer.class);
        List<Prestamo> prestamos = daoPrestamo.findAllWhere("e.usuario.id", usuario.getId());
        Map<Integer, String> prestamosMap = new HashMap<>();
        int contador = 1;
        for (Prestamo prestamo : prestamos){
            if(prestamo.getFechaDevolucion() == null){
                prestamosMap.put(contador, Integer.toString(prestamo.getId()));
                menu += String.format("| %s. %s\n", contador, prestamo.getEjemplar().getIsbn().getTitulo());
                contador++;
            }
        }
        menu += String.format("-----------\n| %s. Salir", contador);
        prestamosMap.put(contador, "----------------salir-----------------");
        prestamosMap.put(0, menu);
        return prestamosMap;
    }

    //GESTIONES

    public static void gestionLibros(int opcion) throws BibliotecaException {
        switch (opcion){
            case 1: Consola.registrarLibro(); break;
            case 2: Consola.menuPrincipal(); break;
            default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones.");
        }
    }

    public static void gestionEjemplares(int opcion) throws BibliotecaException {
        switch (opcion){
            case 1: Consola.registrarEjemplar(); break;
            case 2: Consola.menuPrincipal(); break;
            default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones.");
        }
    }

    public static void gestionUsuarios(int opcion) throws BibliotecaException {
        switch (opcion){
            case 1: Consola.registrarUsuario(); break;
            case 2: Consola.menuPrincipal(); break;
            default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones.");
        }
    }

    public static void gestionPrestamos(int opcion) throws BibliotecaException {
        switch (opcion){
            case 1: Consola.registrarPrestamo(); break;
            case 2: Consola.gestionDevolucion(); break;
            case 3: Consola.menuPrincipal(); break;
            default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones.");
        }
    }

    public static void gestionDevolucion(int opcion) throws BibliotecaException {
        switch (opcion){
            case 1: Consola.registrarDevolucion(); break;
            case 2: Consola.gestionPrestamos(); break;
            default: throw new BibliotecaException("Introduce únicamente números que esten comprendidos en las opciones.");
        }
    }

    //REGISTROS

    public static void registrarLibro(Libro libro) throws BibliotecaException {
        DAO<Libro, String> daoLibro = new DAO<>(Libro.class, String.class);
        if(!validarIsbn(libro.getIsbn())) throw new BibliotecaException("El isbn no es correcto.");
        daoLibro.add(libro);
    }

    public static void registrarEjemplar(String isbn, String estado) {
        DAO<Libro, Integer> daoLibro = new DAO<>(Libro.class, Integer.class);
        DAO<Ejemplar, String> daoEjemplar = new DAO<>(Ejemplar.class, String.class);
        Ejemplar ejemplar = new Ejemplar(daoLibro.findByUniqueValue("isbn", isbn), estado);
        daoEjemplar.add(ejemplar);
    }

    public static void registrarUsuario(Usuario usuario) throws BibliotecaException {
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        if(!validadorEmail(usuario.getEmail())) throw new BibliotecaException("Escribe un email con un formato válido ej: example@example.example.");
        if(!validadorDNI(usuario.getDni())) throw new BibliotecaException("El DNI introducido no es válido, debe de tener 8 digitos y una letra válida.");
        else usuario.setDni(usuario.getDni().toUpperCase());
        daoUsuario.add(usuario);
    }

    public static void registrarPrestamo(Ejemplar ejemplar, Usuario usuario) throws BibliotecaException {
        DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);
        DAO<Prestamo, Integer> daoPrestamo = new DAO<>(Prestamo.class, Integer.class);
        List<Prestamo> currentLends = new ArrayList<>();
        if(usuario.getPenalizacionHasta() != null){
            int daysRemain = (int) LocalDate.now().datesUntil(usuario.getPenalizacionHasta()).count();
            String plural = daysRemain > 1 ? "s" : "";
            throw new BibliotecaException(String.format("El usuario tiene una penalización,\npor lo que no puede solicitar nuevos\nprestamos hasta: %s (%s día%s)",usuario.getPenalizacionHasta(), daysRemain, plural));
        }
        for (Prestamo prestamo : daoPrestamo.findAllWhere("e.usuario.id", usuario.getId())){
            if(prestamo.getFechaDevolucion() == null){
                currentLends.add(prestamo);
                if(prestamo.equals(new Prestamo(usuario, ejemplar, null))) throw new BibliotecaException("El usuario ya tiene un prestamo activo\ncon un ejemplar del mismo libro");
            }
        }
        if(currentLends.size() < 3 ){
            ejemplar.setEstado("Prestado");
            daoEjemplar.update(ejemplar);
            Prestamo prestamo = new Prestamo(usuario,ejemplar, LocalDate.now());
            daoPrestamo.add(prestamo);
        }
        else throw new BibliotecaException("El usuario ya tiene 3 prestamos activos");
    }

    public static void registrarDevolucion(Usuario usuario, Prestamo prestamo) {
        DAO<Prestamo, Integer> daoPrestamo = new DAO<>(Prestamo.class, Integer.class);
        DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        LocalDate fechaDefin = prestamo.getFechaInicio().plusDays(15);
        LocalDate fechaDevolucion = LocalDate.now()/*.plusDays(17)*/;
        if(usuario.getPenalizacionHasta() != null){
            if(LocalDate.now().minusDays(1).isAfter(usuario.getPenalizacionHasta())){
                usuario.setPenalizacionHasta(null);
            }
        }
        if(fechaDevolucion.isAfter(fechaDefin) || prestamo.getEjemplar().getEstado().equals("Dañado")){
            if(usuario.getPenalizacionHasta() != null){
                usuario.setPenalizacionHasta(usuario.getPenalizacionHasta().plusDays(15));
            }else usuario.setPenalizacionHasta(LocalDate.now().plusDays(15));
            daoUsuario.update(usuario);
        }
        daoEjemplar.update(prestamo.getEjemplar());
        prestamo.setFechaDevolucion(fechaDevolucion);
        daoPrestamo.update(prestamo);
    }

    //VARIOS

    public static Usuario seleccionarUsuario(String email){
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        return daoUsuario.findByUniqueValue("email", email);
    }

    public static boolean checkExit(String exit) {
        return exit.equalsIgnoreCase("----------------salir-----------------");
    }

    public static Ejemplar atribuirEjemplar(String isbn) {
        DAO<Libro, String> daoLibro = new DAO<>(Libro.class, String.class);
        DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);
        Libro libro = daoLibro.findByUniqueValue("isbn", isbn);
        List<Ejemplar> ejemplares = daoEjemplar.findAllWhere("e.isbn.isbn", libro.getIsbn());
        Ejemplar ejemplarPrestado = null;
        for (Ejemplar ejemplar : ejemplares) {
            if(ejemplar.getEstado().equalsIgnoreCase("Disponible")){
                ejemplarPrestado = ejemplar;
            }
        }
        return ejemplarPrestado;
    }

    public static Prestamo seleccionarPrestamo(Integer idPrestamo ) {
        DAO<Prestamo, Integer> daoPrestamo = new DAO<>(Prestamo.class, Integer.class);
        return daoPrestamo.findById(idPrestamo);
    }

    private static boolean validarIsbn(String isbn) throws BibliotecaException {
        if(!(isbn.startsWith("978")) && !(isbn.startsWith("979"))){
            throw new BibliotecaException("El ISBN no es valido, los ISBN de libros deben empezar por 978 o 979");
        }
        if (isbn.length() != 13) throw new BibliotecaException("El ISBN tiene que tener 13 dígitos");
        int suma = 0;
        for (int i = 0; i < isbn.length()-1; i++) {
            if(((i+1) % 2 == 1)) suma += Integer.parseInt(Character.toString(isbn.charAt(i)));
            else suma += Integer.parseInt(Character.toString(isbn.charAt(i))) * 3;
        }
        return Integer.parseInt(Character.toString(isbn.charAt(isbn.length()-1))) == ((10 - (suma % 10)) % 10);
    }
}
