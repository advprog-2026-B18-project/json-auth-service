package id.ac.ui.cs.advprog.jsonauthservice.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class SocialMediaLink {
    private String platform;
    private String url;
}
