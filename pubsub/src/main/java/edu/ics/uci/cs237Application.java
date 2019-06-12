package edu.ics.uci;

import edu.ics.uci.resources.HelloWorldResource;
import edu.ics.uci.resources.MiddlewareReceiverResource;
import edu.ics.uci.resources.PushServer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;

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
        bootstrap.addBundle(new WebsocketBundle(PushServer.class));
    }

    @Override
    public void run(final cs237Configuration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource();
        environment.jersey().register(resource);
        // TODO: implement application

        final MiddlewareReceiverResource middlewareReceiverResource = new MiddlewareReceiverResource();
        environment.jersey().register(middlewareReceiverResource);

    }

}
