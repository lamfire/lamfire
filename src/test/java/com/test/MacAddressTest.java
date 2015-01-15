package com.test;

import com.lamfire.utils.MACAddressUtils;
import com.lamfire.utils.Printers;

public class MacAddressTest {

	public static void main(String[] args) {
		Printers.print(MACAddressUtils.getHardwareAddresses());
		System.out.println(MACAddressUtils.getHardwareAddress());
		System.out.println(MACAddressUtils.getMacAddress());
		System.out.println(MACAddressUtils.getMacAddressAsLong());
		System.out.println(MACAddressUtils.asLongAddress(MACAddressUtils.getHardwareAddress()));
		System.out.println(MACAddressUtils.asStringAddress(MACAddressUtils.getMacAddressAsLong()));
	}
}
