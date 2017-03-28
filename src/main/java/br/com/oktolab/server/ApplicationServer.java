package br.com.oktolab.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.netflix.config.ConfigurationManager;

public class ApplicationServer {
	
	private Server server;
	private ServletContextHandler context;

	public void start() throws Exception {
		configure();
		server.start();
		server.join();
	}

	private void configure() {
		int port = ConfigurationManager.getConfigInstance().getInt("port", 7020);
		server = new Server(port);
//		server.setHandler(servletContext);
		ResourceConfig config = buildResourceConfig();
        ServletHolder jerseyServlet = buildServletHolder(config);
        context = buildServletContext(server, jerseyServlet);
	}

	private ServletContextHandler buildServletContext(Server server, ServletHolder jerseyServlet) {
		String contextPath = ConfigurationManager.getConfigInstance().getString("server.context.path", "/");
        ServletContextHandler context = new ServletContextHandler(server, contextPath);
        context.addServlet(jerseyServlet, "/*");
		return context;
	}

	private ServletHolder buildServletHolder(ResourceConfig config) {
		ServletHolder jerseyServlet  = new ServletHolder(new ServletContainer(config));
		return jerseyServlet;
	}

	private ResourceConfig buildResourceConfig() {
		String packages = ConfigurationManager.getConfigInstance().getString("jersey.config.server.provider.packages");
		ResourceConfig config = new ResourceConfig();
		config.packages(packages);
		return config;
	}

	public Server getServer() {
		return server;
	}

	public ServletContextHandler getContext() {
		return context;
	}
}
