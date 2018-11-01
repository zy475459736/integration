package me.personal.integration.jenkins.controller;


import me.personal.integration.jenkins.models.FinishInfo;
import me.personal.integration.jenkins.service.FastTestJenkinsService;
import org.springframework.web.bind.annotation.*;

@RestController

public class FinishController {

    @RequestMapping(value = "/fasttest/finish.json",method = RequestMethod.GET)
    public @ResponseBody
    FinishInfo isFinish(@RequestParam(value = "job_id", required = true)int job_id) {

        FastTestJenkinsService fastTestJenkinsService = new FastTestJenkinsService();

        boolean isFinish = fastTestJenkinsService.isJobFinish(job_id);

        FinishInfo finishInfo = new FinishInfo();

        finishInfo.setFinished(isFinish);

        return finishInfo;

    }
}
