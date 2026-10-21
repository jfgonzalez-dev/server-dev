package dev.jfgonzalez.campaigndb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Entity
@Table(name = "game_character")
public class GameCharacter {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String background;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @Column(name = "is_pc", nullable = false)
    private boolean isPc = true;

    @Column(nullable = false)
    private int level = 1;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> properties;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "owner_id", nullable = true)
    private Player owner;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "campaign_id", nullable = true)
    private Campaign campaign;

    @ToString.Exclude
    @OneToMany(
        mappedBy = "gameCharacter",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GameCharacterFeature> features = new ArrayList<>();

    public GameCharacterFeature addFeature(Feature feature, Map<String, Object> properties) {
        GameCharacterFeature characterFeature = new GameCharacterFeature();
        characterFeature.setGameCharacter(this);
        characterFeature.setFeature(feature);
        characterFeature.setProperties(properties);

        this.features.add(characterFeature);
        return characterFeature;
    }

    public void removeFeature(Feature feature) {
        this.features.removeIf(cf -> {
            boolean match = cf.getFeature().equals(feature);
            if (match) {
                cf.setGameCharacter(null);
                cf.setFeature(null);
            }
            return match;
        });
    }

    @ToString.Exclude
    @OneToMany(
        mappedBy = "gameCharacter",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GameCharacterItem> items = new ArrayList<>();

    public GameCharacterItem addItem(Item item, int quantity, boolean isEquipped, Map<String, Object> properties) {
        GameCharacterItem characterItem = new GameCharacterItem();
        characterItem.setGameCharacter(this);
        characterItem.setItem(item);
        characterItem.setQuantity(quantity);
        characterItem.setEquipped(isEquipped);
        characterItem.setProperties(properties);

        this.items.add(characterItem);
        return characterItem;
    }

    public void removeItem(Item item) {
        this.items.removeIf(i -> {
            boolean match = i.getItem().equals(item);
            if (match) {
                i.setGameCharacter(null);
                i.setItem(null);
            }
            return match;
        });
    }
}