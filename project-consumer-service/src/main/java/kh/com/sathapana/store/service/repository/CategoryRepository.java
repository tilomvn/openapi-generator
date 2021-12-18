package kh.com.sathapana.store.service.repository;

import kh.com.sathapana.store.service.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByName(String category);
}
