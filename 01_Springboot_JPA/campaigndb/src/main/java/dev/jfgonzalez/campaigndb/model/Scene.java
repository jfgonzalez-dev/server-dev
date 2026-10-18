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
@Table (name = "scene")
public class Scene {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (nullable = false)
    private String name;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column (columnDefinition = "json", nullable = false)
    private Map<String, Object> data;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ToString.Exclude
    @OneToMany (
        mappedBy = "scene",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<SceneResource> sceneResources = new ArrayList<>();

    public SceneResource addResource(Resource resource, float posX, float posY, boolean isHidden) {
        SceneResource sceneResource = new SceneResource();
        sceneResource.setScene(this);
        sceneResource.setResource(resource);
        sceneResource.setPosX(posX);
        sceneResource.setPosY(posY);
        sceneResource.setHidden(isHidden);

        this.sceneResources.add(sceneResource);
        return sceneResource;
    }

    public void removeResource(SceneResource sceneResource) {
        if (this.sceneResources.remove(sceneResource)) {
            sceneResource.setScene(null);
            sceneResource.setResource(null);
        }
    }
    
}
