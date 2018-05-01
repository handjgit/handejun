/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.platform.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.platform.entity.Tuser;
import com.jeeplus.modules.platform.service.TuserService;
import com.jeeplus.modules.sys.service.SystemService;

/**
 * 用户信息Controller
 * @author handejun
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/platform/tuser")
public class TuserController extends BaseController {

	@Autowired
	private TuserService tuserService;
	
	@ModelAttribute
	public Tuser get(@RequestParam(required=false) String id) {
		Tuser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tuserService.get(id);
		}
		if (entity == null){
			entity = new Tuser();
		}
		return entity;
	}
	
	/**
	 * 员工信息列表页面
	 */
	@RequiresPermissions("platform:tuser:list")
	@RequestMapping(value = {"list", ""})
	public String list(Tuser tuser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tuser> page = tuserService.findPage(new Page<Tuser>(request, response), tuser); 
		model.addAttribute("page", page);
		String retStr = "modules/platform/tuserList";
		if(tuser.getType()!=null){
			if(tuser.getType()==2){
				retStr = "modules/platform/tuserList2";
			}else if(tuser.getType()==3){
				retStr = "modules/platform/tuserList3";
			}
		}
		return retStr;
	}

	/**
	 * 查看，增加，编辑员工信息表单页面
	 */
	@RequiresPermissions(value={"platform:tuser:view","platform:tuser:add","platform:tuser:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tuser tuser, Model model) {
		model.addAttribute("tuser", tuser);
		return "modules/platform/tuserForm";
	}

	/**
	 * 保存员工信息
	 */
	@RequiresPermissions(value={"platform:tuser:add","platform:tuser:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Tuser tuser, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tuser)){
			return form(tuser, model);
		}
		if(StringUtils.isNotBlank(tuser.getPassword())){
			tuser.setPassword(SystemService.entryptPassword(tuser.getPassword()));
		}
		if(!tuser.getIsNewRecord()){//编辑表单保存
			Tuser t = tuserService.get(tuser.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tuser, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tuserService.save(t);//保存
		}else{//新增表单保存
			tuserService.save(tuser);//保存
		}
		addMessage(redirectAttributes, "保存员工信息成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
	}
	
	/**
	 * 删除员工信息
	 */
	@RequiresPermissions("platform:tuser")
	@RequestMapping(value = "shenhe")
	public String shenhe(Tuser tuser, RedirectAttributes redirectAttributes) {
		Tuser user = tuserService.get(tuser);
		if(user!=null){
			user.setStatus(1);
		}
		tuserService.update(user);
		addMessage(redirectAttributes, "审核成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/list?type=2";
	}
	
	/**
	 * 删除员工信息
	 */
	@RequiresPermissions("platform:tuser:del")
	@RequestMapping(value = "delete")
	public String delete(Tuser tuser, RedirectAttributes redirectAttributes) {
		tuserService.delete(tuser);
		addMessage(redirectAttributes, "删除员工信息成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
	}
	
	/**
	 * 批量删除员工信息
	 */
	@RequiresPermissions("platform:tuser:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tuserService.delete(tuserService.get(id));
		}
		addMessage(redirectAttributes, "删除员工信息成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("platform:tuser:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Tuser tuser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "员工信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tuser> page = tuserService.findPage(new Page<Tuser>(request, response, -1), tuser);
    		new ExportExcel("员工信息", Tuser.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出员工信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("platform:tuser:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tuser> list = ei.getDataList(Tuser.class);
			for (Tuser tuser : list){
				try{
					tuserService.save(tuser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条员工信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条员工信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入员工信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
    }
	
	/**
	 * 下载导入员工信息数据模板
	 */
	@RequiresPermissions("platform:tuser:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "员工信息数据导入模板.xlsx";
    		List<Tuser> list = Lists.newArrayList(); 
    		new ExportExcel("员工信息数据", Tuser.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tuser/?repage";
    }
	
	
	

}