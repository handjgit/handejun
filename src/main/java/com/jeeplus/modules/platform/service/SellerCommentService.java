/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.platform.entity.SellerComment;
import com.jeeplus.modules.platform.dao.SellerCommentDao;

/**
 * 商家的评论信息审核Service
 * @author handejun
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class SellerCommentService extends CrudService<SellerCommentDao, SellerComment> {

	public SellerComment get(String id) {
		return super.get(id);
	}
	
	public List<SellerComment> findList(SellerComment sellerComment) {
		return super.findList(sellerComment);
	}
	
	public Page<SellerComment> findPage(Page<SellerComment> page, SellerComment sellerComment) {
		return super.findPage(page, sellerComment);
	}
	
	@Transactional(readOnly = false)
	public void save(SellerComment sellerComment) {
		super.save(sellerComment);
	}
	
	@Transactional(readOnly = false)
	public void delete(SellerComment sellerComment) {
		super.delete(sellerComment);
	}
	
	@Transactional(readOnly = false)
	public void update(SellerComment sellerComment){
		super.dao.update(sellerComment);
	}
	
	
}