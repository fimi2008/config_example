package top.lionxxw.spring.cloud.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package top.lionxxw.spring.cloud.web
 * Project config_example
 *
 * Author lionxxw
 * Created on 2017/8/7 14:39
 * version 1.0.0
 */
@RestController
public class HelloController {
    @Value("${lionxxw.name}")
    private String hello;

    @RequestMapping("/hello")
    public String from() {
        return this.hello;
    }
}
