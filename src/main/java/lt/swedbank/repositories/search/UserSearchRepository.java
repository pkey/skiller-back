package lt.swedbank.repositories.search;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.SearchUserResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.userSkill.SearchSkillResponse;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.*;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
@SuppressWarnings("unchecked")
public class UserSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    //Happens on startup
    @PostConstruct
    public void init() {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (Exception e) {
            System.out.println(
                    "An error occurred trying to build the serach index: " +
                            e.toString());
        }
    }

    public List<SearchUserResponse> search(String searchText) {

        List<SearchUserResponse> userResults = new LinkedList<>();

        String[] keywords = processQuery(searchText);

        boolean isResultEmpty = true;

        for (String keyword : keywords) {

            List<SearchUserResponse> userTempResults = new LinkedList<>();

            List<SearchUserResponse> skillResults = resultsByKeyword(keyword);

            userTempResults.addAll(skillResults);

            if (isResultEmpty) {
                userResults.addAll(userTempResults);
                isResultEmpty = false;
            } else {
                userResults.retainAll(userTempResults);
            }
        }

        return userResults;//sortUserEntityResponse(convertUserSetToUserResponseList(userResults));

    }

    private List<SearchUserResponse> resultsByKeyword(String keyword) {

        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        .get();


        Query query = queryBuilder.keyword().wildcard().onFields("name", "lastName", "userSkills.skill.title").
                matching("*" + keyword + "*").createQuery();

        
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class).setProjection("name","lastName", "userSkills.skills.title");


        List<SearchUserResponse> userResponses = new LinkedList<>();
        List<Object> result = (List<Object>) jpaQuery.getResultList();

      Iterator itr = result.iterator();
        while(itr.hasNext()){
            Object[] obj = (Object[]) itr.next();
            //now you have one array of Object for each row
            String name = String.valueOf(obj[0]); // don't know the type of column CLIENT assuming String
            String lastname = String.valueOf(obj[1]); //SERVICE assumed as int
            String title = String.valueOf(obj[2]);
            System.out.println(title);


            SearchUserResponse userResponse = new SearchUserResponse();
            userResponse.setLastname(lastname);
            userResponse.setName(name);
            userResponses.add(userResponse);
        }
        //Return results
        return userResponses;

    }


    private Set<User> getUsersFromSkills(List<Skill> skills) {
        Set<User> users = new HashSet<>();
        for (Skill skill : skills) {
            for (UserSkill userSkill : skill.getUserSkills()) {
                users.add(userSkill.getUser());
            }

        }
        return users;
    }

    private String[] processQuery(String query) {
        String[] queries = query.toLowerCase().split(" +");
        return queries;
    }


}

