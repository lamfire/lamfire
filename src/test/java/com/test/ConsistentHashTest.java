package com.test;

import com.lamfire.code.ConsistentHash;
import com.lamfire.code.ConsistentHash.Node;
import com.lamfire.utils.Lists;

import java.util.List;

public class ConsistentHashTest {


	public static void showPrimaryAndSecondary(ConsistentHash hash, String key) {
		Node primary = hash.getPrimary(key);
		Node secondary = hash.getSecondary(key);
		System.out.println("primary[" + key + "] : " + primary);
		System.out.println("secondary[" + key + "] : " + secondary);
	}
	
	public static void main(String[] args) {
		//boundary();
		List<ConsistentHash.HashNode> nodes = Lists.newArrayList();
		for (int i = 0; i < 16; i++) {
			ConsistentHash.HashNode n = new ConsistentHash.HashNode("Node-" + i);
			nodes.add(n);

		}

		ConsistentHash<ConsistentHash.HashNode> hash = new ConsistentHash<ConsistentHash.HashNode>(nodes);

		showPrimaryAndSecondary(hash, "10001");
		showPrimaryAndSecondary(hash, "10002");


		System.out.println("\n\n\n");

		showPrimaryAndSecondary(hash, "10003");
		showPrimaryAndSecondary(hash, "10004");
	}
}
