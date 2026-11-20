package dev.jfgonzalez.campaigndb.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.campaigndb.model.Item;

public interface ItemRepository extends JpaRepository<Item,UUID> {
    List<Item> findByCampaignId(UUID id);
}
