package kh.com.sathapana.store.service.entity.category;


import kh.com.sathapana.store.service.entity.Store;
import kh.com.sathapana.store.service.entity.converter.CategoryLanguageConverter;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "category", indexes = {
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_id", columnList = "id"),
})
public class Category {
    @Id
    private String id;

    @Column(nullable = false, length = 100, updatable = false, unique = true)
    private String code;

    @Column(name = "names", length = 200, nullable = false)
    @Convert(converter = CategoryLanguageConverter.class)
    private Language name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<Store> stores;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;
}
