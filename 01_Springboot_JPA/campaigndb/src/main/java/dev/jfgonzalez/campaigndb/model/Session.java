package dev.jfgonzalez.campaigndb.model;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table (name = "session")
public class Session {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (nullable = false)
    private ZonedDateTime date;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column (columnDefinition = "json", nullable = false)
    private Map<String, Object> data;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "campaign_id", nullable = false)
    private Campaign campaign;
}
