package se.jelmstrom.musicfinder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class Server {

    static{
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "info");
        System.setProperty("org.apache.commons.logging.simplelog.log.se.jelmstrom.musicfinder.http", "debug");
    }
    private final static int PORT = 8081;

    private final static String CONTEXT_PATH = "/";
    private final static String API_PACKAGE = "se.jelmstrom.musicfinder";
    private final static String API_PATH = "/*";
    private final static Log log = LogFactory.getLog(Server.class);
    private static final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(final String[] args) throws Exception {
        log.info("Booting");
        QueuedThreadPool threadPool = new QueuedThreadPool(100, 10);
        final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(threadPool);
        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory());
        httpConnector.setPort(PORT);
        server.addConnector(httpConnector);
        final ServletContextHandler servletContextHandler = new ServletContextHandler(server, CONTEXT_PATH);
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.setMapper(mapper);
        final ResourceConfig config = new ResourceConfig()
                .register(jsonProvider)
                .packages(API_PACKAGE);

        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));
        servletContextHandler.addServlet(servletHolder, API_PATH);

        try {
            server.start();
            server.join();
        } finally {
            server.stop();
            server.destroy();
        }
    }

}
