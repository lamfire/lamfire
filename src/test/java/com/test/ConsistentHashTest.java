package com.test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lamfire.code.ConsistentHash;
import com.lamfire.code.ConsistentHash.Node;
import com.lamfire.utils.Printers;

public class ConsistentHashTest {

	private static final Integer EXE_TIMES = 100000;
	private static final Integer NODE_COUNT = 2;
	private static final Integer VRITURE_NODE_COUNT = 1600;
	
	public static void boundary() {
		Map<Node, Integer> nodeRecord = new HashMap<Node, Integer>();
		List<Node> allNodes = ConsistentHashData.getNodes(NODE_COUNT);
		List<String> allKeys = ConsistentHashData.getAllStrings(EXE_TIMES);
		
		ConsistentHash<Node> cHash = new ConsistentHash<Node>(allNodes,VRITURE_NODE_COUNT);
		for (String key : allKeys) {
			Node node = cHash.getPrimary(key);
			
			Integer times = nodeRecord.get(node);
			if (times == null) {
				nodeRecord.put(node, 1);
			} else {
				nodeRecord.put(node, times + 1);
			}
		}
		
		System.out.println("Nodes count : " + NODE_COUNT + ", Keys count : " + EXE_TIMES + ", Normal percent : " + (float) 100 / NODE_COUNT + "%");
		System.out.println("-------------------- boundary  ----------------------");
		for (Map.Entry<Node, Integer> entry : nodeRecord.entrySet()) {
			System.out.println("Node name :" + entry.getKey() + " - Times : " + entry.getValue() + " - Percent : " + (float)entry.getValue() / EXE_TIMES * 100 + "%");
		}
		
	}
	
	public static void printNodes() {
		List<Node> allNodes = ConsistentHashData.getNodes(NODE_COUNT);
		ConsistentHash<Node> cHash = new ConsistentHash<Node>(allNodes,VRITURE_NODE_COUNT);
		Printers.print(cHash.getVirtualNodes());
		Printers.print(cHash.getVirtualNodes().size());
	}
	
	public static void primaryAndSecondary() {
		List<Node> allNodes = ConsistentHashData.getNodes(NODE_COUNT);
		ConsistentHash<Node> cHash = new ConsistentHash<Node>(allNodes,VRITURE_NODE_COUNT);
		Printers.print(cHash.getPrimary("192.168.1.1"));
		Printers.print(cHash.getSecondary("192.168.1.1"));
	}
	
	public static void main(String[] args) {
		boundary();
	}
}
