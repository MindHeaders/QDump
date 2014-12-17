package org.dataart.qdump.entities.enums;

public enum QuestionnaireStatusEnums {
	PASSED("passed"),
	NOT_PASSED("not passed"),
	IN_PROGRESS("in progress"),
	IN_CHECKING_PROCESS("in checking process"),
	NOT_SPECIFIED("not specified");
	private final String name;
	private QuestionnaireStatusEnums(String name) {
		this.name = name;
	}
	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}
	public String getName() {
		return name;
	}
}
