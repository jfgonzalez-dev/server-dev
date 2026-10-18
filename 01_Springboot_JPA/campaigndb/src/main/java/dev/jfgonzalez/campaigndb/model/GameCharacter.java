package dev.jfgonzalez.campaigndb.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> data;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "player_id",
        nullable = true
    )
    private Player player;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "campaign_id",
        nullable = true
    )
    private Campaign campaign;

    @ToString.Exclude
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (
        name = "game_character_skill",
        joinColumns = @JoinColumn (name = "game_character_id"),
        inverseJoinColumns = @JoinColumn (name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.getGameCharacters().add(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.getGameCharacters().remove(this);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "gameCharacter",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Inventory> inventory = new ArrayList<>();

    public Inventory addItem(Item item, int quantity, boolean isEquipped, Map<String, Object> data) {
        Inventory inventoryItem = new Inventory();
        inventoryItem.setGameCharacter(this);
        inventoryItem.setItem(item);
        inventoryItem.setQuantity(quantity);
        inventoryItem.setEquipped(isEquipped);
        inventoryItem.setData(data);

        this.inventory.add(inventoryItem);
        return inventoryItem;
    }

    public void removeItem(Item item) {
        this.inventory.removeIf(i -> {
            boolean match = i.getItem().equals(item);
            if (match) {
                i.setGameCharacter(null);
                i.setItem(null);
            }
            return match;
        });
    }

}
