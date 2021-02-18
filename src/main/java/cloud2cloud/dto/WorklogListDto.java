package cloud2cloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WorklogListDto {
    String self;
    @JsonProperty("metadata")
    WorklogsMetaDataDto worklogsMetaDataDto;
    @JsonProperty("results")
    List<WorklogDto> results = null;
}
