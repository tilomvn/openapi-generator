package kh.com.sathapana.merchant.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user", indexes = {
        @Index(name = "username_index", columnList = "username"),
        @Index(name = "idx_merchant_id", columnList = "id")
})
public class User {
    @Id
    private String id;

    @Column(
            unique = true,
            name = "username",
            nullable = false,
            updatable = false,
            length = 50
    )
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false)
    @Enumerated
    private UserStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    private Merchant merchant;

    @Column(nullable = false, name = "role")
    @Enumerated
    private Role role;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

}
