package lt.swedbank.controllers;

import lt.swedbank.beans.entity.unit.Department;
import lt.swedbank.services.unit.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<Department> getAllUnits() {
        return departmentService.getAllDepartments();
    }

}
