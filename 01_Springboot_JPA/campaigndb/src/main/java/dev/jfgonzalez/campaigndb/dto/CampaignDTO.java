package dev.jfgonzalez.campaigndb.dto;

import java.util.UUID;

public record CampaignDTO(
    UUID id, String name, String description, String gameSystem,
    String status, Integer secondsPlayed, UUID ownerId
) {
    
}
