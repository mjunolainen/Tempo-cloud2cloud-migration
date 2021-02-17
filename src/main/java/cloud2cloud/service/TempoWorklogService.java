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

        WorklogListDto destinationWorklogList = destinationCloudConnector.getDestinationWorklogs();

        //while (destinationWorklogList.getWorklogsMetaDataDto().getNext() != null) {

            for (WorklogDto destinationWorklog : destinationWorklogList.getResults()) {
                destinationCloudConnector.deleteDestinationWorklog(destinationWorklog.getTempoWorklogId());
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    log.error("Async error while deleting worklogs");
                    e.printStackTrace();
                }
            }
            //destinationWorklogList = destinationCloudConnector.getNextDestinationWorklogs
              //      (destinationWorklogList.getWorklogsMetaDataDto().getNext());


        LocalTime timeEnd = LocalTime.now();
        log.info("Start time: {}", timeStart);
        log.info("End time: {}", timeEnd);
        return "Worklogs deleted";
    }

    public String migrateWorklogs() {
        LocalTime timeStart = LocalTime.now();

        WorklogListDto sourceWorklogList = sourceCloudConnector.getSourceWorklogs();

        while (sourceWorklogList.getWorklogsMetaDataDto().getNext() != null) {

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
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    log.error("Async error while migrating worklogs");
                    e.printStackTrace();
                }
            }
            sourceWorklogList = sourceCloudConnector.getNextSourceWorklogs
                    (sourceWorklogList.getWorklogsMetaDataDto().getNext());
        }

        LocalTime timeEnd = LocalTime.now();
        log.info("Start time: {}", timeStart);
        log.info("End time: {}", timeEnd);
        return "Worklogs migrated";
    }
}
