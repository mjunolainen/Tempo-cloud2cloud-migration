package cloud2cloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WorklogAttributesDto {
    String self;
    @JsonProperty("values")
    List<WorklogAttributesValuesDto> values = null;
}
