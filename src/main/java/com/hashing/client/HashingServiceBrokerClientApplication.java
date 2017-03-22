package com.hashing.client;

import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hashing.client.model.HashingServiceInfo;
/**
 * 
 * @author DharmendraPandit
 *
 */
@RestController
@SpringBootApplication
public class HashingServiceBrokerClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HashingServiceBrokerClientApplication.class, args);
	}

	@Bean
	Cloud cloud() {
		return new CloudFactory().getCloud();
	}

	@Bean
	HashingServiceInfo hashingServiceInfo() {
		List<ServiceInfo> serviceInfos = cloud().getServiceInfos();
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo instanceof HashingServiceInfo) {
				return (HashingServiceInfo) serviceInfo;
			}
		}
		throw new RuntimeException("Unable to find bound hashing instance!");
	}

	@Bean
	RestTemplate restTemplate() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(hashingServiceInfo().getUsername(), hashingServiceInfo().getPassword()));
		httpClient.setCredentialsProvider(credentialsProvider);
		ClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(httpClient);

		return new RestTemplate(rf);
	}

	@RequestMapping("/hashing/info")
	public HashingServiceInfo info() {
		return hashingServiceInfo();
	}

	@RequestMapping(value = "/hashing/{key}", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@PathVariable("key") String key, @RequestBody String value) {
		restTemplate().put(hashingServiceInfo().getUri() + "/{key}", value, key);
		return new ResponseEntity<>("{}", HttpStatus.CREATED);
	}

	@RequestMapping(value = "/hashing/{key}", method = RequestMethod.GET)
	public ResponseEntity<String> get(@PathVariable("key") String key) {
		String response = restTemplate().getForObject(hashingServiceInfo().getUri() + "/{key}", String.class, key);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
