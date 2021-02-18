package cloud2cloud.connector;

import cloud2cloud.dto.DestinationWorklogDto;
import cloud2cloud.dto.WorklogListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DestinationCloudConnector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tempo.cloud.url}")
    private String tempoCloudUrl;

    @Value("${destination.tempo.token}")
    private String destinationTempoToken;

    @Value("${destination.tempo.project.key}")
    private String destinationTempoProjectKey;

    @Async
    public Boolean insertDestinationWorklog(DestinationWorklogDto destinationWorklog) {
        try {
            restTemplate.exchange(tempoCloudUrl + "/worklogs", HttpMethod.POST,
                    getEntityInsertWorklogs(destinationWorklog), void.class);
            return true;
        } catch (HttpStatusCodeException sce) {
            if (sce.getStatusCode() == HttpStatus.FORBIDDEN) {
            }
            if (sce.getStatusCode() == HttpStatus.BAD_REQUEST) {
            }
            if (sce.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            }
            return false;
        }
    }

    public WorklogListDto getDestinationWorklogs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(destinationTempoToken);
            HttpEntity httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<WorklogListDto> usage = restTemplate.exchange(tempoCloudUrl +
                            "/worklogs/project/" + destinationTempoProjectKey + "?offset=0&limit=1000",
                    HttpMethod.GET, httpEntity, WorklogListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    @Async
    public void deleteDestinationWorklog(Integer tempoWorklogId) {
        try {
            restTemplate.exchange(tempoCloudUrl + "/worklogs/{tempoWorklogId}", HttpMethod.DELETE,
                    getEntity(), void.class, tempoWorklogId);
        } catch (HttpStatusCodeException sce) {
            if (sce.getStatusCode() == HttpStatus.FORBIDDEN) {
            }
            if (sce.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            }
        }
    }

    private HttpEntity getEntity() {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(null, headers);
        return httpEntity;
    }

    private HttpEntity getEntityInsertWorklogs(DestinationWorklogDto worklog) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(worklog, headers);
        return httpEntity;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(destinationTempoToken);
        return headers;
    }

    public WorklogListDto getNextDestinationWorklogs(String nextDestinationWorklogUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(destinationTempoToken);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<WorklogListDto> usage = restTemplate.exchange(nextDestinationWorklogUrl,
                    HttpMethod.GET, httpEntity, WorklogListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }
}