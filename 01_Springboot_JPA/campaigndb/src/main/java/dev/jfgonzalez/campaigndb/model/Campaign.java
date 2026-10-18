package dev.jfgonzalez.campaigndb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Entity 
@Table(name = "campaign")
public class Campaign {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    
    @Column (nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> data;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn (name = "owner_id", nullable = false)
    private Player owner;

    @ToString.Exclude
    @OneToMany (
        mappedBy = "campaign",
        fetch = FetchType.LAZY
    )
    private List<GameCharacter> gameCharacters = new ArrayList<>();

    public void addGameCharacter(GameCharacter gameCharacter) {
        gameCharacters.add(gameCharacter);
        gameCharacter.setCampaign(this);
    }

    public void removeGameCharacter(GameCharacter gameCharacter) {
        gameCharacters.remove(gameCharacter);
        gameCharacter.setCampaign(null);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "campaign",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Scene> scenes = new ArrayList<>();

    public void addScene(Scene scene) {
        scenes.add(scene);
        scene.setCampaign(this);
    }

    public void removeScene(Scene scene) {
        scenes.remove(scene);
        scene.setCampaign(null);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "campaign",
        fetch = FetchType.LAZY
    )
    private List<WikiArticle> wikiArticles = new ArrayList<>();

    public void addWikiArticle(WikiArticle wikiArticle) {
        wikiArticles.add(wikiArticle);
        wikiArticle.setCampaign(this);
    }

    public void removeWikiArticle(WikiArticle wikiArticle) {
        wikiArticles.remove(wikiArticle);
        wikiArticle.setCampaign(null);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "campaign",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
        session.setCampaign(this);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        session.setCampaign(null);
    }

    @ToString.Exclude
    @OneToMany (
        mappedBy = "campaign",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CampaignPlayer> campaignPlayers = new ArrayList<>();

    public CampaignPlayer addPlayer(Player player, CampaignRole role) {
        CampaignPlayer campaignPlayer = new CampaignPlayer();
        campaignPlayer.setCampaign(this);
        campaignPlayer.setPlayer(player);
        campaignPlayer.setRole(role);

        this.campaignPlayers.add(campaignPlayer);
        return campaignPlayer;
    }

    public void removePlayer(Player player) {
        this.campaignPlayers.removeIf(cp -> {
            boolean match = cp.getPlayer().equals(player);
            if (match) {
                cp.setCampaign(null);
                cp.setPlayer(null);
            }
            return match;
        });
    }

}
