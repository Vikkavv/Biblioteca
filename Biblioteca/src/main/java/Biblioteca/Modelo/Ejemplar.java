package Biblioteca.Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ejemplar")
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "isbn", nullable = false)
    private Biblioteca.Modelo.Libro isbn;

    @Lob
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "ejemplar")
    private Set<Biblioteca.Modelo.Prestamo> prestamos = new LinkedHashSet<>();

    public Ejemplar(Libro isbn, String estado) {
        this.id = id;
        this.isbn = isbn;
        this.estado = estado;
        prestamos = new LinkedHashSet<>();
    }

    public Ejemplar() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Biblioteca.Modelo.Libro getIsbn() {
        return isbn;
    }

    public void setIsbn(Biblioteca.Modelo.Libro isbn) {
        this.isbn = isbn;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<Biblioteca.Modelo.Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(Set<Biblioteca.Modelo.Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", estado='" + estado + '\'' +
                '}';
    }
}