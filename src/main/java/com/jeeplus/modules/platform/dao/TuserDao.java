/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.platform.entity.Tuser;

/**
 * 用户信息DAO接口
 * @author handejun
 * @version 2018-04-30
 */
@MyBatisDao
public interface TuserDao extends CrudDao<Tuser> {

	
}