package com.test.json;

import java.util.ArrayList;
import java.util.List;

import com.lamfire.json.JSON;

public class ByteArraySerializerTest {
	static class ThisItem{
		String name;
		int status;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

	static class ByteArrayItem extends ThisItem{
		
		byte[] bytes;
		List<byte[]> list;
		List<String> strings;

		public List<byte[]> getList() {
			return list;
		}

		public void setList(List<byte[]> list) {
			this.list = list;
		}

		public List<String> getStrings() {
			return strings;
		}

		public void setStrings(List<String> strings) {
			this.strings = strings;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}
		
		
	}
	
	
	public static void main(String[] args) {
		ByteArrayItem item = new ByteArrayItem();
		item.setName("DEFAULT");
		item.setStatus(200);
		List<byte[]> list = new ArrayList<byte[]>();
		List<String> strings =  new ArrayList<String>();
		for(int i=0;i<10;i++){
			list.add(String.valueOf(i).getBytes());
			strings.add(String.valueOf(i));
		}
		item.setList(list);
		item.setBytes("bytes".getBytes());
		item.setStrings(strings);
		
		String js = JSON.toJSONString(item);
		System.out.println(js);
		
		ByteArrayItem decode = JSON.toJavaObject(js, ByteArrayItem.class);
		System.out.println(JSON.toJSONString(decode));
	}
}
