/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商家结算工资信息审核Entity
 * @author handejun
 * @version 2018-04-30
 */
public class SalaryBalance extends DataEntity<SalaryBalance> {
	
	private static final long serialVersionUID = 1L;
	private String selId;		// 商家userId
	private String stuId;		// 学生userid
	private String pid;		// 职位id
	private Integer salary;		// 工资
	private Integer status;		// 审核状态
	
	public SalaryBalance() {
		super();
	}

	public SalaryBalance(String id){
		super(id);
	}

	@ExcelField(title="商家userId", align=2, sort=1)
	public String getSelId() {
		return selId;
	}

	public void setSelId(String selId) {
		this.selId = selId;
	}
	
	@ExcelField(title="学生userid", align=2, sort=2)
	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	
	@ExcelField(title="职位id", align=2, sort=3)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	@NotNull(message="工资不能为空")
	@ExcelField(title="工资", align=2, sort=4)
	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	
	@NotNull(message="审核状态不能为空")
	@ExcelField(title="审核状态", dictType="status", align=2, sort=5)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}