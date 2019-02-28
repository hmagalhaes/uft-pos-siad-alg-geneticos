package coberturawifi.model;

import coberturawifi.Configs;

public enum RepresentationType {

	BITS("bits"), REAL("real");

	public final String code;

	RepresentationType(String code) {
		this.code = code;
	}

	public static RepresentationType fromCode(String code) {
		for (RepresentationType type : RepresentationType.values()) {
			if (type.code.equalsIgnoreCase(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Bad type => " + code);
	}

	public static RepresentationType fromConfigs(Configs instance) {
		final String code = Configs.getInstance().getString(Configs.REPRESENTATION_TYPE);
		return fromCode(code);
	}
	
}
