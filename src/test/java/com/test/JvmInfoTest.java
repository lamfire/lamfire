package com.test;

import com.lamfire.json.JSON;
import com.lamfire.utils.JvmInfo;
import com.lamfire.utils.StringUtils;


public class JvmInfoTest {


	public static void main(String[] args) throws Exception {

		JvmInfo info = JvmInfo.getInstance();
		JSON json = new JSON();
		json.put("status",200);
		json.put("Hostname",info.getHostname());
		json.put("VmName",info.getVmName());
		json.put("VmVersion",info.getVmVersion());
		json.put("AvailableProcessors",info.getAvailableProcessors());
		json.put("Threads",info.getAllThreadIds().length);
		json.put("HeapUsedMemory",info.getMem().getHeapUsed());
		json.put("HeapMaxMemory",info.getMem().getHeapMax());
		String result = json.toJSONString();
		System.out.println(result);
	}
}
