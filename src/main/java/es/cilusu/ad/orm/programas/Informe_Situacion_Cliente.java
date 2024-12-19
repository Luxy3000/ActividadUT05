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
        int idCliente= sc.nextInt();
        while(idCliente != 0){
            try(EntityManagerFactory emf = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
            EntityManager em = emf.createEntityManager()) {
                findClient(em, idCliente);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Ingrese el ID del cliente para consulta: ");
            idCliente = Integer.parseInt(sc.next());
        }
    }

    private static void findClient(EntityManager entityManager, int clietnId) {
        System.out.printf("Buscando datos del cliente con ID %s \n", clietnId);
        Customer cliente = entityManager.find(Customer.class, clietnId);
        System.out.println("\tDatos de cliente:");
        System.out.println("\t\tNombre: " + cliente.getFirstName() + " " + cliente.getLastName());
        System.out.println("\t\tEmail: " + cliente.getEmail());
        System.out.println("\t\tActivo: " + ((cliente.getActive())? "Sí" : "No"));
        System.out.println("\t\tDireccion: " + cliente.getAddress().getAddress() + ", " + cliente.getAddress().getDistrict() + ", " + cliente.getAddress().getCity().getCity());
        System.out.println("\t\tTienda socio: " + cliente.getStore().getAddress().getAddress() + ", " + cliente.getStore().getAddress().getDistrict() + ", " + cliente.getStore().getAddress().getCity().getCity());
        System.out.println("\t\tAlquileres: " + cliente.getRentals().size());
            if (cliente.getRentals().isEmpty()) {
                System.out.println("\t\t\tNo tiene alquileres.");
            } else {
                for (Rental rental : cliente.getRentals()) {
                    System.out.println("\t\t\t➤ " + rental.getInventory().getFilm().getTitle());
                }
            }
        findPayments(entityManager, clietnId);
    }

    private static void findPayments(EntityManager entityManager, int clientId) {
        List<Payment> pagosTotal = entityManager.createNativeQuery(
                        "SELECT * FROM Payment WHERE payment.customer_id = ?", Payment.class)
                .setParameter(1, clientId)
                .getResultList();

        Set<Payment> pagos = new HashSet<>(pagosTotal);
        System.out.println("\t\tPagos: " + pagos.size());
        if (pagos.isEmpty()) {
            System.out.println("\t\t\tNo tiene pagos.");
        } else {
            int count = 0;
            for (Payment payment : pagos) {
                if(count > 0 && ((count%4) == 0)){
                    System.out.println();
                }
                System.out.print("\t\t\t➤ " + payment.getAmount() + "$");
                count++;
            }
        }

    }
}
