package com.test.json;

import java.util.Collection;
import java.util.List;


public class Item {
	private String key;
	private List<User> users;
	private List<byte[]> values;
	private Collection<String> keys;
	private byte[] bytes;
	private Args args;

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

	public List<byte[]> getValues() {
		return values;
	}

	public void setValues(List<byte[]> values) {
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

	public void setKeys(Collection<String> keys) {
		this.keys = keys;
	}

	
}
