/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.platform.entity.Tjob;

/**
 * 商家职位信息DAO接口
 * @author handejun
 * @version 2018-05-01
 */
@MyBatisDao
public interface TjobDao extends CrudDao<Tjob> {

	
}