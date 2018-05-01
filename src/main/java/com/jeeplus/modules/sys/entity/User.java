package com.jeeplus.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

public class User extends DataEntity<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2186124017974893337L;
	private String name;		// 姓名
	private String password;		// 密码
	private String phone;		// 手机
	private Integer status;		// 状态
	private Integer type;		// 类型
	private Integer empCount;		// 求职次数
	private Integer empedCount;		// 被雇佣次数
	private Integer score;		// 信任分
	private String website;		// 商家网址
	private String servQQ;		// 客服QQ
	private Integer pubCount;		// 发布职位次数
	private Office office;
	
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	private String loginFlag;	// 是否允许登陆
	private Role role;
	
	private String photo;
	
	private String loginName;
	
	public String getPhoto() {
		return photo;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	private String newPassword;
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	
	public User(String id, String loginName){
		super(id);
		this.name = loginName;
	}
	
	public User(Role role){
		super();
		this.role = role;
	}
	
	public User() {
		super();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public User(String id){
		super(id);
	}

	@Length(min=1, max=20, message="姓名长度必须介于 1 和 20 之间")
	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.loginName = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="密码不能为空")
	@ExcelField(title="密码", align=2, sort=2)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=11, max=11, message="手机长度必须介于 11 和 11 之间")
	@ExcelField(title="手机", align=2, sort=3)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@NotNull(message="状态不能为空")
	@ExcelField(title="状态", dictType="status", align=2, sort=4)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@NotNull(message="类型不能为空")
	@ExcelField(title="类型", dictType="userType", align=2, sort=5)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="求职次数", align=2, sort=6)
	public Integer getEmpCount() {
		return empCount;
	}

	public void setEmpCount(Integer empCount) {
		this.empCount = empCount;
	}
	
	@ExcelField(title="被雇佣次数", align=2, sort=7)
	public Integer getEmpedCount() {
		return empedCount;
	}

	public void setEmpedCount(Integer empedCount) {
		this.empedCount = empedCount;
	}
	
	@ExcelField(title="信任分", align=2, sort=8)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@ExcelField(title="商家网址", align=2, sort=9)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	@ExcelField(title="客服QQ", align=2, sort=10)
	public String getServQQ() {
		return servQQ;
	}

	public void setServQQ(String servQQ) {
		this.servQQ = servQQ;
	}
	
	@ExcelField(title="发布职位次数", align=2, sort=11)
	public Integer getPubCount() {
		return pubCount;
	}

	public void setPubCount(Integer pubCount) {
		this.pubCount = pubCount;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public boolean isAdmin(){
		if(this.name.equals("admin")){
			return true;
		}
		return false;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getOldLoginIp() {
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	public Date getOldLoginDate() {
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}
	
}
