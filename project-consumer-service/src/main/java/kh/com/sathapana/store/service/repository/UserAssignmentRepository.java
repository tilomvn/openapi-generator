package kh.com.sathapana.store.service.repository;

import kh.com.sathapana.store.service.entity.Store;
import kh.com.sathapana.store.service.entity.StoreRole;
import kh.com.sathapana.store.service.entity.User;
import kh.com.sathapana.store.service.entity.UserAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAssignmentRepository extends JpaRepository<UserAssignment, String> {

    List<UserAssignment> findByUser(User user);

    long countByUserAndStoreAndRole(User user, Store store, StoreRole role);
}
