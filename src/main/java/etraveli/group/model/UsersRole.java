package etraveli.group.model;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UsersRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer idAsignation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private Users user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_role", referencedColumnName = "id_role")
    private Role role;
}
