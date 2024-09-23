package org.SchoolApp.Datas.Request;

import lombok.Data;
import org.SchoolApp.Datas.Entity.ReferentielEntity;

import java.util.Date;
import java.util.List;

@Data
public class PromoReferentiel {
    private String libelle;
    private String code;
    private String description;

}
