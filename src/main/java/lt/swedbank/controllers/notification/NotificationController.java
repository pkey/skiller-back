package lt.swedbank.controllers.notification;


import lt.swedbank.beans.response.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/notification")
public class NotificationController {
    @RequestMapping(value = "all/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Response getNotificationById(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return null;
    }

}


