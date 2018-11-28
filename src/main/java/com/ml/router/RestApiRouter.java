package com.ml.router;

import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.ml.model.MutantRequest;
import com.ml.processor.StatsProcessor;

@RestController
public class RestApiRouter {

	@Value("${ml.api.path}")
	String contextPath;

	@Component
	class RestApi extends RouteBuilder {

		@Override
		public void configure() {

			CamelContext context = new DefaultCamelContext();

			restConfiguration().contextPath(contextPath)
//			.port("8080")
			.enableCORS(true)
			.apiContextPath("/api-doc")
			.apiProperty("api.title", "REST API")
			.apiProperty("api.version", "v1")
			.apiProperty("cors", "true") 
			.apiContextRouteId("doc-api")
			.component("servlet")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true");

			rest("/api/")
			.post("/mutant")
			.consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
			.type(MutantRequest.class).outType(String.class)
			.to("direct:mutantRoute");

			rest("/api/")
			.get("/stats")
			.route().routeId("remotestats")
			.to("direct:stats");

			from("direct:stats")
			.routeId("direct-stats")
			.tracing()
			.bean(StatsProcessor.class, "obtenerStats")
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

			from("direct:mutantRoute")
			.routeId("direct-mutantRoute")
			.tracing()
			.bean(StatsProcessor.class,"agregarDna")
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
		}
	}
}
