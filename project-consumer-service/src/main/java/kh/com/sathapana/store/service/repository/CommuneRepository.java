package kh.com.sathapana.store.service.repository;

import kh.com.sathapana.store.service.entity.address.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneRepository extends JpaRepository<Commune, String> {
}
