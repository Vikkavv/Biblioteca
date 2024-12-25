package Biblioteca.Vista;

import Biblioteca.Controlador.BibliotecaException;
import Biblioteca.Controlador.Controlador;
import Biblioteca.Modelo.*;
import jakarta.persistence.NoResultException;

import java.util.HashMap;
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
            Controlador.opcionesMenuPrincipal(usuario, opcion);
        }
        catch (BibliotecaException e){
            System.out.println(e.getMessage());
            menuPrincipal();
        }
        catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            menuPrincipal();
        }
    }

    //REGISTROS

    public static void registrarLibro() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.registrarLibro());
        String isbn = sc.nextLine();
        System.out.println("Título: ");
        String titulo = sc.nextLine();
        System.out.println("Autor: ");
        String autor = sc.nextLine();
        try{
            Controlador.registrarLibro(new Libro(isbn, titulo, autor));
        }
        catch(NumberFormatException e){
            System.out.println("El ISBN no puede tener letras");
            registrarLibro();
        }
        catch (BibliotecaException e){
            System.out.println(e.getMessage());
            registrarLibro();
        }
        gestionLibros();
    }

    public static void registrarEjemplar(){
        Map<Integer, String> estados = new HashMap<>();
        estados.put(1, "Disponible"); estados.put(2, "Prestado"); estados.put(3, "Dañado"); estados.put(4, "----------------salir-----------------");
        Scanner sc = new Scanner(System.in);
        Map<Integer, String> listaLibros = Controlador.menuLibros();
        System.out.println(Menus.registrarEjemplar()+"\n"+listaLibros.get(0));
        listaLibros.remove(0);
        int opcion = 0;
        try{
            opcion = sc.nextInt();
            if(Controlador.checkExit(listaLibros.get(opcion))) gestionEjemplares();
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            registrarEjemplar();
        }catch (NullPointerException e){
            System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            registrarEjemplar();
        }
        boolean finish = false;
        while(!finish){
            try{
                System.out.println("Estado:\n| 1. Disponible\n| 2. Prestado\n| 3. Dañado\n-----------\n| 4. Salir");
                int opcion1 = sc.nextInt();
                if(Controlador.checkExit(estados.get(opcion1))){ registrarEjemplar(); return;}
                Controlador.registrarEjemplar(listaLibros.get(opcion), estados.get(opcion1));
                finish = true;
            }catch(InputMismatchException e) {
                System.out.println("Introduce solo números");
                sc.next();
            }
            catch (NullPointerException e){
                System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            }
        }
        gestionEjemplares();
    }

    public static void registrarUsuario() {
        Map<Integer, String> tipos = new HashMap<>();
        tipos.put(1, "administrador"); tipos.put(2, "normal");
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.registrarUsuario());
        String dni = sc.nextLine();
        System.out.println("Nombre: ");
        String nombre = sc.nextLine();
        System.out.println("Email: ");
        String email = sc.nextLine();
        System.out.println("Contraseña: ");
        String pass = sc.nextLine();
        int tipo = 0;
        while (tipo == 0){
            try{
                System.out.println("Tipo: \n1.Administrador\n2.Normal");
                tipo = sc.nextInt();
                Controlador.registrarUsuario(new Usuario(dni,nombre,email,pass,tipos.get(tipo)));
            }catch(InputMismatchException e){
                System.out.println("Introduce solo números");
                sc.next();
            }catch(org.hibernate.PropertyValueException e){
                System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
                tipo = 0;
            }catch (BibliotecaException e){
                System.out.println(e.getMessage());
            }
        }
        gestionUsuarios();
    }

    public static void registrarPrestamo() {
        Scanner sc = new Scanner(System.in);
        Map<Integer, String> usuariosMap = Controlador.menuUsuarios();
        Map<Integer, String> librosMap;
        System.out.println(Menus.registrarPrestamo()+"\n"+usuariosMap.get(0));
        usuariosMap.remove(0);
        Usuario usuario = null;
        int opcion = 0;
        try{
            opcion = sc.nextInt();
            if(Controlador.checkExit(usuariosMap.get(opcion))) gestionPrestamos();
            usuario = Controlador.seleccionarUsuario(usuariosMap.get(opcion));
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            registrarPrestamo();
        }catch (NullPointerException e){
            System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            registrarPrestamo();
        }
        Ejemplar ejemplar = null;
        boolean finish = false;
        boolean registeredPrestamo = false;
        while (!registeredPrestamo){
            while(!finish){
                try{
                    librosMap = Controlador.menuLibros();
                    System.out.printf("Usuario seleccionado: %s, %s\n"+librosMap.get(0), usuario.getNombre(), usuario.getEmail());
                    librosMap.remove(0);
                    opcion = sc.nextInt();
                    if(Controlador.checkExit(librosMap.get(opcion))) registrarPrestamo();
                    ejemplar = Controlador.atribuirEjemplar(librosMap.get(opcion));
                    finish = true;
                }catch (InputMismatchException e){
                    System.out.println("Introduce solo números");
                    sc.next();
                }
                catch (NullPointerException e){
                    System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
                }
            }
            try{
                Controlador.registrarPrestamo(ejemplar, usuario);
                registeredPrestamo = true;
                System.out.println("##### Prestamo registrado #####");
            }
            catch (NullPointerException e){
                System.out.println("No quedan ejemplares disponibles de ese libro");
                registrarPrestamo();
            }
            catch (BibliotecaException e){
                System.out.println(e.getMessage());
                if(e.getMessage().equals("El usuario ya tiene un prestamo activo\ncon un ejemplar del mismo libro")){
                    finish = false;
                }
                else registrarPrestamo();
            }
        }
        gestionPrestamos();
    }

    public static void registrarDevolucion() {
        Scanner sc = new Scanner(System.in);
        Map<Integer, String> estadoActual = new HashMap<>();
        estadoActual.put(1, "Dañado"); estadoActual.put(2, "Disponible"); estadoActual.put(3, "----------------salir-----------------");
        Map<Integer, String> usuariosMap = Controlador.menuUsuarios();
        Map<Integer, String> ejemplaresPrestados;
        System.out.println(Menus.registrarDevolucion()+"\n"+usuariosMap.get(0));
        usuariosMap.remove(0);
        Usuario usuario = null;
        int opcion;
        try{
            opcion = sc.nextInt();
            if(Controlador.checkExit(usuariosMap.get(opcion))) gestionDevolucion();
            usuario = Controlador.seleccionarUsuario(usuariosMap.get(opcion));
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            registrarDevolucion();
        }catch (NullPointerException e){
            System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            registrarDevolucion();
        }
        Prestamo prestamo = null;
        boolean finish = false;
        while(!finish){
            try{
                ejemplaresPrestados = Controlador.menuEjemplaresDeUnUsuario(usuario);
                System.out.println(Menus.elegirPrestamo(usuario.getNombre())+"\n"+ejemplaresPrestados.get(0));
                ejemplaresPrestados.remove(0);
                opcion = sc.nextInt();
                if(Controlador.checkExit(ejemplaresPrestados.get(opcion))) registrarDevolucion();
                prestamo = Controlador.seleccionarPrestamo(Integer.parseInt(ejemplaresPrestados.get(opcion)));
                finish = true;
            }catch (InputMismatchException e){
                System.out.println("Introduce solo números");
                sc.next();
            }catch (NullPointerException e){
                System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            }
        }
        finish = false;
        while(!finish){
            try{
                System.out.println("¿El ejemplar ha sido dañado?\n| 1. Sí\n| 2. No\n-----------\n| 3. Salir");
                opcion = sc.nextInt();
                if(Controlador.checkExit(estadoActual.get(opcion))) registrarDevolucion();
                prestamo.getEjemplar().setEstado(estadoActual.get(opcion));
                finish = true;
            }catch (InputMismatchException e){
                System.out.println("Introduce solo números");
                sc.next();
            }catch (NullPointerException e){
                System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
            }
        }
        Controlador.registrarDevolucion(usuario, prestamo);
        gestionDevolucion();
    }

    //GESTIONES

    public static void gestionLibros(){
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionLibros());
        try{
            Controlador.gestionLibros(sc.nextInt());
        }catch (BibliotecaException e){
            System.out.println(e.getMessage());
            gestionLibros();
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            gestionLibros();
        }
    }

    public static void gestionEjemplares() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionEjemplares());
        try{
            Controlador.gestionEjemplares(sc.nextInt());
        }catch (BibliotecaException e){
            System.out.println(e.getMessage());
            gestionEjemplares();
        }
        catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            gestionEjemplares();
        }
    }

    public static void gestionUsuarios() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionUsuarios());
        try{
            Controlador.gestionUsuarios(sc.nextInt());
        }catch (BibliotecaException e){
            System.out.println(e.getMessage());
            gestionUsuarios();
        }
        catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            gestionUsuarios();
        }
    }

    public static void gestionPrestamos() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionPrestamos());
        try{
            Controlador.gestionPrestamos(sc.nextInt());
        }catch (BibliotecaException e){
            System.out.println(e.getMessage());
            gestionPrestamos();
        }
        catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            gestionPrestamos();
        }
    }

    public static void gestionDevolucion() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Menus.gestionDevolucion());
        try{
            Controlador.gestionDevolucion(sc.nextInt());
        }catch (BibliotecaException e){
            System.out.println(e.getMessage());
            gestionDevolucion();
        }catch (InputMismatchException e){
            System.out.println("Introduce solo números");
            gestionDevolucion();
        }
    }

    public static void visualizacionPrestamos() {
        Scanner sc = new Scanner(System.in);
        Map<Integer, String> ejemplaresPrestados;
        Usuario usuarioLcl = null;
        if(!(usuario.getTipo().equals("normal"))){
            Map<Integer, String> usuariosMap = Controlador.menuUsuarios();
            System.out.println(Menus.visualizarPrestamos()+"\n"+usuariosMap.get(0));
            usuariosMap.remove(0);
            int opcion;
            try{
                opcion = sc.nextInt();
                if(Controlador.checkExit(usuariosMap.get(opcion))){ menuPrincipal(); return;}
                usuarioLcl = Controlador.seleccionarUsuario(usuariosMap.get(opcion));
            }catch (InputMismatchException e){
                System.out.println("Introduce solo números");
                visualizacionPrestamos();
            }catch (NullPointerException e){
                System.out.println("Introduce únicamente números que esten comprendidos en las opciones");
                visualizacionPrestamos();
            }
        }
        else usuarioLcl = usuario;
        ejemplaresPrestados = Controlador.menuEjemplaresDeUnUsuario(usuarioLcl);
        String menu = Menus.elegirPrestamo(usuarioLcl.getNombre());
        menu = menu.replace("\nElige el ejemplar a devolver:", "");
        System.out.println(menu+"\n"+ejemplaresPrestados.get(0)+"\n(Pulse cualquier tecla para continuar)");
        ejemplaresPrestados.remove(0);
        sc.next();
        if(!usuario.getTipo().equals("normal")){
            visualizacionPrestamos();
        }
        else menuPrincipal();
    }

    public static void main(String[] args) {
        usuario = menuIniciarSesion();
        Consola.menuPrincipal();
    }

    //juan.perez@example.com password123
    //luis.lopez@example.com adminpass123
}