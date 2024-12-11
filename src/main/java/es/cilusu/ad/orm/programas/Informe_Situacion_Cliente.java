package es.cilusu.ad.orm.programas;

import es.cilusu.ad.orm.entities.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.mariadb.jdbc.client.Client;

import java.util.*;

public class Informe_Situacion_Cliente {
    private static final String SAKILA_PERSISTENCE_UNIT = "sakila";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID del cliente para consulta: ");
        int idCliente = Integer.parseInt(sc.next());
        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager()) {
            findClient(em, idCliente);
        }

    }

    private static void findClient(EntityManager entityManager, int clietnId) {
        System.out.printf("Buscando datos del cliente con ID %s \n", clietnId);
        Customer cliente = entityManager.find(Customer.class, clietnId);
        System.out.println("Datos de cliente:");
        System.out.println("Nombre: " + cliente.getFirstName() + " " + cliente.getLastName());
    }
}
