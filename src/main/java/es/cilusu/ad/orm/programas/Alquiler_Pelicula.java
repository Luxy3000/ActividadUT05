package es.cilusu.ad.orm.programas;

import es.cilusu.ad.orm.entities.*;
import jakarta.persistence.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Alquiler_Pelicula {
    private static final String SAKILA_PERSISTENCE_UNIT = "sakila";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try(EntityManagerFactory factory = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
            EntityManager em = factory.createEntityManager()) {
            System.out.print("Id de empleado: "); int idStaff = sc.nextInt();
            Staff staff = em.find(Staff.class, idStaff);
            if(staff != null) { //El empleado existe

                Store store = staff.getStore();
                System.out.print("Id de la película: "); int idFilm = sc.nextInt();
                Inventory invent= primerInvent(em, store, idFilm);
                if(invent != null) { //La película está en el inventario

                    Long hayStock = hayStock(em, store, idFilm);
                    if(hayStock > 0) { //La película está en stock

                        System.out.print("Id del cliente: "); int idCliente = sc.nextInt();
                        Customer cliente = em.find(Customer.class, idCliente);
                        if(cliente != null) { //Cliente existe

                            crearRenta(em, staff, cliente, invent);

                        } else {
                            System.out.println("Cliente no encontrado");
                        }
                    } else {
                        System.out.println("Película fuera de stock");
                    }
                } else {
                    System.out.println("La película no existe en este local.");
                }
            } else {
                System.out.println("Id de empleado no encontrado.");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static Inventory primerInvent(EntityManager em, Store store, int idFilm){
        //El inventory tiene id de la store y de la film
        int idStore = store.getId();
        TypedQuery<Inventory> existe = em.createQuery("select i from Inventory i where i.store.id = :idStore and i.film.id = :idFilm", Inventory.class);
        existe.setParameter("idStore", idStore);
        existe.setParameter("idFilm", idFilm);
        Inventory inventory = existe.getResultStream().findFirst().orElse(null);

        return inventory;
    }

    private static Long hayStock(EntityManager em, Store store, int idFilm){
        //Usar lo mismo que para comprobar si la película está.
        int idStore = store.getId();
        Long hayStock = em.createQuery("select count(*) from Inventory i where i.store.id = :idStore and i.film.id = :idFilm", Long.class).setParameter("idStore", idStore).setParameter("idFilm", idFilm).getSingleResult();
        return hayStock;
    }

    private static void crearRenta(EntityManager em, Staff staff, Customer cliente, Inventory invent) {
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            Rental rental = new Rental();
            rental.setInventory(invent);
            rental.setCustomer(cliente);
            rental.setStaff(staff);
            rental.setReturnDate(null);
            rental.setLastUpdate(Instant.now());
            rental.setRentalDate(Instant.now());

            em.persist(rental);

            System.out.println(tx);

            tx.commit();
        } finally {
            if(tx.isActive()) tx.rollback();
        }
    }
}
