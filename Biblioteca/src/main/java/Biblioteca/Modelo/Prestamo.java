package Biblioteca.Modelo;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "prestamo")
public class Prestamo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Biblioteca.Modelo.Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ejemplar_id", nullable = false)
    private Ejemplar ejemplar;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaDevolucion")
    private LocalDate fechaDevolucion;

    public Prestamo(Usuario usuario, Ejemplar ejemplar, LocalDate fechaInicio) {
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fechaInicio = fechaInicio;
    }

    public Prestamo(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Biblioteca.Modelo.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Biblioteca.Modelo.Usuario usuario) {
        this.usuario = usuario;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return Objects.equals(usuario, prestamo.usuario) && Objects.equals(ejemplar, prestamo.ejemplar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, ejemplar);
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", ejemplar=" + ejemplar +
                ", fechaInicio=" + fechaInicio +
                ", fechaDevolucion=" + fechaDevolucion +
                '}'+"\n";
    }
}