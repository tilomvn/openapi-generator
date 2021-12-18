package kh.com.sathapana.merchant.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "merchant", indexes = {
        @Index(name = "idx_merchant_id", columnList = "id")
})
public class Merchant {
    @Id
    private String id;

    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "slogan")
    private String slogan;

    @OneToOne(mappedBy = "merchant")
    private User owner;

}
