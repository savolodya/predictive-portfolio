package com.savolodya.predictiveportfolio.models.actiontoken;

import com.savolodya.predictiveportfolio.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "action_tokens",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type", "user_id"})
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActionToken {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(nullable = false, unique = true)
        @NotNull(message = "Token has to be set")
        private UUID token;

        @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(nullable = false, name = "user_id")
        @NotNull(message = "User has to be set")
        private User user;

        @Column(nullable = false)
        @NotNull(message = "Expiry timestamp has to be set")
        @Future(message = "Expiry timestamp has to be in the future")
        private Instant expiryTimestamp;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        @NotNull(message = "Type has to be set")
        private ActionTokenType type;

        public ActionToken(User user, Instant expiryTimestamp, ActionTokenType type) {
                this.token = UUID.randomUUID();
                this.user = user;
                this.expiryTimestamp = expiryTimestamp;
                this.type = type;
        }
}
