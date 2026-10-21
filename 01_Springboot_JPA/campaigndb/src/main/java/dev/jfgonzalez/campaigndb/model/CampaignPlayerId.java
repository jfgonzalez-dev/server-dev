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
public class CampaignPlayerId implements Serializable {
    @Column(name = "campaign_id")
    private UUID campaignId;

    @Column(name = "player_id")
    private UUID playerId;
}