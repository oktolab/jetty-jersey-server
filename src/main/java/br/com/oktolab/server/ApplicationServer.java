package br.com.oktolab.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import com.netflix.config.ConfigurationManager;

public class ApplicationServer {
	
	private static Function<Void, Boolean> healthCheckFunction;
	
	private Server server;
	private ServletContextHandler context;
	
	private List<Class<?>> features = new ArrayList<>();
	
	public ApplicationServer() {
		java.security.Security.setProperty("networkaddress.cache.ttl" , "10");
		java.security.Security.setProperty("networkaddress.cache.negative.ttl" , "10");
	}

	public void registerResourceConfigFeature(Class<?> clazz) {
		features.add(clazz);
	}
	
	private void configure() {
		int port = ConfigurationManager.getConfigInstance().getInt("port", 7020);
		server = new Server(port);
//		server.setHandler(servletContext);
		ResourceConfig config = buildResourceConfig();
		config.register(RolesAllowedDynamicFeature.class);
		config.register(JacksonFeature.class);
		for (Class<?> clazz : features) {
			config.register(clazz);
		}
        ServletHolder jerseyServlet = buildServletHolder(config);
        context = buildServletContext(server, jerseyServlet);
        server.setHandler(context);
	}
	
	/**
	 * Adiciona uma function a função de healthCheck da aplicação.
	 * A função 'healthCheckFunction' deve retornar 'true' caso o teste de saúde seja satisfeito.
	 * 
	 * @param function
	 */
	public ApplicationServer withAppHealthCheckFunction(Function<Void, Boolean> healthCheckFunction) {
		ApplicationServer.healthCheckFunction = healthCheckFunction;
		return this;
	}
	
	public void start() throws Exception {
		configure();
		server.start();
		server.join();
	}

	private ServletContextHandler buildServletContext(Server server, ServletHolder jerseyServlet) {
		String contextPath = ConfigurationManager.getConfigInstance().getString("server.context.path", "/");
        ServletContextHandler context = new ServletContextHandler(server, contextPath);
        context.addServlet(jerseyServlet, "/*");
        context.setInitParameter("com.sun.jersey.config.feature.DisableWADL", "true");
		return context;
	}

	private ServletHolder buildServletHolder(ResourceConfig config) {
		ServletContainer servletContainer = new ServletContainer(config);
		ServletHolder jerseyServlet  = new ServletHolder(servletContainer);
//        ServletHolder sh = new ServletHolder(ServletContainer.class);    
//        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
//        sh.setInitParameter("com.sun.jersey.config.property.packages", "rest");//Set the package where the services reside
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
	
	public static Function<?, Boolean> getAppHealthCheckFunction() {
		return healthCheckFunction;
	}
}
