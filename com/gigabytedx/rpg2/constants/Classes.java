package com.gigabytedx.rpg2.constants;

public enum Classes {
	DPS("Archer"),
	TANK("Knight"),
	HEALER("Cleric"),
	BARD("Bard");
	
	private final String className;

	private Classes(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}
	
	public static String[] names() {
	    Classes[] states = values();
	    String[] names = new String[states.length];

	    for (int i = 0; i < states.length; i++) {
	        names[i] = states[i].name();
	    }

	    return names;
	}
	
}
