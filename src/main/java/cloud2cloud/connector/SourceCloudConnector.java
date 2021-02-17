package cloud2cloud.connector;

import cloud2cloud.dto.WorklogListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SourceCloudConnector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tempo.cloud.url}")
    private String tempoCloudUrl;

    @Value("${source.tempo.token}")
    private String sourceTempoToken;

    public WorklogListDto getSourceWorklogs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sourceTempoToken);
            HttpEntity httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<WorklogListDto> usage = restTemplate.exchange(tempoCloudUrl +
                            "/worklogs/project/CUST360?offset=0&limit=1000",
                    HttpMethod.GET, httpEntity, WorklogListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public WorklogListDto getNextSourceWorklogs(String nextSourceWorklogUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sourceTempoToken);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<WorklogListDto> usage = restTemplate.exchange(nextSourceWorklogUrl,
                    HttpMethod.GET, httpEntity, WorklogListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }
}