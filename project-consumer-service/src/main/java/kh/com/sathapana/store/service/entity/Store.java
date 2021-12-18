package kh.com.sathapana.store.service.entity;

import kh.com.sathapana.store.service.entity.address.City;
import kh.com.sathapana.store.service.entity.address.Commune;
import kh.com.sathapana.store.service.entity.address.District;
import kh.com.sathapana.store.service.entity.category.Category;
import kh.com.sathapana.store.service.entity.converter.BusinessHourConverter;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "store", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_store_id", columnList = "id")
})
public class Store {
    @Id
    private String id;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String code;

    /**
     * To determine which merchant this store belongs to
     */
    @Column(nullable = false, name = "merchant_id", length = 50)
    private String merchantId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store")
    private List<UserAssignment> userAssignment;

    @Column(name = "business_hour", length = 1000, nullable = false)
    @Convert(converter = BusinessHourConverter.class)
    private List<BusinessHour> businessHours;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "commune_id", referencedColumnName = "id")
    private Commune commune;

    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_category",
            joinColumns = {
                    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, updatable = false)
            }
    )
    private List<Category> categories;

    @Column(name = "description", length = 500)
    private String description;

    @Column(nullable = false, name = "status")
    @Enumerated
    private StoreStatus status;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] logo;

    @Column(length = 200)
    private String gps;

    @Column(nullable = false, name = "imported_m1")
    private boolean importedM1;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    public void setStoreInUserAssignment() {
        userAssignment.forEach(userAssignment -> userAssignment.setStore(this));
    }
}
