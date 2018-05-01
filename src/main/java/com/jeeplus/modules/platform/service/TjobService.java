/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.platform.entity.Tjob;
import com.jeeplus.modules.platform.dao.TjobDao;

/**
 * 商家职位信息Service
 * @author handejun
 * @version 2018-05-01
 */
@Service
@Transactional(readOnly = true)
public class TjobService extends CrudService<TjobDao, Tjob> {

	public Tjob get(String id) {
		return super.get(id);
	}
	
	public List<Tjob> findList(Tjob tjob) {
		return super.findList(tjob);
	}
	
	public Page<Tjob> findPage(Page<Tjob> page, Tjob tjob) {
		return super.findPage(page, tjob);
	}
	
	@Transactional(readOnly = false)
	public void save(Tjob tjob) {
		super.save(tjob);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tjob tjob) {
		super.delete(tjob);
	}
	
	public void update(Tjob tjob){
		super.dao.update(tjob);
	}
	
	
}