package com.github.pocketkid2.radio.utils;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RadioTest {

	private static Pattern tier;
	private static Pattern frequency;
	private static Pattern state;

	static {
		tier = Pattern.compile("Tier: (\\d)");
		frequency = Pattern.compile("Frequency: (\\d{3})");
		state = Pattern.compile("State: (OFF|ON)");
	}

	@Test
	public void test() {
		String title = "Tier: 1";
		String title2 = "Frequency: 135";
		String title3 = "State: ON";

		assertTrue(tier.matcher(title).matches());
		assertTrue(frequency.matcher(title2).matches());
		assertTrue(state.matcher(title3).matches());

		Matcher m = tier.matcher(title);

		System.out.println(m.find());
		System.out.println(m.group(1));
	}

}
