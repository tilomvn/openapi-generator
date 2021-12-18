package kh.com.sathapana.merchant.repository;

import kh.com.sathapana.merchant.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
