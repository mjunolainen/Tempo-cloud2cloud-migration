package cloud2cloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WorklogDto {
    String self;
    Integer tempoWorklogId;
    Integer jiraWorklogId;

    @JsonProperty("issue")
    WorklogIssueDto worklogIssueDto;

    Integer timeSpentSeconds;
    Integer billableSeconds;
    String startDate;
    String startTime;
    String description;
    String createdAt;
    String updatedAt;

    @JsonProperty("author")
    WorklogAuthorDto worklogAuthorDto;

    @JsonProperty("attributes")
    WorklogAttributesDto worklogAttributesDto;
}
