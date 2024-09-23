package org.SchoolApp.Datas.Request;

import lombok.Data;

@Data
public class NoteRequest {
    private float note;
    private Long apprenantId;
    private Long moduleId;
}
