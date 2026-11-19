package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.Player;

import java.util.List;


public interface CampaignRepository extends JpaRepository<Campaign, UUID>{
    List<Campaign> findByOwnerAndStatusContainsIgnoreCase(Player owner, String status);
    List<Campaign> findByGameSystemIgnoreCaseAndStatusContainsIgnoreCase(String gameSystem, String status);
    List<Campaign> findByOwnerIdOrderByStatus(UUID ownerId);
    long countByStatusIgnoreCase(String status);
    List<Campaign> findTop10ByGameSystemIgnoreCaseOrderBySecondsPlayedDesc(String gameSystem);
    @Query("""
        select distinct c from Campaign c
        join c.campaignPlayers cp
        where cp.player.id = :playerId
    """)
    List<Campaign> findByPlayerId(@Param("playerId") UUID playerId);
}
