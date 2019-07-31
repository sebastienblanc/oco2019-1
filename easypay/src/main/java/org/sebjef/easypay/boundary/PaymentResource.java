/*
 * Copyright 2019 JF James.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sebjef.easypay.boundary;

import java.net.URI;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.sebjef.easypay.control.PaymentProcessingContext;
import org.sebjef.easypay.control.PaymentService;
import org.sebjef.easypay.entity.Payment;

/**
 * A Rest resource for accessing payment operations
 *
 * @author JF James
 */
@Path("payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped // Required with Payara to enable CDI injection
public class PaymentResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private PaymentService paymentService;

    @GET
    @Path("/ping")
    @Operation(operationId = "Test the service accesibility")
    public String ping() {
        return "Enjoy SmartPay with Java EE 8 and MicroProfile 2.1 at Oracle Code One 2019!";
    }

    @GET
    @Operation(operationId = "Find all payments")
    public List<Payment> findAll() {
        return paymentService.findAll();
    }

    @GET
    @Path("/count")
    @Operation(operationId = "Count all payments")
    public long count() {
        return paymentService.count();
    }

    @GET
    @Path("{id}")
    @Operation(operationId = "Retrieve a given payment by its id")
    @APIResponses(
            value = {
                @APIResponse(responseCode = "200", description = "Payment found", content = @Content(mediaType = "application/json")),
                @APIResponse(responseCode = "204", description = "Payment not found", content = @Content(mediaType = "text/plain"))
            }
    )
    public Payment findById(@Parameter(description = "The payment id to be retrieved", required = true)
            @PathParam("id") long paymentId) {
        return paymentService.findById(paymentId);
    }

    @POST
    @APIResponses(
            value = {
                @APIResponse(responseCode = "201", description = "Payment accepted", content = @Content(mediaType = "application/json"))
            }
    )
    @Operation(operationId = "Accept (or deny) a payment")
    public Response addPayment(@Valid @NotNull PaymentRequest paymentRequest) {

        PaymentProcessingContext paymentContext = new PaymentProcessingContext(paymentRequest);
        
        paymentService.accept(paymentContext);
        
        PaymentResponse response = paymentContext.generateResponse();

        URI paymentUri = uriInfo.getBaseUriBuilder().path(PaymentResource.class).path(PaymentResource.class, "findById").build(response.getPaymentId());

        return Response.created(paymentUri).entity(response).build();
    }

}
