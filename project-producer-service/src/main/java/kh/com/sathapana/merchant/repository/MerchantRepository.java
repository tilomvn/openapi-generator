package kh.com.sathapana.merchant.repository;

import kh.com.sathapana.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, String> {
    @Query("select m from Merchant m where m.owner.id = ?1")
    Optional<Merchant> findMerchantByOwner(String userId);
}
