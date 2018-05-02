package com.dhcc.ms.plugin.datatype.domain;

import java.io.Serializable;

public class Datatype implements Serializable {
	private static final long serialVersionUID = -8495456680196782814L;

	private String className;
	private String simpleClassName;
	private String description;

	Datatype(String className, String simpleClassName, String description) {
		this.className = className;
		this.simpleClassName = simpleClassName;
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public String getSimpleClassName() {
		return simpleClassName;
	}

	public String getDescription() {
		return description;
	}

}