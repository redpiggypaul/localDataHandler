/**
 * 
 */
package com.csv.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author william
 * @date 2016年5月28日 下午2:17:15
 */
@Entity
@Table(name="ebay_monthly_official_stat")
public class TestEntity {
	@Id
	@GeneratedValue
	private Long id;
	private String month;
	private Integer soldItem;
	
	public TestEntity(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getSoldItem() {
		return soldItem;
	}

	public void setSoldItem(Integer soldItem) {
		this.soldItem = soldItem;
	}

}
