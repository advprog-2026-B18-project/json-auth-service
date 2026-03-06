package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KycListResponseDTO {
    private List<KycListItemDTO> data;
    private PaginationDTO pagination;
}
