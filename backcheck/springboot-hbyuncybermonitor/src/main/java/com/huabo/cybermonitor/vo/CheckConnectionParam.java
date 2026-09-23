package com.huabo.cybermonitor.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckConnectionParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "dataBaseType 数据库类型Oracle 不为空")
	@Schema(name = "数据库类型Oracle Mysql SqlServer")
	private String dataBaseType;

	@NotBlank(message = "dataBaseConnectionAddress 数据库连接地址 不为空")
	@Schema(name = "数据库连接地址")
	private String dataBaseConnectionAddress;

	@NotBlank(message = "dataBaseUsers 数据库用户 不为空")
	@Schema(name = "数据库用户")
	private String dataBaseUsers;

	@NotBlank(message = "dataBasePassWord 数据库密码 不为空")
	@Schema(name = "数据库密码")
	private String dataBasePassWord;

	public String getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public String getDataBaseConnectionAddress() {
		return dataBaseConnectionAddress;
	}

	public void setDataBaseConnectionAddress(String dataBaseConnectionAddress) {
		this.dataBaseConnectionAddress = dataBaseConnectionAddress;
	}

	public String getDataBaseUsers() {
		return dataBaseUsers;
	}

	public void setDataBaseUsers(String dataBaseUsers) {
		this.dataBaseUsers = dataBaseUsers;
	}

	public String getDataBasePassWord() {
		return dataBasePassWord;
	}

	public void setDataBasePassWord(String dataBasePassWord) {
		this.dataBasePassWord = dataBasePassWord;
	}
}
