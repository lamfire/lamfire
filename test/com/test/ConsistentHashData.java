package com.test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lamfire.code.ConsistentHash.HashNode;
import com.lamfire.code.ConsistentHash.Node;

public class ConsistentHashData {

	static Random ran = new Random();
	/**
	 * Gets the mock node by the material parameter
	 * 
	 * @param nodeCount 
	 * 		the count of node wanted
	 * @return
	 * 		the node list
	 */
	public static List<Node> getNodes(int nodeCount) {
		List<Node> nodes = new ArrayList<Node>();
		
		for (int k = 1; k <= nodeCount; k++) {
			Node node = new HashNode(  "node" + k);
			nodes.add(node);
		}
		
		return nodes;
	}
	
	/**
	 *	All the keys	
	 */
	public static List<String> getAllStrings(int exeTimes) {
		List<String> allStrings = new ArrayList<String>(exeTimes);
		for (int i = 0; i < exeTimes; i++) {
			allStrings.add(generateRandomString(ran.nextInt(100)));
		}
		return allStrings;
	}
	

	public static String generateRandomString(int length) {
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			sb.append((char) (ran.nextInt(95) + 32));
		}
		
		return sb.toString();
	}
}
