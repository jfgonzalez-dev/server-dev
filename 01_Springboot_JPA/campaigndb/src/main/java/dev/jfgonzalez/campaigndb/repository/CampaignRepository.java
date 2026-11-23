package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.Player;

import java.util.List;


public interface CampaignRepository extends JpaRepository<Campaign, UUID>{
    List<Campaign> findByNameContainsIgnoreCase(String name);
    Page<Campaign> findByNameContainsIgnoreCase(String name, Pageable pageable);
    List<Campaign> findByOwnerAndStatusContainsIgnoreCase(Player owner, String status);
    Page<Campaign> findByOwnerAndStatusContainsIgnoreCase(Player owner, String status, Pageable pageable);
    List<Campaign> findByGameSystemIgnoreCaseAndStatusContainsIgnoreCase(String gameSystem, String status);
    Page<Campaign> findByGameSystemIgnoreCaseAndStatusContainsIgnoreCase(String gameSystem, String status, Pageable pageable);
    List<Campaign> findByOwnerIdOrderByStatus(UUID ownerId);
    Page<Campaign> findByOwnerIdOrderByStatus(UUID ownerId, Pageable pageable);
    List<Campaign> findByStatusIgnoreCase(String status);
    Page<Campaign> findByStatusIgnoreCase(String status, Pageable pageable);
    long countByStatusIgnoreCase(String status);
    List<Campaign> findTop10ByGameSystemIgnoreCaseOrderBySecondsPlayedDesc(String gameSystem);
    @Query("""
        select distinct c from Campaign c
        join c.campaignPlayers cp
        where cp.player.id = :playerId
    """)
    List<Campaign> findByPlayerId(@Param("playerId") UUID playerId);
}
