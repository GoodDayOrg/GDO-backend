package org.example.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import java.sql.SQLException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.InvalidException;
import org.example.models.LoginRequest;
import org.example.services.AuthService;

@Api("Login API")
@Path("/api/auth")
@SwaggerDefinition(
        securityDefinition =
                @SecurityDefinition(
                        apiKeyAuthDefinitions = {
                            @ApiKeyAuthDefinition(
                                    key = HttpHeaders.AUTHORIZATION,
                                    name = HttpHeaders.AUTHORIZATION,
                                    in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER)
                        }))
public class AuthController {

    private final AuthService authService;

    private static final Logger LOGGER = LogManager.getLogger();

    public AuthController(final AuthService authService) {
        this.authService = authService;

        LOGGER.info("Authcontroller has been initialized");
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(final LoginRequest loginRequest) {
        LOGGER.info("Login request received");
        try {
            return Response.ok().entity(authService.login(loginRequest)).build();
        } catch (SQLException e) {
            return Response.serverError().build();
        } catch (InvalidException e) {
            LOGGER.error("Login request failed ", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
