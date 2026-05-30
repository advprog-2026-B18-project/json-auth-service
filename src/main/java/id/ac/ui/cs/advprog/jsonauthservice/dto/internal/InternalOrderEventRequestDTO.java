package id.ac.ui.cs.advprog.jsonauthservice.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InternalOrderEventRequestDTO {

    @NotBlank
    @JsonProperty("event")
    private String event;
}
