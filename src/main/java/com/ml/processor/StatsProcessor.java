package com.ml.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ml.detector.Detector;
import com.ml.detector.MutantDetector;
import com.ml.model.MutantRequest;
import com.ml.model.StatsResponse;

@Component
public class StatsProcessor {

	private String url = "http://localhost:4444/dna/stats";
	private String urlInsertM = "http://localhost:4444/dna/mutant";
	private String urlInsertH = "http://localhost:4444/dna/human";
	private static final Logger LOGGER = LoggerFactory.getLogger(StatsProcessor.class);

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public void obtenerStats(Exchange exchange) {
		try {

			Map<String, Object> headersCamel = exchange.getIn().getHeaders();
			Object bodyCamel = exchange.getIn().getBody();
			MutantRequest mutantRequest= new MutantRequest();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType( MediaType.APPLICATION_JSON );

			HttpEntity request= new HttpEntity( mutantRequest, headers );
			ResponseEntity<StatsResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, StatsResponse.class);

			StatsResponse enrollmentResponse= responseEntity.getBody();

			exchange.getOut().setHeaders(headersCamel);
			exchange.getOut().setBody(enrollmentResponse);
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void agregarDna(Exchange exchange) {
		try {
			ResponseEntity<String> responseEntity;
			
			Map<String, Object> headersCamel = exchange.getIn().getHeaders();
			MutantRequest dnaBody= new MutantRequest();
			
			buildRequest(dnaBody,exchange);
			
			Detector mutant = new MutantDetector();

			HttpHeaders headers = new HttpHeaders();
            headers.setContentType( MediaType.APPLICATION_JSON );
            
			MutantRequest bodyIn = (MutantRequest) exchange.getIn().getBody();

			HttpEntity request= new HttpEntity( dnaBody, headers );
			if(mutant.isMutant(dnaBody.getDna())) {
				responseEntity = restTemplate.postForEntity(urlInsertH,request, String.class);	
			} else {
				responseEntity = restTemplate.postForEntity(urlInsertM,request, String.class);
			}
			
			HttpStatus enrollmentResponse= responseEntity.getStatusCode();
			
			exchange.getOut().setHeaders(headersCamel);
			exchange.getOut().setBody(enrollmentResponse);
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	private void buildRequest(MutantRequest reqDna, Exchange exchange) {

		MutantRequest dna =(MutantRequest)exchange.getIn().getBody();
		reqDna.setDna(dna.getDna());

	}
	
}