package com.test.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Item {
	private String key;
	private List<User> users;
	private Collection<byte[]> values;
	private Set<String> keys;
	private byte[] bytes;
	private Args args;
    private String url;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Collection<byte[]> getValues() {
		return values;
	}

	public void setValues(Collection<byte[]> values) {
		this.values = values;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public Args getArgs() {
		return args;
	}

	public void setArgs(Args args) {
		this.args = args;
	}

	public Collection<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
