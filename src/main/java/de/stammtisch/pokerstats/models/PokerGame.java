package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * Checks if the given user has played in this game.
     * @param user The user to check for.
     * @return True if the user has played in this game, false otherwise.
     */
    public boolean userHasPlayed(User user) {
        return this.users.stream().anyMatch(userGame -> userGame.getUser().equals(user));
    }

    /**
     * Returns the date of the game.
     * @return The date of the game.
     */
    public Date getDate() {
        return new Date(this.id);
    }

    /**
     * Returns the date of the game in the given format.
     * @param format The format to use. See {@link SimpleDateFormat} for more information.
     * @return The date of the game in the given format.
     */
    public String getFormattedDate(String format) {
        return new SimpleDateFormat(format).format(this.getDate());
    }

    public double getBuyInForUser(User user) {
        return this.users.stream()
                .filter(userGame -> userGame.getUser().equals(user))
                .map(UserGame::getBuyIn)
                .findFirst()
                .orElse(0.0);
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
