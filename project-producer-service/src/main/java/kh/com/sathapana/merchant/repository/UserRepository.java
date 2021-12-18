package kh.com.sathapana.merchant.repository;

import kh.com.sathapana.merchant.entity.Role;
import kh.com.sathapana.merchant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    //TODO this is not correct
    @Query("select u from User u where u.role = ?1 and u.merchant.id = ?1")
    List<User> getCashiers(Role role, String merchantId);
}
