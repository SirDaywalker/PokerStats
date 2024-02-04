package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    private @Id @Setter(AccessLevel.PRIVATE) Long id;

    private String password;
    private String firstName;
    private String lastName;
    private int buyIn;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<UserGame> games;

    @OneToMany(mappedBy = "winner")
    private Set<PokerGame> wins;

    public User(String password, String firstName, String lastName, int buyIn, Role role) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.buyIn = buyIn;
        this.role = role;
    }

    @PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
