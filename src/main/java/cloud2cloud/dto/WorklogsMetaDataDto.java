package cloud2cloud.dto;

import lombok.Data;

@Data
public class WorklogsMetaDataDto {
    Integer count;
    Integer offset;
    Integer limit;
    String next;
}
