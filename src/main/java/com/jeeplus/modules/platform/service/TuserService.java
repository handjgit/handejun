/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.platform.entity.Tuser;
import com.jeeplus.modules.platform.dao.TuserDao;

/**
 * 用户信息Service
 * @author handejun
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class TuserService extends CrudService<TuserDao, Tuser> {

	public Tuser get(String id) {
		return super.get(id);
	}
	
	public List<Tuser> findList(Tuser tuser) {
		return super.findList(tuser);
	}
	
	public Page<Tuser> findPage(Page<Tuser> page, Tuser tuser) {
		return super.findPage(page, tuser);
	}
	
	@Transactional(readOnly = false)
	public void save(Tuser tuser) {
		tuser.setId(IdGen.uuid());
		super.save(tuser);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tuser tuser) {
		super.delete(tuser);
	}
	
	@Transactional(readOnly = false)
	public void update(Tuser tuser){
		super.dao.update(tuser);
	}
	
	
}