package etraveli.group.model;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.stream.Collectors.joining;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

// Don't define @Data annotation because include @ToString method, and it
// can generate overload when print, for every user, their roles list.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Users implements UserDetails {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = IDENTITY)
    private Integer idUser;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = EAGER, cascade = CascadeType.ALL)
    private List<UsersRole> usersRoles;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRolesWithDelimiter = this.usersRoles.stream()
                .map(userRole -> "ROLE_" + userRole.getRole().getName().name())
                .collect(joining(","));
        return commaSeparatedStringToAuthorityList(userRolesWithDelimiter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(idUser, users.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUser);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Users{");
        sb.append("idUser=").append(idUser);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
