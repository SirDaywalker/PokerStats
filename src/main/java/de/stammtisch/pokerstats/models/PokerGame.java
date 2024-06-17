package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "poker_games")
@NoArgsConstructor
@Getter
@Setter
public class PokerGame implements Comparable<PokerGame> {
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@NonNull PokerGame o) {
        return Long.compare(this.getId(), o.getId());
    }
}
