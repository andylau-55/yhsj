package com.yhsjedu.web.agent.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yhsjedu.datacloud.core.entity.Replace;
import com.yhsjedu.datacloud.core.service.ReplaceService;
import com.yhsjedu.datacloud.dao.Page;
import com.yhsjedu.datacloud.utils.CommonUtil;
import com.yhsjedu.datacloud.utils.DataTableBean;
import com.yhsjedu.datacloud.utils.DateUtility;
import com.yhsjedu.datacloud.utils.ResultBean;
@Controller
@RequestMapping("/admin/replace/")
public class ReplaceController {
	
	@Autowired
	private ReplaceService replaceService;
	
	@ResponseBody
	@RequestMapping("getReplacePageList")
	public Object getReplacePageList(DataTableBean dtb,Replace replace){
		ResultBean retValue = new ResultBean();
        Page<Replace> page = replaceService.getReplacePageList(dtb,replace);
        retValue.setData(CommonUtil.getJsonByPagers(page, dtb));
        return retValue;
    }
	
	@ResponseBody
	@RequestMapping("deleteReplaceById")
	public Object deleteReplaceById(Replace replace){
		ResultBean retValue = new ResultBean();
		if(replace.getId()==null){
			retValue.setErrMsg("请传人ID");
			return retValue;
		}
        replaceService.deleteReplace(replace);
        retValue.setMsg("删除成功");
        return retValue;
    }
	
	@ResponseBody
    @RequestMapping(value = "saveReplace")
    public Object saveReplace(Replace replace){
        ResultBean retVal = new ResultBean();
        	
    	Replace oldReplace = null;
        if (CommonUtil.isEmpty(replace.getId())) {
        	replace.setCreateTime(DateUtility.getSystemDateTime());
        } else {
        	oldReplace = replaceService.getReplaceById(replace.getId());
        	replace.setCreateTime(oldReplace.getCreateTime());
        	if (CommonUtil.isEmpty(replace.getType())) {
        		replace.setType(oldReplace.getType());
        	}
        	if (CommonUtil.isEmpty(replace.getImg())) {
        		replace.setImg(oldReplace.getImg());
        	}
        	if (CommonUtil.isEmpty(replace.getTitle())) {
        		replace.setTitle(oldReplace.getTitle());
        	}
        	if (CommonUtil.isEmpty(replace.getContent())) {
        		replace.setContent(oldReplace.getContent());
        	}
        	if (CommonUtil.isEmpty(replace.getUrl())) {
        		replace.setUrl(oldReplace.getUrl());
        	}
        	if (CommonUtil.isEmpty(replace.getColour())) {
        		replace.setColour(oldReplace.getColour());
        	}
        	if (CommonUtil.isEmpty(replace.getHoverColour())) {
        		replace.setHoverColour(oldReplace.getHoverColour());
        	}
        	if (CommonUtil.isEmpty(replace.getState())) {
        		replace.setState(oldReplace.getState());
        	}
        	if (CommonUtil.isEmpty(replace.getPageOrder())) {
        		replace.setPageOrder(oldReplace.getPageOrder());
        	}
        }
        // 保存信息
        replace = replaceService.saveReplace(replace);
        retVal.setMsg("保存成功");
        retVal.setData(replace);
        return retVal;
    }
	
	
}
