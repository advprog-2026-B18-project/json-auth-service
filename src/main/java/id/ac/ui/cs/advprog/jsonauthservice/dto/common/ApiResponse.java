package id.ac.ui.cs.advprog.jsonauthservice.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ErrorDetail> errors;
}
