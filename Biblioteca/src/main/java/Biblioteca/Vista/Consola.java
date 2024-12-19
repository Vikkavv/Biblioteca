package Biblioteca.Vista;

import Biblioteca.Controlador.BibliotecaException;
import Biblioteca.Controlador.Controlador;
import Biblioteca.Modelo.Libro;
import Biblioteca.Modelo.Usuario;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Consola {

    private static Usuario usuario;

    //MENUS

    public static Usuario menuIniciarSesion(){
        Usuario usuario = null;
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.menuIniciarSesion());
        String email = sc.nextLine();
        System.out.println("Contraseña: ");
        String pass = sc.nextLine();
        try {
        usuario = Controlador.validaCredenciales(email, pass);
        }
        catch (BibliotecaException e){
            System.out.println(e.getMessage());
            return menuIniciarSesion();
        }
        return usuario;
    }

    public static void menuPrincipal() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Controlador.selectorDeMenus(usuario));
        int opcion = 0;
        try{
            opcion = sc.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números comprendidos en el rango de opciones");
            menuPrincipal();
        }
        Controlador.opcionesMenuPrincipal(usuario, opcion);
    }

    //REGISTROS

    public static void registrarLibro(){
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.registrarLibro());
        String isbn = sc.nextLine();
        System.out.println("Título: ");
        String titulo = sc.nextLine();
        System.out.println("Autor: ");
        String autor = sc.nextLine();
        Controlador.registrarLibro(new Libro(isbn, titulo, autor));
        gestionLibros();
    }

    public static void registrarEjemplar(){
        Scanner sc = new Scanner(System.in);
        Map<Integer, String> listaLibros = Controlador.menuLibros();
        System.out.println(Menus.registrarEjemplar()+"\n"+listaLibros.get(0));
        int opcion = sc.nextInt();
        sc.nextLine();
        System.out.println("Estado: Disponible, Prestado, Dañado");
        String estado = sc.nextLine();
        Controlador.registrarEjemplar(listaLibros, opcion, estado);
        gestionEjemplares();
    }

    //GESTIONES

    public static void gestionLibros(){
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionLibros());
        Controlador.gestionLibros(sc.nextInt());
    }

    public static void gestionEjemplares() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionEjemplares());
        Controlador.gestionEjemplares(sc.nextInt());
    }

    public static void main(String[] args) {
        usuario = menuIniciarSesion();
        Consola.menuPrincipal();
    }

    //juan.perez@example.com password123
    //luis.lopez@example.com adminpass123
}
