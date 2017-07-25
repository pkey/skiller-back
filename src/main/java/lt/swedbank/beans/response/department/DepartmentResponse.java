package lt.swedbank.beans.response.department;


import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Division;

import javax.validation.constraints.NotNull;

public class DepartmentResponse {

        protected Long id;
        protected String name;

        public DepartmentResponse() {
        }

        public DepartmentResponse(@NotNull Department department) {
            this.id = department.getId();
            this.name = department.getName();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

}
