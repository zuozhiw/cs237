package edu.ics.uci;

import edu.ics.uci.resources.HelloWorldResource;
import edu.ics.uci.resources.TokenResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jdk.nashorn.internal.parser.Token;
import okhttp3.OkHttpClient;

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
    }

    @Override
    public void run(final cs237Configuration configuration,
                    final Environment environment) {
        environment.jersey().setUrlPattern("/api/*");

        final HelloWorldResource resource = new HelloWorldResource();
        environment.jersey().register(resource);
        // TODO: implement application

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final TokenResource resource2 = new TokenResource(okHttpClient);
        environment.jersey().register(resource2);
    }

}
