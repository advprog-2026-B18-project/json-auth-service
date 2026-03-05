package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationDTO {
    private int page;
    private int limit;
    private long total;
}
