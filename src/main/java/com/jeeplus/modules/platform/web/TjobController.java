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
import com.jeeplus.modules.platform.entity.Tjob;
import com.jeeplus.modules.platform.service.TjobService;

/**
 * 商家职位信息Controller
 * @author handejun
 * @version 2018-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/platform/tjob")
public class TjobController extends BaseController {

	@Autowired
	private TjobService tjobService;
	
	@ModelAttribute
	public Tjob get(@RequestParam(required=false) String id) {
		Tjob entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tjobService.get(id);
		}
		if (entity == null){
			entity = new Tjob();
		}
		return entity;
	}
	
	/**
	 * 职位发布列表页面
	 */
	@RequiresPermissions("platform:tjob:list")
	@RequestMapping(value = {"list", ""})
	public String list(Tjob tjob, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tjob> page = tjobService.findPage(new Page<Tjob>(request, response), tjob); 
		model.addAttribute("page", page);
		return "modules/platform/tjobList";
	}

	/**
	 * 查看，增加，编辑职位发布表单页面
	 */
	@RequiresPermissions(value={"platform:tjob:view","platform:tjob:add","platform:tjob:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tjob tjob, Model model) {
		model.addAttribute("tjob", tjob);
		return "modules/platform/tjobForm";
	}

	/**
	 * 保存职位发布
	 */
	@RequiresPermissions(value={"platform:tjob:add","platform:tjob:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Tjob tjob, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tjob)){
			return form(tjob, model);
		}
		if(!tjob.getIsNewRecord()){//编辑表单保存
			Tjob t = tjobService.get(tjob.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tjob, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tjobService.save(t);//保存
		}else{//新增表单保存
			tjobService.save(tjob);//保存
		}
		addMessage(redirectAttributes, "保存职位发布成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
	}
	
	@RequiresPermissions("platform:tjob:list")
	@RequestMapping(value = "shenhe")
	public String shenhe(Tjob tjob, RedirectAttributes redirectAttributes) {
		tjob.setStatus(1);
		tjobService.update(tjob);
		addMessage(redirectAttributes, "职位信息审核成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/list";
	}
	
	/**
	 * 删除职位发布
	 */
	@RequiresPermissions("platform:tjob:del")
	@RequestMapping(value = "delete")
	public String delete(Tjob tjob, RedirectAttributes redirectAttributes) {
		tjobService.delete(tjob);
		addMessage(redirectAttributes, "删除职位发布成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
	}
	
	/**
	 * 批量删除职位发布
	 */
	@RequiresPermissions("platform:tjob:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tjobService.delete(tjobService.get(id));
		}
		addMessage(redirectAttributes, "删除职位发布成功");
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("platform:tjob:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Tjob tjob, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "职位发布"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tjob> page = tjobService.findPage(new Page<Tjob>(request, response, -1), tjob);
    		new ExportExcel("职位发布", Tjob.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出职位发布记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("platform:tjob:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tjob> list = ei.getDataList(Tjob.class);
			for (Tjob tjob : list){
				try{
					tjobService.save(tjob);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条职位发布记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条职位发布记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入职位发布失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
    }
	
	/**
	 * 下载导入职位发布数据模板
	 */
	@RequiresPermissions("platform:tjob:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "职位发布数据导入模板.xlsx";
    		List<Tjob> list = Lists.newArrayList(); 
    		new ExportExcel("职位发布数据", Tjob.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/tjob/?repage";
    }
	
	
	

}