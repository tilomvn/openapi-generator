package kh.com.sathapana.store.service.repository;

import kh.com.sathapana.store.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
