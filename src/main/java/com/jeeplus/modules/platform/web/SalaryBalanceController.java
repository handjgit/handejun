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
import com.jeeplus.modules.platform.entity.SalaryBalance;
import com.jeeplus.modules.platform.service.SalaryBalanceService;

/**
 * 商家结算工资信息审核Controller
 * @author handejun
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/platform/salaryBalance")
public class SalaryBalanceController extends BaseController {

	@Autowired
	private SalaryBalanceService salaryBalanceService;
	
	@ModelAttribute
	public SalaryBalance get(@RequestParam(required=false) String id) {
		SalaryBalance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = salaryBalanceService.get(id);
		}
		if (entity == null){
			entity = new SalaryBalance();
		}
		return entity;
	}
	
	/**
	 * 工资结算审核成功列表页面
	 */
	@RequiresPermissions("platform:salaryBalance:list")
	@RequestMapping(value = {"list", ""})
	public String list(SalaryBalance salaryBalance, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SalaryBalance> page = salaryBalanceService.findPage(new Page<SalaryBalance>(request, response), salaryBalance); 
		model.addAttribute("page", page);
		return "modules/platform/salaryBalanceList";
	}

	/**
	 * 查看，增加，编辑工资结算审核成功表单页面
	 */
	@RequiresPermissions(value={"platform:salaryBalance:view","platform:salaryBalance:add","platform:salaryBalance:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SalaryBalance salaryBalance, Model model) {
		model.addAttribute("salaryBalance", salaryBalance);
		return "modules/platform/salaryBalanceForm";
	}

	/**
	 * 保存工资结算审核成功
	 */
	@RequiresPermissions(value={"platform:salaryBalance:add","platform:salaryBalance:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SalaryBalance salaryBalance, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, salaryBalance)){
			return form(salaryBalance, model);
		}
		if(!salaryBalance.getIsNewRecord()){//编辑表单保存
			SalaryBalance t = salaryBalanceService.get(salaryBalance.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(salaryBalance, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			salaryBalanceService.save(t);//保存
		}else{//新增表单保存
			salaryBalanceService.save(salaryBalance);//保存
		}
		addMessage(redirectAttributes, "保存工资结算审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
	}
	
	/**
	 * 删除工资结算审核成功
	 */
	@RequiresPermissions("platform:salaryBalance")
	@RequestMapping(value = "shenhe")
	public String shenhe(SalaryBalance salaryBalance, RedirectAttributes redirectAttributes) {
		salaryBalance.setStatus(1);
		salaryBalanceService.update(salaryBalance);
		addMessage(redirectAttributes, "工资结算审核成功");
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/list";
	}
	
	/**
	 * 删除工资结算审核成功
	 */
	@RequiresPermissions("platform:salaryBalance:del")
	@RequestMapping(value = "delete")
	public String delete(SalaryBalance salaryBalance, RedirectAttributes redirectAttributes) {
		salaryBalanceService.delete(salaryBalance);
		addMessage(redirectAttributes, "删除工资结算审核成功");
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
	}
	
	/**
	 * 批量删除工资结算审核成功
	 */
	@RequiresPermissions("platform:salaryBalance:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			salaryBalanceService.delete(salaryBalanceService.get(id));
		}
		addMessage(redirectAttributes, "删除工资结算审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("platform:salaryBalance:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SalaryBalance salaryBalance, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工资结算审核成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SalaryBalance> page = salaryBalanceService.findPage(new Page<SalaryBalance>(request, response, -1), salaryBalance);
    		new ExportExcel("工资结算审核成功", SalaryBalance.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出工资结算审核成功记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("platform:salaryBalance:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SalaryBalance> list = ei.getDataList(SalaryBalance.class);
			for (SalaryBalance salaryBalance : list){
				try{
					salaryBalanceService.save(salaryBalance);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工资结算审核成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工资结算审核成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工资结算审核成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
    }
	
	/**
	 * 下载导入工资结算审核成功数据模板
	 */
	@RequiresPermissions("platform:salaryBalance:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工资结算审核成功数据导入模板.xlsx";
    		List<SalaryBalance> list = Lists.newArrayList(); 
    		new ExportExcel("工资结算审核成功数据", SalaryBalance.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/salaryBalance/?repage";
    }
	
	
	

}