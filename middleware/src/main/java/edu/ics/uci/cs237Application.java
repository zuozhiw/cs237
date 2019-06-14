package edu.ics.uci;

import com.github.dirkraft.dropwizard.fileassets.FileAssetsBundle;
import edu.ics.uci.core.BuildingLocations;
import edu.ics.uci.resources.HelloWorldResource;
import edu.ics.uci.resources.ReserveWebSocketServer.ReserveWebSocketServerConfigurator;
import edu.ics.uci.resources.TutorResource;
import edu.ics.uci.resources.ReserveWebSocketServer;
import edu.ics.uci.resources.WebSocketTest;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import okhttp3.OkHttpClient;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.websocket.server.ServerEndpointConfig;
import java.util.EnumSet;

public class cs237Application extends Application<cs237Configuration> {

    private WebsocketBundle websocketBundle;

    public static void main(final String[] args) throws Exception {
        new cs237Application().run(args);
    }

    @Override
    public String getName() {
        return "cs237";
    }

    @Override
    public void initialize(final Bootstrap<cs237Configuration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(new FileAssetsBundle("../gui/", "/", "index.html"));
        websocketBundle = new WebsocketBundle(WebSocketTest.class);
        bootstrap.addBundle(websocketBundle);
    }

    @Override
    public void run(final cs237Configuration configuration,
                    final Environment environment) {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        environment.jersey().setUrlPattern("/api");

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final HelloWorldResource helloWorldResource = new HelloWorldResource();
        environment.jersey().register(helloWorldResource);

        final TutorResource tutorResource = new TutorResource(jdbi, okHttpClient);
        environment.jersey().register(tutorResource);

        websocketBundle.addEndpoint(ServerEndpointConfig.Builder
                .create(ReserveWebSocketServer.class, "/api/reserve-ws")
                .configurator(new ReserveWebSocketServerConfigurator(jdbi, okHttpClient, configuration))
                .build()
        );

        BuildingLocations.init();
        // TODO: implement application
    }

}
