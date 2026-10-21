package dev.jfgonzalez.campaigndb.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode
@Embeddable
public class GameCharacterFeatureId implements Serializable {
    @Column(name = "game_character_id")
    private UUID gameCharacterId;

    @Column(name = "feature_id")
    private UUID featureId;
}