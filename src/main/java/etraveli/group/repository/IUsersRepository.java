package etraveli.group.repository;

import etraveli.group.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
}
