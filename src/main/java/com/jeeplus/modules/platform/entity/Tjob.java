/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商家职位信息Entity
 * @author handejun
 * @version 2018-05-01
 */
public class Tjob extends DataEntity<Tjob> {
	
	private static final long serialVersionUID = 1L;
	private String sellerId;		// 商家的userid
	private String name;		// 商家名称
	private Integer salary;		// 工资
	private Date pubTime;		// 发布时间
	private Integer status;		// 审核状态
	
	public Tjob() {
		super();
	}

	public Tjob(String id){
		super(id);
	}

	@Length(min=1, max=Integer.MAX_VALUE, message="商家的userid长度必须介于 1 和  之间")
	@ExcelField(title="商家的userid", align=2, sort=1)
	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	@Length(min=1, max=20, message="商家名称长度必须介于 1 和  之间")
	@ExcelField(title="商家名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="工资不能为空")
	@ExcelField(title="工资", align=2, sort=4)
	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="发布时间不能为空")
	@ExcelField(title="发布时间", align=2, sort=5)
	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	
	@NotNull(message="审核状态不能为空")
	@ExcelField(title="审核状态", dictType="status", align=2, sort=6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}