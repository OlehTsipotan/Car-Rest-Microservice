package com.myapi.cars.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "make")
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "make_generator")
    @SequenceGenerator(name = "make_generator", sequenceName = "make_seq", allocationSize = 1)
    @Column(name = "make_id")
    private Long id;

    @NotBlank(message = "Make name must not be blank")
    @Size(max = 255, message = "Make name must be less than 255 characters")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Make(String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
                o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() :
                this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Make make = (Make) o;
        return getId() != null && Objects.equals(getId(), make.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}