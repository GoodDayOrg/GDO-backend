package org.example.controllers;

import io.dropwizard.auth.Auth;
import static org.example.utils.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.example.utils.HttpStatus.NOT_FOUND;
import static org.example.utils.HttpStatus.OK;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.example.exceptions.InvalidFileTypeException;
import org.example.exceptions.ResultSetException;
import org.example.models.JobRole;
import org.example.models.JobRoleApplication;
import org.example.models.JobRoleDetailsCSV;
import org.example.models.JobRoleResponse;
import org.example.models.JwtToken;
import org.example.models.UserRole;
import org.example.services.JobRoleService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.example.models.JobRoleFilteredRequest;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Api("Job Role API")
@Path("/api/job-roles")
public class JobRoleController {
    JobRoleService jobRoleService;
    private static final Logger LOGGER = LogManager.getLogger();

    public JobRoleController(final JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
        LOGGER.info("JobController initialized");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    @ApiOperation(
            value = "Returns a list of Job Roles",
            authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION),
            response = JobRoleResponse.class,
            responseContainer = "List",
            produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = OK, message = "Job roles listed successfully", response = JobRole.class),
        @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "getting all job roles failed due to SQL Exception"),
        @ApiResponse(code = NOT_FOUND, message = "getting all job roles failed due to DoesNotExistException")
    })
    public Response getAllJobRoles() {
        LOGGER.info("Get all job roles request received");
        try {
            return Response.ok().entity(jobRoleService.getAllJobRoles()).build();
        } catch (SQLException e) {
            LOGGER.error("getting all job roles failed due to SQL Exception\n" + e.getMessage());
            return Response.serverError().build();
        } catch (DoesNotExistException | NullPointerException e) {
            LOGGER.error("getting all job roles failed due to DoesNotExistException\n" + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (ResultSetException e) {
            LOGGER.error("getting all job roles failed due to ResultSetException\n" + e.getMessage());
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns a list of Job Roles",
            authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION),
            response = JobRoleResponse.class,
            responseContainer = "Filtered list",
            produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = OK, message = "Job roles listed successfully", response = JobRole.class),
        @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "getting filtered job roles failed due to SQL exception"),
        @ApiResponse(code = NOT_FOUND, message = "getting filtered job roles failed due to DoesNotExistException")
    })
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    @Path("/filter")
    public Response getFilteredJobRoles(final @BeanParam JobRoleFilteredRequest jobRoleFilteredRequest) {
        LOGGER.info("Get filtered job roles request received");
        try {
            return Response.ok()
                    .entity(jobRoleService.getFilteredJobRoles(jobRoleFilteredRequest))
                    .build();
        } catch (SQLException e) {
            LOGGER.error("getting filtered job roles failed due to SQL exception\n" + e.getMessage());
            return Response.serverError().build();
        } catch (DoesNotExistException | NullPointerException e) {
            LOGGER.error("getting filtered job roles failed due to DoesNotExistException\n" + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (ResultSetException e) {
            LOGGER.error("getting filtered job roles failed due to ResultSetException\n" + e.getMessage());
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{jobRoleId}")
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    @ApiOperation(
            value = "Returns Job Role details",
            authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION),
            response = JobRoleResponse.class,
            responseContainer = "Set",
            produces = "application/json")
    public Response getJobRoleById(@PathParam("jobRoleId") final int jobRoleId) {
        LOGGER.info("Get job role by ID request received");
        try {
            return Response.ok()
                    .entity(jobRoleService.getJobRoleById(jobRoleId))
                    .build();
        } catch (SQLException e) {
            LOGGER.error("getJobRoleById failed, SQL Exception \n{}", e.getMessage());
            return Response.serverError().build();
        } catch (DoesNotExistException | NullPointerException e) {
            LOGGER.error("getJobRoleById failed, DoesNotExistException\n{}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/my-job-applications")
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns a list of user's applications",
            authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION),
            response = JobRoleApplication.class,
            responseContainer = "List",
            produces = "application/json")
    @ApiResponses({
            @ApiResponse(
                    code = OK,
                    message = "User's job applications listed successfully",
                    response = JobRoleApplication.class),
            @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "getUserAllJobApplications failed, SQL Exception"),
            @ApiResponse(code = NOT_FOUND, message = "getUserAllJobApplications failed, DoesNotExistException")
    })
    public Response getUserAllJobApplications(@ApiParam(hidden = true) @Auth final JwtToken token) {
        LOGGER.info("Get all user job applications request received");
        String email = token.getUserEmail();
        try {
            return Response.ok().entity(jobRoleService.getAllUserApplications(email)).build();
        } catch (SQLException e) {
            LOGGER.error("Receiving job applications failed due to SQLException\n" + e.getMessage());
            return Response.serverError().build();
        } catch (DoesNotExistException e) {
            LOGGER.error("Receiving job applications failed due to DoesNotExistException\n" + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @Path("/import")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({UserRole.ADMIN})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation(
            value = "Allows to import CSV file",
            authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION),
            response = JobRoleDetailsCSV.class,
            produces = "application/json")
    @ApiResponses({
            @ApiResponse(
                    code = OK,
                    message = "Job roles import succeed",
                    response = JobRoleDetailsCSV.class),
            @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "uploading CSV file failed, SQL Exception"),
            @ApiResponse(code = NOT_FOUND, message = "uploading CSV file failed, DoesNotExistException")
    })
    public Response uploadJobRolesCsvFile(@FormDataParam("file") final InputStream fileInputStream,
                                          @FormDataParam("file") final FormDataContentDisposition fileDetails) {
        try {
            LOGGER.info("uploadJobRolesCsvFile request received");
            String fileName = fileDetails.getFileName();
            jobRoleService.getJobRolesFromCsv(fileInputStream, fileName);
            return Response.ok().build();
        } catch (IOException e) {
            LOGGER.error("Importing CSV File failed\n" + e.getMessage());
            return Response.serverError().build();
        } catch (FileNeededException e) {
            LOGGER.error("Importing CSV File failed, no file found\n" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (FileTooBigException e) {
            LOGGER.error("Importing CSV File failed, File is too big\n" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (InvalidFileTypeException e) {
            LOGGER.error("Importing CSV File failed, wrong file type\n" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }
}
