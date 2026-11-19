package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.campaigndb.model.Player;
import java.util.List;



public interface PlayerRepository extends JpaRepository<Player, UUID> {
    List<Player> findByEmail(String email);
    List<Player> findByAdmin(boolean admin);
    List<Player> findByNameContainsIgnoreCaseOrderByName(String name);
    long countByAdmin(boolean admin);
}
