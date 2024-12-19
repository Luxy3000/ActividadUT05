package es.cilusu.ad.orm.programas;

import es.cilusu.ad.orm.entities.*;
import jakarta.persistence.*;
import java.util.*;


public class Informe_Peliculas {
    private static final String SAKILA_PERSISTENCE_UNIT = "sakila";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID de la película para consulta: ");
        int idFilm = sc.nextInt();

        while(idFilm != 0){
            try(EntityManagerFactory emf = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
            EntityManager em = emf.createEntityManager()) {
                findFilm(em, idFilm);
            }catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Ingrese el ID de la película para consulta: ");
            idFilm = sc.nextInt();
        }
    }

    private static void findFilm(EntityManager em, int idFilm) {
        System.out.printf("Buscando datos de la película con ID %s \n", idFilm);
        Film film = em.find(Film.class, idFilm);
        System.out.println("\tDatos de la Película: ");
        System.out.println("\t\tNombre: " + film.getTitle());
        System.out.println("\t\tDuración: " + film.getLength() + "min");
        System.out.println("\t\tSinopsis: " + film.getDescription());
        System.out.println("\t\tAño publicación: " + film.getReleaseYear());
        System.out.println("\t\tClasificación: " + film.getRating());
        System.out.println("\t\tCategorías: " + film.getCategories().size());
            if (film.getCategories().isEmpty()) {
                System.out.println("\t\t\tNo tiene categoria.");
            } else {
                for (Category cat : film.getCategories()) {
                    System.out.println("\t\t\t➤ " + cat.getName());
                }
            }
        System.out.println("\t\tIdioma Original: " + film.getOriginalLanguage().getName());
        System.out.println("\t\tIdioma: " + film.getLanguage().getName());
        System.out.println("\t\tActores: " + film.getActors().size());
        if(film.getActors().isEmpty()) {
            System.out.println("\t\t\tSin actores.");
        }else{
            for (Actor actor : film.getActors()) {
                System.out.println("\t\t\t➤ " + actor.getFirstName() + " " + actor.getLastName());
            }
        }
        System.out.println("\t\tCoste de renta: " + film.getRentalRate() + "$");
        System.out.println("\t\tDuración de renta: " + film.getRentalDuration() + " días");
        System.out.println("\t\tCoste de reemplazo: " + film.getReplacementCost() + "$");
        findCopias(em, idFilm);
    }

    private static void findCopias(EntityManager em, int idFilm) {
        String jpql = "SELECT i FROM Inventory i WHERE i.film.id = :idFilm AND  NOT EXISTS (SELECT r FROM Rental r WHERE r.inventory = i AND r.returnDate IS NULL)";
        TypedQuery<Inventory> query = em.createQuery(jpql, Inventory.class);
        query.setParameter("idFilm", idFilm);

        List<Inventory> copias = query.getResultList();

        System.out.println("\t\tCopias disponibles: " + copias.size());

        Set<String> direcciones = new HashSet<>();
        for(Inventory inv : copias) {
            String direc = inv.getStore().getAddress().getAddress();
            int count = 0;

            if(!direcciones.contains(direc)) {
                for(Inventory inventario : copias){
                    if(inventario.getStore().getAddress().getAddress().equals(direc)){
                        count++;
                    }
                }
                System.out.println("\t\t\tCopias: " + count);
                System.out.println("\t\t\tDireccion: " + direc + ", " + inv.getStore().getAddress().getDistrict() + ", " + inv.getStore().getAddress().getCity().getCity());

                direcciones.add(direc);
            }


        }
    }
}