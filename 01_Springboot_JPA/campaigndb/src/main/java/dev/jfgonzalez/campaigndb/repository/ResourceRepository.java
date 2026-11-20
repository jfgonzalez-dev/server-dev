package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.campaigndb.model.Resource;
import java.util.List;


public interface ResourceRepository extends JpaRepository<Resource,UUID> {
    List<Resource> findByCampaignId(UUID id);    
}
