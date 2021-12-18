package kh.com.sathapana.store.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {
    /**
     * This is userID
     */
    @Id
    private String id;

    @Column(nullable = false)
    @Enumerated
    private UserStatus status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserAssignment> userAssignment;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    public void setUserInUserAssignment() {
        this.userAssignment.forEach(user -> user.setUser(this));
    }
}
