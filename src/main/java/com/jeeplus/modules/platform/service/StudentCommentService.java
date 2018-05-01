/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.platform.entity.StudentComment;
import com.jeeplus.modules.platform.dao.StudentCommentDao;

/**
 * 学生评价信息审核Service
 * @author handejun
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class StudentCommentService extends CrudService<StudentCommentDao, StudentComment> {

	public StudentComment get(String id) {
		return super.get(id);
	}
	
	public List<StudentComment> findList(StudentComment studentComment) {
		return super.findList(studentComment);
	}
	
	public Page<StudentComment> findPage(Page<StudentComment> page, StudentComment studentComment) {
		return super.findPage(page, studentComment);
	}
	
	@Transactional(readOnly = false)
	public void save(StudentComment studentComment) {
		super.save(studentComment);
	}
	
	@Transactional(readOnly = false)
	public void delete(StudentComment studentComment) {
		super.delete(studentComment);
	}
	

	@Transactional(readOnly = false)
	public void update(StudentComment studentComment){
		super.dao.update(studentComment);
	}
	
	
}