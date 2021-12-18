package kh.com.sathapana.store.service.entity.category;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Language implements Serializable {
    private String km;
    private String en;
}
