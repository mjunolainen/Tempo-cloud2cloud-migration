package cloud2cloud.controller;

import cloud2cloud.service.TempoWorklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Cloud2CloudController {
    @Autowired
    private TempoWorklogService tempoWorklogService;

    @GetMapping("migrateWorklogs")
    public String migrateWorklogs() {
        return tempoWorklogService.migrateWorklogs();
    }

    @GetMapping("deleteDestinationWorklogs")
    public String deleteDestinationWorklogs() {
        return tempoWorklogService.deleteDestinationWorklogs();
    }


}
