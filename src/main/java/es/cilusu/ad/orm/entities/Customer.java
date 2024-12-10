package es.cilusu.ad.orm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "customer_id", nullable = false)
    private Short id;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ColumnDefault("1")
    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne(optional = false)
    private Store store;

    @OneToMany(mappedBy = "Rental")
    private Collection<Rental> rentals;

    @OneToMany(mappedBy = "customer")
    private Collection<Payment> payments;

}