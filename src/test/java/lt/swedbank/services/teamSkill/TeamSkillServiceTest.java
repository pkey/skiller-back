package lt.swedbank.services.teamSkill;

import lt.swedbank.repositories.TeamSkillRepository;
import lt.swedbank.services.skill.UserSkillService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TeamSkillServiceTest {

    @InjectMocks
    private TeamSkillService teamSkillService;
    @Mock
    private TeamSkillRepository teamSkillRepository;
    @Mock
    private UserSkillService userSkillService;

    @Before
    public void setUp() throws Exception {
    }

}