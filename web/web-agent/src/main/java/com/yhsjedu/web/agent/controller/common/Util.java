package com.yhsjedu.web.agent.controller.common;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhsjedu.datacloud.dao.Page;

public class Util {
    @Deprecated
    public static HashMap<String, Object> page2map(Page<?> page, int draw) {
        HashMap<String, Object> info = new HashMap<String, Object>();
        info.put("data", page.getResult());
        info.put("recordsTotal", page.getTotalCount());
        info.put("recordsFiltered", page.getTotalCount());
        info.put("draw", draw);
        return info;
    }

    /**
     * 页面数据转为检索用map
     * 
     * @param request
     * @return
     */
    @Deprecated
    public static HashMap<String, Object> getSearchFormData(HttpServletRequest request) {
        String searchFormJsonStr = request.getParameter("searchFormStr");
        HashMap<String, Object> retVal = new HashMap<String, Object>();

        JSONArray array = (JSONArray) JSON.parse(searchFormJsonStr);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            retVal.put(obj.getString("name"), obj.getString("value"));
        }

        return retVal;
    }

    /**
     * 去除html标签
     * 
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

}
