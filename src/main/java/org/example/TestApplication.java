package org.example;


import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.example.auth.JwtAuthenticator;
import org.example.auth.RoleAuthorizer;
import org.example.controllers.AuthController;
import org.example.daos.AuthDao;
import org.example.mappers.JobRoleMapper;
import org.example.models.JwtToken;
import org.example.services.AuthService;
import org.example.utils.JwtUtils;
import org.example.validators.AuthValidator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import javax.crypto.SecretKey;
import org.example.controllers.JobRoleController;
import org.example.daos.JobRoleDao;
import org.example.services.JobRoleService;

public class TestApplication extends Application<TestConfiguration> {
    public static void main(final String[] args) throws Exception {
        new TestApplication().run(args);
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public void initialize(final Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                    final TestConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final TestConfiguration configuration,
                    final Environment environment) {

        SecretKey jwtSecretKey = JwtUtils.getSecretKey();

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<JwtToken>()
                        .setAuthenticator(new JwtAuthenticator(jwtSecretKey))
                        .setAuthorizer(new RoleAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.
                Binder<>(JwtToken.class));

        environment.jersey()
                .register(new AuthController(
                        new AuthService(new AuthDao(), new AuthValidator())));

        environment.jersey()
                .register(new JobRoleController(new JobRoleService(new JobRoleDao(), new JobRoleMapper())));
        environment.jersey()
                .register(MultiPartFeature.class);
    }
}
