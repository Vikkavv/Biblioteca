package Biblioteca.Controlador;

import Biblioteca.Modelo.DAO;
import Biblioteca.Modelo.Ejemplar;
import Biblioteca.Modelo.Libro;
import Biblioteca.Modelo.Usuario;
import Biblioteca.Vista.Consola;
import Biblioteca.Vista.Menus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controlador {

    //INICIO DE SESIÓN

    public static Usuario validaCredenciales(String email, String password) throws BibliotecaException {
        DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
        Usuario usuario = null;
        try{
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

    public static void opcionesMenuPrincipal(Usuario usuario, int opcion) {
        if(!usuario.getTipo().equals("normal")){
            switch (opcion) {
                case 1: Consola.gestionLibros(); break;
                case 2: Consola.gestionEjemplares(); break;
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
            librosMap.put(i+1, libros.get(i).getTitulo());
            Set<Ejemplar> ejemplares = daoLibro.findByUniqueValue("isbn", libros.get(i).getIsbn()).getEjemplars();
            int stock = 0;
            for (Ejemplar ejemplar : ejemplares) {
                if(ejemplar.getEstado().equalsIgnoreCase("Disponible")){
                    stock++;
                }
            }
            menu += String.format("| %s. %s   Stock: %s\n", i+1, libros.get(i).getTitulo(), stock);
        }
        librosMap.put(0, menu);
        return librosMap;
    }

    //GESTIONES

    public static void gestionLibros(int opcion){
        switch (opcion){
            case 1: Consola.registrarLibro(); break;
            case 2: Consola.menuPrincipal(); break;
        }
    }

    public static void gestionEjemplares(int opcion){
        switch (opcion){
            case 1: Consola.registrarEjemplar(); break;
            case 2: Consola.menuPrincipal(); break;
        }
    }

    //REGISTROS

    public static void registrarLibro(Libro libro){
        DAO<Libro, String> daoLibro = new DAO<>(Libro.class, String.class);
        daoLibro.add(libro);
    }

    public static void registrarEjemplar(Map<Integer, String> libros, int opcion, String estado) {
        DAO<Libro, Integer> daoLibro = new DAO<>(Libro.class, Integer.class);
        DAO<Ejemplar, String> daoEjemplar = new DAO<>(Ejemplar.class, String.class);
        Ejemplar ejemplar = new Ejemplar(daoLibro.findByUniqueValue("titulo", libros.get(opcion)), estado);
        daoEjemplar.add(ejemplar);
    }
}
