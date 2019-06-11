package edu.ics.uci;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dirkraft.dropwizard.fileassets.FileAssetsBundle;
import edu.ics.uci.core.TutorBean;
import edu.ics.uci.resources.HelloWorldResource;
import edu.ics.uci.resources.TutorResource;
import edu.ics.uci.resources.WebSocketTest;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import okhttp3.OkHttpClient;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

public class cs237Application extends Application<cs237Configuration> {

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
        bootstrap.addBundle(new FileAssetsBundle("./gui/", "/", "index.html"));
        bootstrap.addBundle(new WebsocketBundle(WebSocketTest.class));
    }

    @Override
    public void run(final cs237Configuration configuration,
                    final Environment environment) {
        // serve backend at /api
        environment.jersey().setUrlPattern("/api/*");

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final HelloWorldResource helloWorldResource = new HelloWorldResource();
        environment.jersey().register(helloWorldResource);

        final TutorResource tutorResource = new TutorResource(jdbi, okHttpClient);
        environment.jersey().register(tutorResource);

        // TODO: implement application
    }

}
