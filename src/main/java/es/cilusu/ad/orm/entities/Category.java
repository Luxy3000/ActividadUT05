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
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "category_id", nullable = false)
    private Byte id;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Film> films;

}