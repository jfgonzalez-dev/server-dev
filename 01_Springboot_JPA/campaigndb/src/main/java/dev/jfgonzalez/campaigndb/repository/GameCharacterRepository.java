package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.jfgonzalez.campaigndb.model.GameCharacter;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, UUID>{
    @Transactional
    List<GameCharacter> deleteByOwnerId(UUID ownerId);
    @Transactional
    List<GameCharacter> deleteByPcFalseAndCampaignId(UUID campaignId);
    @Transactional
    List<GameCharacter> deleteByPcFalseAndCampaignIsNull();
    List<GameCharacter> findByCampaignIdAndPcTrue(UUID campaignId);
    @Query("""
        select gc from GameCharacter gc
        where gc.campaign.id = :campaignId
        and (
            (:comparator = 'EQ' and gc.level = :lvl) or
            (:comparator = 'GT' and gc.level > :lvl) or
            (:comparator = 'LT' and gc.level < :lvl)
        )
        order by gc.level desc
    """)
    List<GameCharacter> findByCampaignIdCompareTo(
        @Param("campaignId") UUID campaignId,
        @Param("comparator") String comparator,
        @Param("lvl") int lvl
    );
}
