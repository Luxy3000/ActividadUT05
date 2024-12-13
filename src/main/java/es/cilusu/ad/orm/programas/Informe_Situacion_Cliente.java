package es.cilusu.ad.orm.programas;

import es.cilusu.ad.orm.entities.Customer;
import es.cilusu.ad.orm.entities.Payment;
import es.cilusu.ad.orm.entities.Rental;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    private static void findClient(EntityManager entityManager, int clietnId) {
        System.out.printf("Buscando datos del cliente con ID %s \n", clietnId);
        Customer cliente = entityManager.find(Customer.class, clietnId);
        System.out.println("   Datos de cliente:");
        System.out.println("      Nombre: " + cliente.getFirstName() + " " + cliente.getLastName());
        System.out.println("      Direccion: " + cliente.getAddress().getAddress() + ", " + cliente.getAddress().getDistrict() + ", " + cliente.getAddress().getCity().getCity());
        System.out.println("      Tienda socio: " + cliente.getStore().getAddress().getAddress() + ", " + cliente.getStore().getAddress().getDistrict() + ", " + cliente.getStore().getAddress().getCity().getCity());
        System.out.println("      Alquileres: ");
            if (cliente.getRentals().isEmpty()) {
                System.out.println("         No tiene alquileres.");
            } else {
                for (Rental rental : cliente.getRentals()) {
                    System.out.println("         Película: " + rental.getInventory().getFilm().getTitle());
                }
            }
        System.out.println("      Pagos: ");
            if (cliente.getPayments().isEmpty()) {
                System.out.println("         No tiene pagos.");
            } else {
                for (Payment payment: cliente.getPayments()) {
                    System.out.println("         Pago: " + payment.getAmount());
                }
            }
    }
}
