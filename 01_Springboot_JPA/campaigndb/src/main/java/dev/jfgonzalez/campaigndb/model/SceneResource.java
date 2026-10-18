package dev.jfgonzalez.campaigndb.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Entity 
@Table (name = "scene_resource")
public class SceneResource {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "scene_id")
    private Scene scene;
    
    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "resource_id")
    private Resource resource;

    @Column (nullable = false)
    private float posX;

    @Column (nullable = false)
    private float posY;

    @Column (nullable = false)
    private boolean isHidden;
    
}
