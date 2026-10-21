package dev.jfgonzalez.campaigndb.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "game_character_feature")
public class GameCharacterFeature {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private GameCharacterFeatureId id = new GameCharacterFeatureId();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("gameCharacterId")
    @JoinColumn(name = "game_character_id", nullable = false)
    private GameCharacter gameCharacter;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("featureId")
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false)
    private Map<String, Object> properties;
}