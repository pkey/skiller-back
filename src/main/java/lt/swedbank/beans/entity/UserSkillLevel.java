package lt.swedbank.beans.entity;

import lt.swedbank.beans.enums.Status;
import lt.swedbank.exceptions.request.FalseRequestStatusException;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class UserSkillLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserSkill userSkill;

    @ManyToOne
    private SkillLevel skillLevel;

    private String motivation;

    private Status status = Status.APPROVED;

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
        this.votes = Collections.EMPTY_LIST;
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

    public Status getStatus() {
        return status;
    }

    public void setPending() {
        this.status = Status.PENDING;
    }

    public void setApproved() {
        this.status = Status.APPROVED;
    }

    public void setDisapproved() {
        this.status = Status.DISAPPROVED;
    }

    public String getCurrentSkillLevelStatus() {
        return status.toString();
    }
}
