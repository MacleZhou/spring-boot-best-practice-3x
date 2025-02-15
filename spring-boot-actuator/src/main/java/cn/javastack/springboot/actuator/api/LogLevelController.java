package cn.javastack.springboot.actuator.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log-level")
public class LogLevelController {

    @GetMapping("/print")
    public Object print() {
        log.info("info log...") ;
        log.debug("debug log...") ;
        log.trace("trace log...") ;
        log.error("error log...") ;
        return "print" ;
    }

}
