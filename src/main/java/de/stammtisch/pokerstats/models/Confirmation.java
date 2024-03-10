package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "confirmations")
public class Confirmation {

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    private String token;

    private long validatedAt;

    @ManyToOne
    private User user;

    @PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }
}
