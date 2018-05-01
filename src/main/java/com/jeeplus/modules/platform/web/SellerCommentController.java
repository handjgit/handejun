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
import com.jeeplus.modules.platform.entity.SellerComment;
import com.jeeplus.modules.platform.service.SellerCommentService;

/**
 * 商家的评论信息审核Controller
 * @author handejun
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/platform/sellerComment")
public class SellerCommentController extends BaseController {

	@Autowired
	private SellerCommentService sellerCommentService;
	
	@ModelAttribute
	public SellerComment get(@RequestParam(required=false) String id) {
		SellerComment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sellerCommentService.get(id);
		}
		if (entity == null){
			entity = new SellerComment();
		}
		return entity;
	}
	
	/**
	 * 商家评论审核成功列表页面
	 */
	@RequiresPermissions("platform:sellerComment:list")
	@RequestMapping(value = {"list", ""})
	public String list(SellerComment sellerComment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SellerComment> page = sellerCommentService.findPage(new Page<SellerComment>(request, response), sellerComment); 
		model.addAttribute("page", page);
		return "modules/platform/sellerCommentList";
	}

	/**
	 * 查看，增加，编辑商家评论审核成功表单页面
	 */
	@RequiresPermissions(value={"platform:sellerComment:view","platform:sellerComment:add","platform:sellerComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SellerComment sellerComment, Model model) {
		model.addAttribute("sellerComment", sellerComment);
		return "modules/platform/sellerCommentForm";
	}

	/**
	 * 保存商家评论审核成功
	 */
	@RequiresPermissions(value={"platform:sellerComment:add","platform:sellerComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SellerComment sellerComment, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sellerComment)){
			return form(sellerComment, model);
		}
		if(!sellerComment.getIsNewRecord()){//编辑表单保存
			SellerComment t = sellerCommentService.get(sellerComment.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sellerComment, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sellerCommentService.save(t);//保存
		}else{//新增表单保存
			sellerCommentService.save(sellerComment);//保存
		}
		addMessage(redirectAttributes, "保存商家评论审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
	}
	
	/**
	 * 审核商家评论
	 */
	@RequiresPermissions("platform:sellerComment:list")
	@RequestMapping(value = "shenhe")
	public String shenhe(SellerComment sellerComment, RedirectAttributes redirectAttributes) {
		sellerComment.setStatus(1);
		sellerCommentService.update(sellerComment);
		addMessage(redirectAttributes, "审核商家评论");
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/list";
	}
	
	/**
	 * 删除商家评论审核成功
	 */
	@RequiresPermissions("platform:sellerComment:del")
	@RequestMapping(value = "delete")
	public String delete(SellerComment sellerComment, RedirectAttributes redirectAttributes) {
		sellerCommentService.delete(sellerComment);
		addMessage(redirectAttributes, "删除商家评论审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
	}
	
	/**
	 * 批量删除商家评论审核成功
	 */
	@RequiresPermissions("platform:sellerComment:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sellerCommentService.delete(sellerCommentService.get(id));
		}
		addMessage(redirectAttributes, "删除商家评论审核成功成功");
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("platform:sellerComment:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SellerComment sellerComment, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "商家评论审核成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SellerComment> page = sellerCommentService.findPage(new Page<SellerComment>(request, response, -1), sellerComment);
    		new ExportExcel("商家评论审核成功", SellerComment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出商家评论审核成功记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("platform:sellerComment:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SellerComment> list = ei.getDataList(SellerComment.class);
			for (SellerComment sellerComment : list){
				try{
					sellerCommentService.save(sellerComment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商家评论审核成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条商家评论审核成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入商家评论审核成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
    }
	
	/**
	 * 下载导入商家评论审核成功数据模板
	 */
	@RequiresPermissions("platform:sellerComment:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "商家评论审核成功数据导入模板.xlsx";
    		List<SellerComment> list = Lists.newArrayList(); 
    		new ExportExcel("商家评论审核成功数据", SellerComment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/platform/sellerComment/?repage";
    }
	
	
	

}