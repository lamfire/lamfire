package com.test;

import com.lamfire.utils.XMLParser;

public class XMLParserTest {
	public static void main(String[] args) throws Exception{
		XMLParser parser = XMLParser.load("test.xml",XMLParserTest.class);
		System.out.println(parser.getNodeValue("/chimaera/server/bind"));
        System.out.println(parser.getNodeAttribute("/chimaera/server", "bind"));
	}
}
