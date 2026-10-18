package dev.jfgonzalez.campaigndb.model;

import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Entity 
@Table (name = "inventory")
public class Inventory {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "game_character_id")
    private GameCharacter gameCharacter;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "item_id")
    private Item item;
    
    @Column (nullable = false)
    private int quantity;

    @Column (name = "is_equipped", nullable = false)
    private boolean isEquipped;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column (columnDefinition = "json", nullable = false)
    private Map<String, Object> data;
}
