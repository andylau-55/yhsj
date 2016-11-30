package com.yhsjedu.datacloud.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users")
@DynamicUpdate(true)
public class Users implements Serializable {

	/**
	 * @author yangjin
	 */
	private static final long serialVersionUID = -7226837948865967797L;
	private Long userId;
	private String userName;
	private String password;
	private String createData;
	private String state;
	private String userDspName;

	public Users() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateData() {
		return createData;
	}

	public void setCreateData(String createData) {
		this.createData = createData;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserDspName() {
		return userDspName;
	}

	public void setUserDspName(String userDspName) {
		this.userDspName = userDspName;
	}

}
