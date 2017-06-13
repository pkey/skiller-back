package lt.swedbank.beans.entity;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Indexed
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Skill skill;

    @OneToMany(mappedBy = "userSkill", orphanRemoval = true)
    private List<UserSkillLevel> userSkillLevels;

    public UserSkill() {
    }

    public UserSkill(User user, Skill skill) {
        this.skill = skill;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getTitle() {
        return skill.getTitle();
    }

    public List<UserSkillLevel> getUserSkillLevels() {
        return userSkillLevels;
    }

    public void setUserSkillLevels(List<UserSkillLevel> userSkillLevels) {
        this.userSkillLevels = userSkillLevels;
    }

    public void setUserSkillLevel(UserSkillLevel userSkillLevel) {

        if(userSkillLevels == null){
            userSkillLevels = new ArrayList<>();
        }

        this.userSkillLevels.add(userSkillLevel);
    }

    public UserSkillLevel getCurrentUserSkillLevel() {
        UserSkillLevel currentUserSkillLevel;

        userSkillLevels.sort(new Comparator<UserSkillLevel>() {
            @Override
            public int compare(UserSkillLevel o1, UserSkillLevel o2) {
                return o2.getValidFrom().compareTo(o1.getValidFrom());
            }
        });


        currentUserSkillLevel = userSkillLevels.get(0);

        return currentUserSkillLevel;
    }


}
