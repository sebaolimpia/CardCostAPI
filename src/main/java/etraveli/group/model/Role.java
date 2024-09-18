package etraveli.group.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import lombok.*;
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
public class Role {

    @Id
    @Column(name = "id_role")
    @GeneratedValue(strategy = IDENTITY)
    private Integer idRole;

    @Column(unique = true, nullable = false)
    @Enumerated(STRING)
    private RoleEnum name;

    private String description;

    @OneToMany(mappedBy = "role", fetch = LAZY)
    private List<UsersRole> usersRoles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(idRole, role.idRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idRole);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Role{");
        sb.append("idRole=").append(idRole);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
