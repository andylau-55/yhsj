package com.yhsjedu.web.agent.controller.admin;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yhsjedu.datacloud.core.entity.Users;
import com.yhsjedu.datacloud.core.service.UsersService;
import com.yhsjedu.datacloud.dao.Page;
import com.yhsjedu.datacloud.utils.CommonUtil;
import com.yhsjedu.datacloud.utils.DataTableBean;
import com.yhsjedu.datacloud.utils.DateUtility;
import com.yhsjedu.datacloud.utils.ResultBean;

@Controller
@RequestMapping("/admin/user/")
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request){
		request.getSession().setAttribute("users", null);
		return "login";
	}

	@RequestMapping(value = "userLogin")
	@ResponseBody
	public Object userLogin(HttpServletRequest request,Users user){
		int login = usersService.usersLogin(user,request);
		return login;
	}
	
	@RequestMapping(value = "getAllUsers")
	@ResponseBody
	public Object getAllUsers(DataTableBean dtb,Users user){
		ResultBean retValue = new ResultBean();
		Page<Users> pagers = usersService.getUsersListPageList(dtb,user);
		retValue.setData(CommonUtil.getJsonByPagers(pagers, dtb));
		return retValue;
	}
	
	@RequestMapping(value = "saveUsers")
	@ResponseBody
	public Object saveUsers(Users users){
		ResultBean retVal = new ResultBean();
		Users perusers =null;
		if(users.getUserId()!=null&&!users.getUserId().equals("")){
			perusers = usersService.getUsersById(users.getUserId());
		}else{
			perusers = new Users();
			perusers.setCreateData(DateUtility.formatDateToString(DateUtility.getSystemDateTime(), DateUtility.DATE_PATTERN_SLASH_YMD));
		}
		if(perusers!=null){
			if(users.getState()!=null&&!users.getState().equals("")){
				perusers.setState(users.getState());
			}
			if(users.getUserName()!=null&&!users.getUserName().equals("")){
				perusers.setUserName(users.getUserName());
			}
			if(users.getUserDspName()!=null&&!users.getUserDspName().equals("")){
				perusers.setUserDspName(users.getUserDspName());
			}
			if(users.getCreateData()!=null&&!users.getCreateData().equals("")){
				perusers.setCreateData(users.getCreateData());
			}
			if(users.getPassword()!=null&&!users.getPassword().equals("")){
				perusers.setPassword(users.getPassword());
			}
			users = usersService.saveUsers(perusers);
			retVal.setMsg("保存成功");
	        retVal.setData(users);
		}
		return retVal;
	}
	
	@RequestMapping(value = "deleteUsers")
	@ResponseBody
	public Object deleteUsers(Users users){
		usersService.deleteUsers(users);
		return null;
	}
	
}
