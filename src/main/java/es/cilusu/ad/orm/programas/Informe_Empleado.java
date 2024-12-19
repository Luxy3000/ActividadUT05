package es.cilusu.ad.orm.programas;


import es.cilusu.ad.orm.entities.Rental;
import es.cilusu.ad.orm.entities.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class Informe_Empleado {
    private static final String SAKILA_PERSISTENCE_UNIT = "sakila";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID del empleado para consulta: ");
        int idEmpleado = sc.nextInt();
        while(idEmpleado != 0){
            try(EntityManagerFactory emf = Persistence.createEntityManagerFactory(SAKILA_PERSISTENCE_UNIT);
                EntityManager em = emf.createEntityManager()) {
                findEmpleado(em, idEmpleado);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Ingrese el ID del empleado para consulta: ");
            idEmpleado = Integer.parseInt(sc.next());
        }
    }

    private static void findEmpleado(EntityManager em, int idEmpleado) {
        System.out.printf("Buscando datos del cliente con ID %s \n", idEmpleado);

        Staff empleado = em.find(Staff.class, idEmpleado);
        System.out.println("\tDatos de empleado: ");
        System.out.println("\t\tNombre: " + empleado.getFirstName() + " " + empleado.getLastName());
        System.out.println("\t\tEmail: " + empleado.getEmail());
        System.out.println("\t\tDirección: " + empleado.getAddress().getAddress() + ", " + empleado.getAddress().getDistrict() + ", " + empleado.getAddress().getCity().getCity());
        System.out.println("\t\tNombre de usuario: " + empleado.getUsername());
        System.out.println("\t\tActivo: " + ((empleado.getActive())? "Sí" : "No"));
        System.out.println("\t\tTienda: " + empleado.getStore().getAddress().getAddress() + ", " + empleado.getStore().getAddress().getDistrict() + ", " + empleado.getStore().getAddress().getCity().getCity());
        System.out.println("\t\tAlquileres: " + empleado.getRentals().size());
            if (empleado.getRentals().isEmpty()) {
                System.out.println("\t\t\t➤ No tiene alquileres.");
            } else {
                for (Rental rental : empleado.getRentals()) {
                    System.out.println("\t\t\t➤ " + rental.getInventory().getFilm().getTitle());
                }
            }
    }
}
