/**
 * 
 */
package com.csv.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author paul zhu
 * @date 2016年7月14日 下午2:17:15
 */
@Entity
@Table(name="REXautoTC_mobile")
public class autoTCEntity {
	@Id
	@GeneratedValue
	private Long id;
	private String tcName;
	private boolean selected;
	private String accountMailAddr;
	private String accountPSW;


	public autoTCEntity(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTcName() {
		return tcName;
	}

	public void setTcName(String tcName) {
		this.tcName = tcName;
	}

	public boolean getSelectStatus() {
		return selected;
	}

	public void setSelectStatus(boolean selected) {
		this.selected = selected;
	}


	public String getMail() {
		return accountMailAddr;
	}

	public void setMail(String mail) {
		this.accountMailAddr = mail;
	}

	public String getPSW() {
		return accountPSW;
	}

	public void setPSW(String psw) {
		this.accountPSW = psw;
	}
}
