package cloud2cloud.service;

import cloud2cloud.connector.DestinationCloudConnector;
import cloud2cloud.connector.SourceCloudConnector;
import cloud2cloud.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class TempoWorklogService {
    @Autowired
    private DestinationCloudConnector destinationCloudConnector;
    @Autowired
    private SourceCloudConnector sourceCloudConnector;


    public String deleteDestinationWorklogs() {
        LocalTime timeStart = LocalTime.now();
        log.info("Start time: {}", timeStart);

        WorklogListDto destinationWorklogList = destinationCloudConnector.getDestinationWorklogs();

        while (destinationWorklogList.getWorklogsMetaDataDto().getCount() > 0) {
            for (WorklogDto destinationWorklog : destinationWorklogList.getResults()) {
                destinationCloudConnector.deleteDestinationWorklog(destinationWorklog.getTempoWorklogId());
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    log.error("Async error while deleting worklogs");
                    e.printStackTrace();
                }
            }
            if (destinationWorklogList.getWorklogsMetaDataDto().getNext() != null) {
                destinationWorklogList = destinationCloudConnector.getNextDestinationWorklogs
                        (destinationWorklogList.getWorklogsMetaDataDto().getNext());
            } else {
                break;
            }
        }
        LocalTime timeEnd = LocalTime.now();
        log.info("End time: {}", timeEnd);
        return "Worklogs deleted";
    }

    public String migrateWorklogs() {
        LocalTime timeStart = LocalTime.now();
        log.info("Start time: {}", timeStart);

        WorklogListDto sourceWorklogList = sourceCloudConnector.getSourceWorklogs();

        while (sourceWorklogList.getWorklogsMetaDataDto().getCount() > 0) {

            for (WorklogDto sourceWorklog : sourceWorklogList.getResults()) {

                DestinationWorklogDto destinationWorklog = new DestinationWorklogDto();
                destinationWorklog.setIssueKey(sourceWorklog.getWorklogIssueDto().getKey());
                destinationWorklog.setTimeSpentSeconds(sourceWorklog.getTimeSpentSeconds());
                destinationWorklog.setBillableSeconds(sourceWorklog.getBillableSeconds());
                destinationWorklog.setStartDate(sourceWorklog.getStartDate());
                destinationWorklog.setStartTime(sourceWorklog.getStartTime());
                destinationWorklog.setDescription(sourceWorklog.getDescription());
                destinationWorklog.setAuthorAccountId(sourceWorklog.getWorklogAuthorDto().getAccountId());

                destinationCloudConnector.insertDestinationWorklog(destinationWorklog);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    log.error("Async error while migrating worklogs");
                    e.printStackTrace();
                }
            }
            if (sourceWorklogList.getWorklogsMetaDataDto().getNext() != null) {
                sourceWorklogList = sourceCloudConnector.getNextSourceWorklogs
                        (sourceWorklogList.getWorklogsMetaDataDto().getNext());
            } else {
                break;
            }
        }
        LocalTime timeEnd = LocalTime.now();
        log.info("End time: {}", timeEnd);
        return "Worklogs migrated";
    }
}