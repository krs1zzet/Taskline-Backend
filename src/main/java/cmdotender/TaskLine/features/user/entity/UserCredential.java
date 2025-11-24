package cmdotender.TaskLine.features.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String type;

    @Column(name = "secret_hash", length = 255)
    private String secretHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder.Default
    private Boolean revoked = Boolean.FALSE;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
