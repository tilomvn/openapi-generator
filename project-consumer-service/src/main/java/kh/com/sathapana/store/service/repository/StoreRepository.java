package kh.com.sathapana.store.service.repository;

import kh.com.sathapana.store.service.entity.Store;
import kh.com.sathapana.store.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, String> {

    Store findByCode(String storeCode);

    List<Store> findByUserAssignment_User(User user);
}
