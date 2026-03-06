package id.ac.ui.cs.advprog.jsonauthservice.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDetail {
    private String field;
    private String error;

    public ErrorDetail() {
    }

    public ErrorDetail(String field, String error) {
        this.field = field;
        this.error = error;
    }
}

