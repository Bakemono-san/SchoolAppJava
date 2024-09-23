package org.SchoolApp.Datas.Request;

import lombok.Data;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Data
public class PromoRequest {
    private String libelle;
    private Date dateDebut;
    private Date dateFin;
    private boolean deleted;
    private int duree;
    private EtatEnum etat;
    private Optional<Set<ReferentielEntity>> referentiels;
}
