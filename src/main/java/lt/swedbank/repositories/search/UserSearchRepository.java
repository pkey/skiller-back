package lt.swedbank.repositories.search;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<User> search(String searchText) {


        Set<User> userResults = new HashSet<>();

        String[] keywords = processQuery(searchText);

        boolean isResultEmpty = true;

        for (String keyword : keywords) {

            Set<User> userTempResults = new HashSet<>();

            Set<User> skillResults = resultsByKeyword(keyword);
            //Set<User> userNameResults = resultsByUserName(keyword);
            //Set<User> userLastNameResults = resultsByUserLastName(keyword);
            //Set<User> skillResults = resultsBySkillProperties(keyword);

            userTempResults.addAll(skillResults);
            //userTempResults.addAll(userNameResults);
            //userTempResults.addAll(userLastNameResults);
            //userTempResults.addAll(skillResults);

            if (isResultEmpty) {
                userResults.addAll(userTempResults);
                isResultEmpty = false;
            } else {
                userResults.retainAll(userTempResults);
            }
        }

        return userResults;//sortUserEntityResponse(convertUserSetToUserResponseList(userResults));

    }

    private Set<User> resultsByKeyword(String keyword) {


        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        .get();

        Query query = queryBuilder.keyword().wildcard().onFields("name", "lastName", "userSkills.skill.title").matching("*" + keyword + "*").createQuery();

        //Converts into full text query
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

        //Return results
        return new HashSet<User>(jpaQuery.getResultList());

    }


    private Set<User> resultsByUserName(String keyword) {
        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        .get();

        Query query = queryBuilder.keyword().wildcard().onField("name").matching("*" + keyword + "*").createQuery();

        //Converts into full text query
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

        //Return results
        return new HashSet<User>(jpaQuery.getResultList());

    }


    private Set<User> resultsByUserLastName(String keyword) {
        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        .get();

        Query query = queryBuilder.keyword().wildcard().onField("lastName").matching("*" + keyword + "*").createQuery();

        //Converts into full text query
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

        //Return results
        return new HashSet<User>(jpaQuery.getResultList());


    }

    private Set<User> resultsBySkillProperties(String keyword) {
        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Skill.class)
                        .get();

        Query query = queryBuilder.keyword().wildcard().onField("title").matching("*" + keyword + "*").createQuery();

        //Converts into full text query
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Skill.class);

        //Return results
        return getUsersFromSkills(jpaQuery.getResultList());

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

