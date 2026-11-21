package dev.jfgonzalez.campaigndb.dto;

import java.util.UUID;

public record GameCharacterDTO(
    UUID id, String name, String background, Boolean pc,
    Integer level, String avatarUrl, UUID campaignId, UUID ownerId
) {
    
}