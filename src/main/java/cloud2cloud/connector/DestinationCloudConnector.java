package cloud2cloud.connector;

import cloud2cloud.dto.DestinationWorklogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
}