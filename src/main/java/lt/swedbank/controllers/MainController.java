package lt.swedbank.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Component
public class MainController {

    @RequestMapping(value = "/login")
    public @ResponseBody
    String login() {
        return "All good. You DO NOT need to be authenticated to call /login";
    }

    /* Maps to all HTTP actions by default (GET,POST,..)*/
    @RequestMapping("/register")
    public @ResponseBody
    String register() {
        return "You are registering";
    }

    @RequestMapping("/get")
    public @ResponseBody
    String get() {
        return "Should only be returned to auhtorized person";
    }


}
