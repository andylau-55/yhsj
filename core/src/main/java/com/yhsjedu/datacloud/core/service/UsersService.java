package com.yhsjedu.datacloud.core.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yhsjedu.datacloud.core.dao.UsersDao;
import com.yhsjedu.datacloud.core.entity.Users;
import com.yhsjedu.datacloud.dao.Page;
import com.yhsjedu.datacloud.utils.CommonUtil;
import com.yhsjedu.datacloud.utils.DataTableBean;


@Service
@Transactional(readOnly = true)
public class UsersService {

	
	@Autowired
	private UsersDao usersDao;
	
	public Users getUsersById(Long id){
		return usersDao.get(id);
	}
	
	public List<Users> getAllUsers(){
		return usersDao.getAll();
	}
	
	public int usersLogin(Users users,HttpServletRequest request){
		String sql = "select * from users where userName = ? and state = ?";
		Object[] parameters = {users.getUserName(),"1"};
		List<Users> userss = usersDao.sqlQuery(Users.class,sql, parameters);
		
		if(userss==null||userss.size()==0){
			return 1;
		}else{
			for(Users user : userss){
				if(user.getPassword().equals(users.getPassword())){
					request.getSession().setAttribute("users", user);
					return 0;
				}
			}
		}
		return 2;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Users saveUsers(Users users){
		return usersDao.save(users);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteUsers(Users users){
		usersDao.delete(users);
	}
	
	public Page<Users> getUsersListPageList(DataTableBean dtb,Users user){
		String querySql = CommonUtil.getSqlByObject(user, "vo", null, null);
		StringBuffer sql = new StringBuffer("select * from users as vo where 1=1");
		sql.append(querySql);
		sql.append(dtb.getOrderSql());
		Page<Users> page = new Page<Users>(dtb);
		return usersDao.getPageBySql(page,Users.class, sql.toString(),user);
	}
	
}
