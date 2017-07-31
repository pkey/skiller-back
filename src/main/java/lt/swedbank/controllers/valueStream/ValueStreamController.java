package lt.swedbank.controllers.valueStream;

import lt.swedbank.beans.response.valueStream.ValueStreamResponse;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/stream")
public class ValueStreamController {

    @Autowired
    private ValueStreamService valueStreamService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Iterable<ValueStreamResponse> getAll() {
        return valueStreamService.getAllValueStreamEntityResponseList();
    }
}
