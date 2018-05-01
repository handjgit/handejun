/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商家的评论信息审核Entity
 * @author handejun
 * @version 2018-04-30
 */
public class SellerComment extends DataEntity<SellerComment> {
	
	private static final long serialVersionUID = 1L;
	private String selId;		// 商家userid
	private String stuId;		// 学生userid
	private Integer score;		// 评分
	private String comment;		// 评价
	private Integer status;		// 审核状态
	
	public SellerComment() {
		super();
	}

	public SellerComment(String id){
		super(id);
	}

	@ExcelField(title="商家userid", align=2, sort=1)
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
	
	@NotNull(message="评分不能为空")
	@ExcelField(title="评分", align=2, sort=3)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@ExcelField(title="评价", align=2, sort=4)
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