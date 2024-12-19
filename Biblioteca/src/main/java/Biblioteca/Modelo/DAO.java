package Biblioteca.Modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAO<T,ID> {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    private Class<T> clase;
    private Class<ID> id;

    public DAO(Class<T> clase, Class<ID> id){
        this.clase = clase;
        this.id = id;
    }

    //INSERT
    public void add(T t){
        tx.begin();
        em.persist(t);
        tx.commit();
    }

    //UPDATE
    public void update(T t){
        tx.begin();
        em.merge(t);
        tx.commit();
    }

    //DELETE
    public void delete(T t){
        tx.begin();
        em.remove(t);
        tx.commit();
    }

    //SELECT
    public T findById(ID id){
        return em.find(clase, id);
    }

    //SELECTByUniqueValue
    public T findByUniqueValue(String column, String value){
        return em.createQuery("SELECT e FROM "+ clase.getSimpleName()+" e WHERE "+ column + " = \"" + value + "\"", clase).getSingleResult();
    }

    //SELECTALLWHERE
    public List<T> findAllWhere(String column, String value){
        return em.createQuery("SELECT e FROM " +clase.getSimpleName()+" e WHERE "+ column + " = \"" + value + "\"", clase).getResultList();
    }
    //SELECTALL
    public List<T> findAll(){
        return em.createQuery("SELECT e FROM "+ clase.getSimpleName()+" e", clase).getResultList();
    }
}
