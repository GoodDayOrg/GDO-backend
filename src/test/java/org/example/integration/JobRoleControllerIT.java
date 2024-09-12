package org.example.integration;

import static org.example.utils.JwtUtils.generateToken;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.example.TestApplication;
import org.example.TestConfiguration;
import org.example.utils.JwtUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class JobRoleControllerIT {

    static final DropwizardAppExtension<TestConfiguration> APP =
            new DropwizardAppExtension<>(TestApplication.class, null, new ResourceConfigurationSourceProvider());

    @Test
    public void authorization_givenAdminRole_whenJobRolesGET_shouldReturnStatus200() {
        String adminToken = generateToken("email@example.com", 1);
        JwtUtils.validateToken(adminToken);

        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles")
                .request()
                .header("Authorization", ("Bearer " + adminToken))
                .get()
                .getStatus();
        assertEquals(200, result);
    }

    @Test
    public void authorization_givenUserRole_whenJobRolesGET_shouldReturnStatus200() {
        String userToken = generateToken("email@example.com", 2);
        JwtUtils.validateToken(userToken);

        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles")
                .request()
                .header("Authorization", ("Bearer " + userToken))
                .get()
                .getStatus();
        assertEquals(200, result);
    }

    @Test
    public void authorization_givenNotAuthenticatedUser_whenJobRolesGET_shouldReturnStatus401() {
        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles")
                .request()
                .get()
                .getStatus();
        assertEquals(401, result);
    }

    @Test
    public void applyForRole_givenNonAuthenticatedUser_shouldReturnStatus401() {

        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles/1/applications")
                .request()
                .post(Entity.entity(null, MediaType.MULTIPART_FORM_DATA))
                .getStatus();
        assertEquals(401, result);
    }

    @Test
    @Disabled
    public void applyForRole_givenAuthenticatedUser_shouldReturnStatus200() {
        String userToken = generateToken("email@example.com", 2);
        JwtUtils.validateToken(userToken);

        byte[] fileContent = "This is a mock PDF content".getBytes();
        InputStream mockFileInputStream = new ByteArrayInputStream(fileContent);
        FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.field("file", mockFileInputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        Integer result = APP.client()
                .register(MultiPartFeature.class) // Register the MultiPartFeature
                .target("http://localhost:8080/api/job-roles/1/applications")
                .request()
                .header("Authorization", "Bearer " + userToken)
                .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE))
                .getStatus();

        assertEquals(200, result);
    }

    @Test
    public void getUserAllJobApplications_givenAdminRole_shouldReturn200() {
        String adminToken = generateToken("admin@example.com", 1);
        JwtUtils.validateToken(adminToken);
        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles/my-job-applications")
                .request()
                .header("Authorization", ("Bearer " + adminToken))
                .get()
                .getStatus();
        assertEquals(200, result);
    }

    @Test
    public void getUserAllJobApplications_notGivenAdminRole_shouldReturn401() {
        Integer result = APP.client()
                .target("http://localhost:8080/api/job-roles/my-job-applications")
                .request()
                .get()
                .getStatus();
        assertEquals(401, result);
    }
}
