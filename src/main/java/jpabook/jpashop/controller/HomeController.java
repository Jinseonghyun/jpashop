package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // Logger 를 뽑아줌 옆의 코드를 대신함 // Logger log = LoggerFactory.getLogger(getClass());
public class HomeController {

    @RequestMapping("/") // 첫번째 화면으로 잡아준다.
    public String home() {
        log.info("Home controller");
        return "home"; // home.html 로 찾아간다.
    }
}
