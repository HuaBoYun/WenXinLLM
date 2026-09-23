package com.huabo.system.entity.yy;

public class Plaintiffs {

	  private String type; //1-公司 2-人
      private String id; //人或公司id
      private String name;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
