package lt.swedbank.controllers.notification;

import lt.swedbank.services.notification.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;


}
