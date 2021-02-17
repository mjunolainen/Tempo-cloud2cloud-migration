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
        return null;
    }

    public String migrateWorklogs() {
        LocalTime timeStart = LocalTime.now();
        Integer worklogCountFromCloud = 0;

        WorklogListDto sourceWorklogList = sourceCloudConnector.getSourceWorklogs();
        log.info("Self: {}", sourceWorklogList.getSelf());
        log.info("Next: {}", sourceWorklogList.getWorklogsMetaDataDto().getNext());

        while (sourceWorklogList.getWorklogsMetaDataDto().getNext() != null) {
            worklogCountFromCloud = worklogCountFromCloud + sourceWorklogList.getWorklogsMetaDataDto().getCount();
            log.info("Worklogs from cloud: {}", worklogCountFromCloud);

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
            }
            log.info(sourceWorklogList.getWorklogsMetaDataDto().getNext());
            sourceWorklogList = sourceCloudConnector.getNextSourceWorklogs(sourceWorklogList.getWorklogsMetaDataDto().getNext());
            log.info("Next: {}", sourceWorklogList.getWorklogsMetaDataDto().getNext());
        }

        worklogCountFromCloud = worklogCountFromCloud + sourceWorklogList.getWorklogsMetaDataDto().getCount();
        log.info("Worklogs from cloud: {}", worklogCountFromCloud);

        LocalTime timeEnd = LocalTime.now();
        log.info("Start time: {}", timeStart);
        log.info("End time: {}", timeEnd);
        return "Worklogs migrated";
    }
}
