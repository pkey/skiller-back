package lt.swedbank.repositories;


import lt.swedbank.beans.entity.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class UserSearch {

    @PersistenceContext
    private EntityManager entityManager;

    public List search(String text) {

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(User.class)
                        // .overridesForField( "name", "customanalyzer_query" )
                        .get();

        String queryText = "petras juozas";

        String[] qeueries = {"*etra*", "*uoza*"};

        // a very basic query by keywords
        BooleanJunction bool =
                queryBuilder.bool();

        for (String element: qeueries) {
            bool.must(queryBuilder.keyword().wildcard().onFields("name", "lastName").matching(element).createQuery());
        }

        Query query = bool.createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, User.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List results = jpaQuery.getResultList();

        return results;
    } // method search




}

