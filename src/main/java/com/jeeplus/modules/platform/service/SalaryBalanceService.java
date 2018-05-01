/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.platform.entity.SalaryBalance;
import com.jeeplus.modules.platform.dao.SalaryBalanceDao;

/**
 * 商家结算工资信息审核Service
 * @author handejun
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class SalaryBalanceService extends CrudService<SalaryBalanceDao, SalaryBalance> {

	public SalaryBalance get(String id) {
		return super.get(id);
	}
	
	public List<SalaryBalance> findList(SalaryBalance salaryBalance) {
		return super.findList(salaryBalance);
	}
	
	public Page<SalaryBalance> findPage(Page<SalaryBalance> page, SalaryBalance salaryBalance) {
		return super.findPage(page, salaryBalance);
	}
	
	@Transactional(readOnly = false)
	public void save(SalaryBalance salaryBalance) {
		super.save(salaryBalance);
	}
	
	@Transactional(readOnly = false)
	public void delete(SalaryBalance salaryBalance) {
		super.delete(salaryBalance);
	}
	
	@Transactional(readOnly = false)
	public void update(SalaryBalance salaryBalance){
		super.dao.update(salaryBalance);
	}
	
	
}