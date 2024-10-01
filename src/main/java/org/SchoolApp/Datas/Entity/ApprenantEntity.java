package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@ToString
public class ApprenantEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomTuteur;
    private String prenomTuteur;
    private String contactTuteur;
    private String cniFile;
    private String extraitFile;
    private String diplomeFile;
    private String casierFile;
    private String photoCouverture;

    // New fields for matricule and QR code link
    @Column(unique = true)
    private String matricule;

    private String qrCodeLink; // Field to store the QR code link

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etat_id", nullable = true)
    @JsonIgnore
    @ToString.Exclude
    private EtatApprenant etatApprenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referentiel_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private ReferentielEntity referentiel;

    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    private List<NotesEntity> notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private PromoEntity promo;

}
