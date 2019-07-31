/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebjef.smartbank.boundary;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.REQUIRED;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.sebjef.smartbank.entity.BankAuthor;

/**
 * Simulates an authorization request
 *
 * @author JF James
 */
@Path("authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped // Required with Payara to enable CDI injection
public class BankAuthorResource {

    @Context
    private UriInfo uriInfo;

    @PersistenceContext(unitName = "smartbank")
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "author.max.amount", defaultValue = "50000")
    private int maxAmount;
    
    @GET
    @Operation(operationId = "Retrieve all payment authorizations")
    public List<BankAuthor> findAll() {
        return em.createNamedQuery("BankAuthor.findAll", BankAuthor.class).getResultList();
    }

    @GET
    @Path("{id}")
    @Operation(operationId = "Retrieve a given payment authorization by its id")
    public BankAuthor findById(@Parameter(description = "The authorization id to be retrieved", required = true)
            @PathParam(value = "id") long id) {
        return em.find(BankAuthor.class, id);
    }

    @GET
    @Path("/count")
    @Operation(operationId = "Count all payment authorizations")
    public long count() {
        return em.createNamedQuery("BankAuthor.count", Long.class).getSingleResult();
    }

    private void store(BankAuthorResponse response) {
        BankAuthor author = new BankAuthor();

        author.setDateTime(LocalDateTime.now());
        author.setMerchantId(response.getMerchantId());
        author.setAmount(response.getAmount());
        author.setCardNumber(response.getCardNumber());
        author.setAuthorized(response.isAuthorized());
        author.setExpiryDate(response.getExpiryDate());

        em.persist(author);
        em.flush(); // required to get the id

        response.setAuthorId(author.getId());

    }

    private BankAuthorResponse initResponse(BankAuthorRequest request) {
        BankAuthorResponse response = new BankAuthorResponse();

        response.setAmount(request.getAmount());
        response.setMerchantId(request.getMerchantId());
        response.setCardNumber(request.getCardNumber());
        response.setExpiryDate(request.getExpiryDate());

        return response;
    }

    @POST
    @APIResponses(
            value = {
                @APIResponse(responseCode = "201", description = "Authorization processed", content = @Content(mediaType = "application/json"))
            }
    )
    @Operation(operationId = "Deliver (or not) a payment authorization")
    @Transactional(REQUIRED)
    public Response authorize(@Valid @NotNull BankAuthorRequest request) {

        BankAuthorResponse response = initResponse(request);

        response.setAuthorized(request.getAmount() <= maxAmount);

        store(response);

        URI paymentUri = uriInfo.getBaseUriBuilder().path(BankAuthorResource.class).path(BankAuthorResource.class, "findById").build(response.getAuthorId());

        return Response.created(paymentUri).entity(response).build();
    }

}
