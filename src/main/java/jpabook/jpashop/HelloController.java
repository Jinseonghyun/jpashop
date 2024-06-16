package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!!");  // data 라는 hello.html 의 키의 값을 hello!!! 넘길거다.
        return "hello"; // 관례상 화면이름 (templates > hello.html)
    }
}
