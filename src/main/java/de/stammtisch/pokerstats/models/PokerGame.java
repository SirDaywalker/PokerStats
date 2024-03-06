package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "poker_games")
@NoArgsConstructor
@Getter
@Setter
public class PokerGame {
    @Id
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(length = 4096, nullable = false)
    private String notes;

    @OneToMany(mappedBy = "pokerGame", fetch = FetchType.EAGER)
    private Set<UserGame> users;

    @ManyToOne(fetch = FetchType.EAGER)
    private User winner;

    @PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PokerGame other)) {
            return false;
        }
        return other.getId().equals(this.id);
    }
}
