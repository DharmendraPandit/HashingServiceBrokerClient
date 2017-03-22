package com.hashing.client.service;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import com.hashing.client.model.HashingServiceInfo;

/**
 * 
 * @author DharmendraPandit
 *
 */
public class HashingServiceClientInfo extends CloudFoundryServiceInfoCreator<HashingServiceInfo>{

	public HashingServiceClientInfo() {
        super(new Tags("hashing"));//in broker service
    }
	
	@Override
    public HashingServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");

        String id = (String) serviceData.get("name");
        String uri = (String) credentials.get("uri");
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");

        return new HashingServiceInfo(id, uri, username, password);
    }
	
	@Override
    public boolean accept(Map<String, Object> serviceData) {
        Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
        String uri = (String) credentials.get("uri");
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");
        return username != null &&
                password != null &&
                uri != null;
    }
}
