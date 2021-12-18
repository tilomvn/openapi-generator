package kh.com.sathapana.merchant.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "profile", indexes = {
        @Index(name = "idx_profile_id", columnList = "id")
})
public class UserProfile {

    @Id
    private String id;

    @Column(name = "user_cif")
    private String userCIF;

    @Column(nullable = false, name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "national_number", nullable = false, unique = true)
    private String nationalNumber;

    @Email
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "profile_picture", columnDefinition = "TEXT")
    private String profilePicture;

    @OneToOne(mappedBy = "profile")
    private User user;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;
}
