package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}