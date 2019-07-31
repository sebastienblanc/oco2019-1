/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebjef.merchantbo.boundary;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.java.Log;
import org.sebjef.merchantbo.entity.Payment;
import org.sebjef.merchantbo.entity.PosRef;

/**
 *
 * @author JF James
 */
@ApplicationScoped
@GraphQLClass
@Log
public class PaymentResolver {
    
    @PersistenceContext
    private EntityManager em;
    
    @GraphQLQuery
    public Optional<PosRef> posRef(@GraphQLContext Payment payment) {
        PosRef posRef = em.createNamedQuery("PosRef.findByPosId", PosRef.class).setParameter("posId", payment.getPosId()).getSingleResult();
        return Optional.of(posRef);
    }
    
}
