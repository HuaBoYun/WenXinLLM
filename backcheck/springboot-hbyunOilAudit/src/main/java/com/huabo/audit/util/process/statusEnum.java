package com.huabo.audit.util.process;

public enum statusEnum {

	unstart(1),start(2),success(3),fail(4),finished(34);
	
	private Integer value;
	
	private statusEnum(Integer value) {
        this.value  = value;
	}



	public Integer getValue() {
		return value;
	}

}
