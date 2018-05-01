/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学生评价信息审核Entity
 * @author handejun
 * @version 2018-04-30
 */
public class StudentComment extends DataEntity<StudentComment> {
	
	private static final long serialVersionUID = 1L;
	private String stuId;		// 学生userid
	private String selId;		// 商家userid
	private Integer score;		// 评价分
	private String comment;		// 评价内容
	private Integer status;		// 审核状态
	
	public StudentComment() {
		super();
	}

	public StudentComment(String id){
		super(id);
	}

	@ExcelField(title="学生userid", align=2, sort=1)
	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	
	@ExcelField(title="商家userid", align=2, sort=2)
	public String getSelId() {
		return selId;
	}

	public void setSelId(String selId) {
		this.selId = selId;
	}
	
	@NotNull(message="评价分不能为空")
	@Min(value=0,message="评价分的最小值不能小于0")
	@Max(value=1,message="评价分的最大值不能超过1")
	@ExcelField(title="评价分", align=2, sort=3)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Length(min=20, max=255, message="评价内容长度必须介于 20 和 255 之间")
	@ExcelField(title="评价内容", align=2, sort=4)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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