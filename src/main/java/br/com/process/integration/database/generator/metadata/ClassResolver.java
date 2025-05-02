package br.com.process.integration.database.generator.metadata;

import com.squareup.javapoet.ClassName;

public class ClassResolver {

	private String name;
	private ClassName typeId;

	public ClassResolver(String name, ClassName typeId) {
		this.name = name;
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClassName getTypeId() {
		return typeId;
	}

	public void setTypeId(ClassName typeId) {
		this.typeId = typeId;
	}

}
