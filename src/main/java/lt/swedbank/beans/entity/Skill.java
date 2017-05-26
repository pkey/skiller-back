package lt.swedbank.beans.entity;




import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

@Entity
@Indexed
public class Skill {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    @Column(unique = true)
    private String title;

    public Skill(String title)
    {
        this.setTitle(title);
    }

    public Skill() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
