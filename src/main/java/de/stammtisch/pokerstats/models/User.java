package de.stammtisch.pokerstats.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @Getter(AccessLevel.NONE)
    private String name;

    private String password;
    private int buyIn;
    private String profilePictureType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<UserGame> games;

    @OneToMany(mappedBy = "winner")
    private Set<PokerGame> wins;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.name;
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

    public String getNamePathSave() {
        return URLEncoder.encode(this.name, StandardCharsets.UTF_8);
    }

    public String getProfilePictureURL() {
        return "/cdn/u/%s/picture.%s".formatted(this.getNamePathSave(), this.getProfilePictureType());
    }
}
