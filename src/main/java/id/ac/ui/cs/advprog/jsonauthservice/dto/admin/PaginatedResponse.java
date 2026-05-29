package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private PaginationData pagination;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PaginationData {
        private int page;
        private int limit;
        private long total;
        private int totalPages;
    }
}