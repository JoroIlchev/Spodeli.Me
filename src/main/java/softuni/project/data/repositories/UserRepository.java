package softuni.project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.project.data.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

//    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.realEstates left join fetch u.vehicles where u.username = :username")
//    User findByUsernameWithQuery(@Param("username") String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);

}
