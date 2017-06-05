package lt.swedbank.controllers.department;

import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.services.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Iterable<DepartmentEntityResponse> getAll() {
        return departmentService.getAllDepartmentEntityResponseList();
    }

}
