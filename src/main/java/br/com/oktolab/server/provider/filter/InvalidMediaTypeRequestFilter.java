package br.com.oktolab.server.provider.filter;

//import java.io.IOException;
//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.ext.Provider;

//@Provider
public class InvalidMediaTypeRequestFilter{}/* implements ContainerRequestFilter {

	final String APPLICATION_JSON_WITH_UTF8_CHARSET = MediaType.APPLICATION_JSON + ";charset=" + java.nio.charset.StandardCharsets.UTF_8;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
//		String contentType = requestContext.getHeaderString("Content-Type");
//		if (contentType == null || contentType.startsWith("text/plain") || contentType.startsWith("application/x-www-form-urlencoded")) {
			requestContext.getHeaders().putSingle("Content-Type", APPLICATION_JSON_WITH_UTF8_CHARSET);
//		}
	}
	
}
*/