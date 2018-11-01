package me.personal.integration.jenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class JenkinsApplication {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(JenkinsApplication.class, args);
	}

    @RequestMapping("/hs")
    @ResponseBody
    public String test(){
        logger.info("hs");
        return "ok";
    }
}
