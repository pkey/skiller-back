package lt.swedbank.beans.response.department;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.user.UserResponse;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentOverviewResponse extends Response {

    @JsonUnwrapped
    @NonNull
    private DepartmentResponse departmentResponse;
    @NonNull
    private Set<UserResponse> users;
    @NonNull
    private Set<SkillTemplateResponse> skillTemplate;

}
