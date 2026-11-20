package dev.jfgonzalez.campaigndb.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.campaigndb.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature,UUID> {
    List<Feature> findByCampaignId(UUID id);
}
