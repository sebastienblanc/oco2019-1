/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebjef.merchantbo.boundary;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.sebjef.merchantbo.entity.Payment;

/**
 * See: https://github.com/kumuluz/kumuluzee-graphql
 * 
 * @author JF James
 */
@RequestScoped
@GraphQLClass
public class GraphqhQLAPI {

    @PersistenceContext
    private EntityManager em;

    // @GraphQLEnvironment enables JPA queries optimization: GraphQLUtils will then extract the queried fields and retrieve only them from JPA
    @GraphQLQuery(name = "allPayments", description="Retrieve all payements")
    public List<Payment> allPayemnts(@GraphQLArgument(name = "sort") Sort sort, @GraphQLArgument(name="filter") Filter filter, @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.processWithoutPagination(em, Payment.class, resolutionEnvironment, sort, filter);
    }
    
}
