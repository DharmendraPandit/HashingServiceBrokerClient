package com.hashing.client.model;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author DharmendraPandit
 *
 */
public class HashingServiceInfo extends BaseServiceInfo{

	private final String uri;
    private final String username;
    private final String password;
    
    public HashingServiceInfo(String id, String uri, String username, String password) {
        super(id);
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    @ServiceProperty
    public String getUri() {
        return uri;
    }

    @ServiceProperty
    public String getUsername() {
        return username;
    }

    @ServiceProperty
    public String getPassword() {
        return password;
    }
}
