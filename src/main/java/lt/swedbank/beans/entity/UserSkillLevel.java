package lt.swedbank.beans.entity;

import lt.swedbank.exceptions.request.FalseRequestStatusException;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class UserSkillLevel {

    private static final String APPROVED = "approved";
    private static final String DISAPPROVED = "disapproved";
    private static final String PENDING = "pending";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserSkill userSkill;

    @ManyToOne
    private SkillLevel skillLevel;

    private String motivation;

    private Integer isApproved = 0;

    @CreationTimestamp
    private Date validFrom;

    private Date validUntil;

    @OneToMany(mappedBy = "userSkillLevel")
    private List<Vote> votes = new ArrayList<>();

    public UserSkillLevel() {
    }

    public UserSkillLevel(UserSkill userSkill, SkillLevel skillLevel) {
        this.userSkill = userSkill;
        this.skillLevel = skillLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSkill getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(UserSkill userSkill) {
        this.userSkill = userSkill;
    }

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public String getCurrentSkillLevelStatus() {

        switch (isApproved) {
            case -1:
                return DISAPPROVED;
            case 0:
                return PENDING;
            case 1:
                return APPROVED;
            default:
                throw new FalseRequestStatusException();
        }

    }
}
