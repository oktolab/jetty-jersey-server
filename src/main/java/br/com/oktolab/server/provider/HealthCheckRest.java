package br.com.oktolab.server.provider;

import java.util.function.Function;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import br.com.oktolab.server.ApplicationServer;

@Path("/healthcheck")
public class HealthCheckRest {

	@GET
	public String healthCheck() {
		Function<?, Boolean> checkFunction = ApplicationServer.getAppHealthCheckFunction();
		if (checkFunction != null && checkFunction.apply(null) == Boolean.FALSE) {
			throw new IllegalStateException("Application health check failed.");
		}
		return "OK";
	}
}
