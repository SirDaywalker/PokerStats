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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Getter(AccessLevel.NONE)
    @Column(unique = true)
    private String name;

    @Column(length = 60, nullable = false)
    private String password;

    private int buyIn;

    @Column(nullable = false)
    private String profilePictureType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserGame> games;

    @OneToMany(mappedBy = "winner", fetch = FetchType.EAGER)
    private Set<PokerGame> wins;
    
    @OneToMany(mappedBy = "debtor", fetch = FetchType.EAGER)
    private Set<Invoice> debts;
    
    @OneToMany(mappedBy = "creditor", fetch = FetchType.EAGER)
    private Set<Invoice> credits;

    @Column(length = 320, unique = true, nullable = false)
    private String email;

    private boolean enabled = false;
    private boolean banned = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * @return the name of the user.
     */
    @Override
    public String getUsername() {
        return this.name;
    }

    /**
     * @return true if the account of the user is not expired, false otherwise.
     * In this case, the account of the user never expires.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return true if the account of the user is not locked, false otherwise.
     * In this case, the account of the user never gets locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !this.banned;
    }

    /**
     * @return true if the credentials of the user are not expired, false otherwise.
     * In this case, the credentials of the user never expire.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled && !this.banned;
    }

    /**
     * This method is used to get the URL of the profile picture of the user.
     * @return the URL of the profile picture of the user.
     */
    public String getProfilePictureURL() {
        return "/cdn/u/picture?id=" + this.getId();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User other)) {
            return false;
        }
        return other.getId() == this.id;
    }
}
