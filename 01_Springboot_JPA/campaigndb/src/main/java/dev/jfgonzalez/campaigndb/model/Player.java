package dev.jfgonzalez.campaigndb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor  @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Entity 
@Table (name = "player")
public class Player {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (nullable = false)
    private String name;
    
    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private boolean isAdmin;

    @ToString.Exclude
    @OneToMany (
        mappedBy = "owner",
        fetch = FetchType.LAZY
    )
    private List<Campaign> ownedCampaigns = new ArrayList<>();

    public void addOwnedCampaign(Campaign campaign) {
        ownedCampaigns.add(campaign);
        campaign.setOwner(this);
    }

    public void removeOwnedCampaign(Campaign campaign) {
        ownedCampaigns.remove(campaign);
        campaign.setOwner(null);
    }
    
    @ToString.Exclude
    @OneToMany (
        mappedBy = "player",
        fetch = FetchType.LAZY
    )
    private List<GameCharacter> gameCharacters = new ArrayList<>();

    public void addGameCharacter(GameCharacter gameCharacter) {
        gameCharacters.add(gameCharacter);
        gameCharacter.setPlayer(this);
    }

    public void removeGameCharacter(GameCharacter gameCharacter) {
        gameCharacters.remove(gameCharacter);
        gameCharacter.setPlayer(null);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "author",
        fetch = FetchType.LAZY
    )
    private List<WikiArticle> authoredWikiArticles = new ArrayList<>();

    public void addAuthoredWikiArticle(WikiArticle wikiArticle) {
        authoredWikiArticles.add(wikiArticle);
        wikiArticle.setAuthor(this);
    }

    public void removeAuthoredWikiArticle(WikiArticle wikiArticle) {
        authoredWikiArticles.remove(wikiArticle);
        wikiArticle.setAuthor(null);
    }
    
}
