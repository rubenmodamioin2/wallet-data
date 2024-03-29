package es.in2.wallet.data.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VCTypeListDTO {
    @JsonProperty("vcTypes")
    private List<String> vcTypes;
}
