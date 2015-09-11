package com.github.pocketkid2.radio.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RadioTest {

	@Test
	public void test() {
		assertTrue(Radio.getTierString(2).matches("Tier: (\\d)"));
		// assertTrue(Radio.getRadiusString(2).matches("Radius: (\\d+)"));
		System.out.println(Radio.getFrequencyString(1));
		assertTrue(Radio.getFrequencyString(1).matches("Frequency: (\\d{3})"));
		assertTrue(Radio.getStateString(false).matches("State: (OFF|ON)"));
	}

}
