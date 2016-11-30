package com.yhsjedu.web.agent.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yhsjedu.datacloud.core.entity.Replace;
import com.yhsjedu.datacloud.core.entity.Users;
import com.yhsjedu.datacloud.core.service.ReplaceService;

@Controller
public class HomeController {
	
	@Autowired
	private ReplaceService replaceService;

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
    	Replace replace = new Replace();
    	replace.setState(1L);
    	request.setAttribute("replaces", replaceService.getReplaceList(replace));
    	return "index"; // 转到主页
    }
    @RequestMapping("/admin")
    public String admin(HttpServletRequest request) {
    	Users users = (Users) request.getSession().getAttribute("users");
        if (users != null && users.getUserName() != null) {
            return "admin/index"; // 转到主页
        } else {
            return "login"; // 转到登录页面
        }
    }

    @RequestMapping("/{fileCode}")
    public String firstLevelPath(@PathVariable String fileCode) {
        return fileCode;
    }

    @RequestMapping("{type}/{fileCode}")
    public String secondLevelPath(@PathVariable String type, @PathVariable String fileCode, HttpServletRequest request) {
        // 参数
        for (Object key : request.getParameterMap().keySet()) {
            request.setAttribute(key.toString(), request.getParameter(key.toString()));
        }
        return type + "/" + fileCode;
    }

    @RequestMapping("/admin/{fileCode}")
    public String adminAll(HttpServletRequest request, @PathVariable String fileCode) {
    	Users users = (Users) request.getSession().getAttribute("users");
        if (users != null && users.getUserName() != null) {
            return "admin/"+ fileCode; 
        } else {
            return "login"; // 转到登录页面
        }
    }
}
