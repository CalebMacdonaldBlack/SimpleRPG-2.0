package com.gigabytedx.rpg2.constants;

public enum ConfirmationMessages {
	SUCCESSFUL_SETLEVEL(NamesAndPrefixes.PLUGIN_NAME.getName() + " Level successfully added to this region!");
	
	private final String name;

	private ConfirmationMessages(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
