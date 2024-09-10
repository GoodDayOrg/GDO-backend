package org.example.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.example.TestApplication;
import org.example.TestConfiguration;
import org.example.models.JobRoleApplication;
import org.example.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.example.utils.JwtUtils.generateToken;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class JobRoleControllerIT {


    static final DropwizardAppExtension<TestConfiguration> APP = new DropwizardAppExtension<>(
            TestApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );

    @Test
    public void authorization_givenAdminRole_whenJobRolesGET_shouldReturnStatus200(){
        String adminToken = generateToken("email@example.com", 1);
        JwtUtils.validateToken(adminToken);
        Integer result = APP.client().target("http://localhost:8080/api/job-roles").request()
                .header("Authorization" , ("Bearer " + adminToken)).get().getStatus();
        assertEquals(200, result);
    }

    @Test
    public void authorization_givenUserRole_whenJobRolesGET_shouldReturnStatus200(){
        String userToken = generateToken("email@example.com", 2);
        JwtUtils.validateToken(userToken);
        Integer result = APP.client().target("http://localhost:8080/api/job-roles").request()
                .header("Authorization" , ("Bearer " + userToken)).get().getStatus();
        assertEquals(200, result);
    }

    @Test
    public void authorization_givenNonAuthorizedUser_whenJobRolesGET_shouldReturnStatus401(){
        Integer result = APP.client().target("http://localhost:8080/api/job-roles").request().get().getStatus();
        assertEquals(401, result);
    }

    @Test
    public void getUserAllJobApplications_givenAdminRole_shouldReturn200() {
        String adminToken = generateToken("admin", 1);
        JwtUtils.validateToken(adminToken);
        Integer result = APP.client().target("http://localhost:8080/api/job-roles/my-job-applications")
                .request()
                .header("Authorization" , ("Bearer " + adminToken))
                .get()
                .getStatus();
        assertEquals(200, result);
    }

    @Test
    public void getUserAllJobApplications_notGivenAdminRole_shouldReturn401() {
        Integer result = APP.client().target("http://localhost:8080/api/job-roles/my-job-applications")
                .request()
                .get()
                .getStatus();
        assertEquals(401, result);
    }
}