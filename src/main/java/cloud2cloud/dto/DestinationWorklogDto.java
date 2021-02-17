package cloud2cloud.dto;

import lombok.Data;

@Data
public class DestinationWorklogDto {
    String issueKey;
    Integer timeSpentSeconds;
    Integer billableSeconds;
    String startDate;
    String startTime;
    String description;
    String authorAccountId;
}