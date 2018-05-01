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
import com.jeeplus.modules.platform.entity.StudentComment;
import com.jeeplus.modules.platform.service.StudentCommentService;

/**
 * 学生评价信息审核Controller
 * @author handejun
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/platform/studentComment")
public class StudentCommentController extends BaseController {

	@Autowired
	private StudentCommentService studentCommentService;
	
	@ModelAttribute
	public StudentComment get(@RequestParam(required=false) String id) {
		StudentComment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = studentCommentService.get(id);
		}
		if (entity == null){
			entity = new StudentComment();
		}
		return entity;
	}
	
	/**
	 * 学生评价信息审核成功列表页面
	 */
	@RequiresPermissions("platform:studentComment:list")
	@RequestMapping(value = {"list", ""})
	public String list(StudentComment studentComment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StudentComment> page = studentCommentService.findPage(new Page<StudentComment>(request, response), studentComment); 
		model.addAttribute("page", page);
		return "modules/platform/studentCommentList";
	}

	/**
	 * 查看，增加，编辑学生评价信息审核成功表单页面
	 */
	@RequiresPermissions(value={"platform:studentComment:view","platform:studentComment:add","platform:studentComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(StudentComment studentComment, Model model) {
		model.addAttribute("studentComment", studentComment);
		return "modules/platform/studentCommentForm";
	}

	/**
	 * 保存学生评价信息审核成功
	 */
	@RequiresPermissions(value={"platform:studentComment:add","platform:studentComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(StudentComment studentComment, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, studentComment)){
			return form(studentComment, model);
		}
		if(!studentComment.getIsNewRecord()){//编辑表单保存
			StudentComment t = studentCommentService.get(studentComment.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(studentComment, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			studentCommentService.save(t);//保存
		}else{//新增表单保存
			studentCommentService.save(studentComment);//保存
		}
		addMessage(redirectAttributes, "保存学生评价信息审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
	}
	
	@RequiresPermissions("platform:studentComment:list")
	@RequestMapping(value = "shenhe")
	public String shenhe(StudentComment studentComment, RedirectAttributes redirectAttributes) {
		studentComment.setStatus(1);
		studentCommentService.update(studentComment);
		addMessage(redirectAttributes, "学生评论审核成功");
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/list";
	}
	
	/**
	 * 删除学生评价信息审核成功
	 */
	@RequiresPermissions("platform:studentComment:del")
	@RequestMapping(value = "delete")
	public String delete(StudentComment studentComment, RedirectAttributes redirectAttributes) {
		studentCommentService.delete(studentComment);
		addMessage(redirectAttributes, "删除学生评价信息审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
	}
	
	/**
	 * 批量删除学生评价信息审核成功
	 */
	@RequiresPermissions("platform:studentComment:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			studentCommentService.delete(studentCommentService.get(id));
		}
		addMessage(redirectAttributes, "删除学生评价信息审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("platform:studentComment:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(StudentComment studentComment, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生评价信息审核成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StudentComment> page = studentCommentService.findPage(new Page<StudentComment>(request, response, -1), studentComment);
    		new ExportExcel("学生评价信息审核成功", StudentComment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生评价信息审核成功记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("platform:studentComment:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StudentComment> list = ei.getDataList(StudentComment.class);
			for (StudentComment studentComment : list){
				try{
					studentCommentService.save(studentComment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生评价信息审核成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生评价信息审核成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生评价信息审核成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
    }
	
	/**
	 * 下载导入学生评价信息审核成功数据模板
	 */
	@RequiresPermissions("platform:studentComment:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生评价信息审核成功数据导入模板.xlsx";
    		List<StudentComment> list = Lists.newArrayList(); 
    		new ExportExcel("学生评价信息审核成功数据", StudentComment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/studentComment/?repage";
    }
	
	
	

}