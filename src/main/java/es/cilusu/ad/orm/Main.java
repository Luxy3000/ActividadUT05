package es.cilusu.ad.orm;

import es.cilusu.ad.orm.entities.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class Main {
    private static final String SAKILA_PERSISTENCE_UNIT = "sakila";
    private static final int TEST_CITY_ID = 1;
    public static void main(String[] args) {

        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager()) {
            findCity(em);

            usandoSQL(em);
        }
    }

    private static void findCity(EntityManager entityManager){
        System.out.printf("Buscamos ciudad con ID %s, usando find.\n", TEST_CITY_ID);
        City cityFind = entityManager.find(City.class, TEST_CITY_ID);
        System.out.println("Resultado de la búsqueda:");
        System.out.println(cityFind);
        System.out.println();
    }

    private static void usandoSQL(EntityManager entityManager){
        System.out.printf("Buscamos la categoría con ID = %s, usando SQL Nativo.\n", TEST_CITY_ID);

        Query singleQuery = entityManager.createNativeQuery("select city_id, city,  last_update from city where city_id = :citId", City.class);
        singleQuery.setParameter("citId", TEST_CITY_ID);

        City city = (City) singleQuery.getSingleResult();
        System.out.println(city);
    }
}