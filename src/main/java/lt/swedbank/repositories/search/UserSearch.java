package lt.swedbank.repositories.search;


import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.UserEntityResponse;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserSearch {

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

    public List<UserEntityResponse> search(String searchText) {
        //Initialises full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        //Forms user class query builder
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        .get();

        //Splits query keywords and fors an array
        String[] qeueries = processQuery(searchText);


        //Initialises boolean query builder (permits chaining queries)
        BooleanJunction bool =
                queryBuilder.bool();

        //Chains queries based on number of keywords
        for (String element : qeueries) {
            bool.must(queryBuilder.keyword().wildcard().onFields("name", "lastName").matching("*" + element + "*").createQuery());
        }

        //Finishes creating a query
        Query query = bool.createQuery();

        //Converts into full text query
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

        //Return results
        List results = jpaQuery.getResultList();

        //Converts result to response entity
        return convertUserListToUserResponseList(results);
    }


    private String[] processQuery(String query) {
        String[] queries = query.toLowerCase().split(" +");
        return queries;
    }

    //TODO Method is widely used, should be somewhere else
    private List<UserEntityResponse> convertUserListToUserResponseList(List<User> userList) {
        List<UserEntityResponse> responseList = new ArrayList<>();
        for (User user : userList) {
            responseList.add(new UserEntityResponse(user));
        }
        return responseList;
    }
}

