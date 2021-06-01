package com.jeptstore.framework;

public enum BROWSERS {
	explorer, chrome, firefox, headless, edge;

	public static BROWSERS fromString(String text) {
		for (BROWSERS b : BROWSERS.values()) {
			if (b.name().contains(text.toLowerCase().trim())) {
				return b;
			}
		}
		return null;
	}
}