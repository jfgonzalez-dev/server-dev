package dev.jfgonzalez.campaigndb.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.campaigndb.model.WikiArticle;
import java.util.List;


public interface WikiArticleRepository extends JpaRepository<WikiArticle,UUID> {
    List<WikiArticle> findByCampaignId(UUID id);
}
