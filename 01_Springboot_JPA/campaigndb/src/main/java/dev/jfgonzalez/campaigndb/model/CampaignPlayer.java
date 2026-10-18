package dev.jfgonzalez.campaigndb.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table (name = "campaign_player")
public class CampaignPlayer {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "campaign_id")
    private Campaign campaign;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "player_id")
    private Player player;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private CampaignRole role;
}
