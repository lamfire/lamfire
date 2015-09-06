package com.test.json;

import com.lamfire.json.JSON;

import java.util.*;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (args != null ? !args.equals(item.args) : item.args != null) return false;
        if (!Arrays.equals(bytes, item.bytes)) return false;
        if (key != null ? !key.equals(item.key) : item.key != null) return false;
        if (keys != null ? !keys.equals(item.keys) : item.keys != null) return false;
        if (url != null ? !url.equals(item.url) : item.url != null) return false;
        if (users != null ? !users.equals(item.users) : item.users != null) return false;
        if (values != null ? !values.equals(item.values) : item.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        result = 31 * result + (bytes != null ? Arrays.hashCode(bytes) : 0);
        result = 31 * result + (args != null ? args.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
