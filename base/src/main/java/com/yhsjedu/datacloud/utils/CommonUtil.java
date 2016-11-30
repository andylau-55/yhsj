package com.yhsjedu.datacloud.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.FacetedPage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhsjedu.datacloud.dao.Page;

public class CommonUtil {
    /**
     * 检查对象是否为空，字符串为“”返回true，list个数为0返回true
     * 
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return true;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return true;
            }
        } else if (obj instanceof String) {
            if ("".equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static Long obj2Long(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public static String null2str(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static List trans(List lst, Class clazz) throws Exception {
        List retLst = new ArrayList();
        for (Object obj : lst) {
            Object toBean = clazz.newInstance();
            BeanUtils.copyProperties(obj, toBean);
            retLst.add(toBean);
        }
        return retLst;
    }

    public static void copyProperties(Object from, Object to) {
        BeanUtils.copyProperties(from, to);
    }

    /**
     * 根据税前金额计算税额(劳务税)
     * 
     * @param amount
     * @return
     */
    public static BigDecimal getServiceTax(BigDecimal amount) {
        if (isEmpty(amount)) {
            return null;
        }
        BigDecimal d800 = new BigDecimal(800);
        BigDecimal d4000 = new BigDecimal(4000);
        BigDecimal d25000 = new BigDecimal(25000);
        BigDecimal d62500 = new BigDecimal(62500);

        if (amount.compareTo(d800) <= 0) {
            // 月提款额低于800，应缴税额为：0
            return new BigDecimal(0);
        } else if (amount.compareTo(d4000) <= 0) {
            // 月提款额800-4000，应缴税额为：（收入-800）*20%
            return setScale(amount.subtract(d800).multiply(new BigDecimal(0.2)), 2);
        } else if (amount.compareTo(d25000) <= 0) {
            // 月提款额4000-25000，应缴税额为：（收入-收入*20%）*20%
            return setScale(amount.subtract(amount.multiply(new BigDecimal(0.2))).multiply(new BigDecimal(0.2)), 2);
        } else if (amount.compareTo(d62500) <= 0) {
            // 月提款额25000-62500，应缴税额为：（收入-收入*20%）*30%-2000
            return setScale(amount.subtract(amount.multiply(new BigDecimal(0.2))).multiply(new BigDecimal(0.3)), 2);
        } else {
            // 月提款额高于62500，应缴税额为：（收入-收入*20%）*40%-7000
            return setScale(amount.subtract(amount.multiply(new BigDecimal(0.2))).multiply(new BigDecimal(0.4)), 2);
        }
    }

    public static BigDecimal getServiceTaxRate(BigDecimal amount) {
        if (isEmpty(amount)) {
            return null;
        }
        BigDecimal d800 = new BigDecimal(800);
        BigDecimal d4000 = new BigDecimal(4000);
        BigDecimal d25000 = new BigDecimal(25000);
        BigDecimal d62500 = new BigDecimal(62500);

        if (amount.compareTo(d800) <= 0) {
            // 月提款额低于800，应缴税额为：0
            return setScale(new BigDecimal(0), 2);
        } else if (amount.compareTo(d4000) <= 0) {
            // 月提款额800-4000，应缴税额为：（收入-800）*20%
            return setScale(new BigDecimal(0.2), 2);
        } else if (amount.compareTo(d25000) <= 0) {
            // 月提款额4000-25000，应缴税额为：（收入-收入*20%）*20%
            return setScale(new BigDecimal(0.2), 2);
        } else if (amount.compareTo(d62500) <= 0) {
            // 月提款额25000-62500，应缴税额为：（收入-收入*20%）*30%-2000
            return setScale(new BigDecimal(0.3), 2);
        } else {
            // 月提款额高于62500，应缴税额为：（收入-收入*20%）*40%-7000
            return setScale(new BigDecimal(0.4), 2);
        }
    }

    public static BigDecimal setScale(BigDecimal decimal, int scale) {
        if (isEmpty(decimal)) {
            return null;
        }
        return decimal.divide(new BigDecimal(1), scale, RoundingMode.HALF_UP);
    }

    /**
     * @param object 需要查询的对象 如果是between则需有startXX endXX的参数
     * @param tableAlias sql的表的别名
     * @param sqlTypeMap 查询字段对应的查询类别,键为相应的查询字段名 值为 in,like,between,= ,>,<,>=,<=,<>
     * @param tableAliasMap 表的别名的MAP,键为相应的查询字段名 值为表的别名,如果没有该查询字段名则使用tableAlias
     * @return 示例:
     * 
     *         <pre>
     * Long,Number,Integer,Short,BigInteger,boolean,byte[],int,long,short,Boolean,Byte默认为=;
     * Array,List,Set,Map默认为in;
     * Date,Time,BigDecimal,Timestamp,Double,Float,double,float默认为between;
     * String和其他默认为like;
     * 
     *         <pre>
     * Select2Bean s2 = new Select2Bean();
     * s2.setGroupNm(&quot;123123kjndaksj&quot;);
     * s2.setId(&quot;1&quot;);
     * s2.setParentId(&quot;0&quot;);
     * HashMap&lt;String, String&gt; map = new HashMap&lt;String, String&gt;();
     * map.put(&quot;groupNm&quot;, &quot;between&quot;);
     * map.put(&quot;id&quot;, &quot;=&quot;);
     * map.put(&quot;parentId&quot;, &quot;in&quot;);
     * getSqlByObject(s2, &quot;vo&quot;, map, null);
     * 
     * 得到sql :  and vo.id = :id and vo.parentId in :parentId and vo.groupNm >= :startgroupNm and vo.groupNm <= :endgroupNm
     */
    public static String getSqlByObject(Object object, String tableAlias, Map<String, String> sqlTypeMap,
            Map<String, String> tableAliasMap) {
    	String alias = tableAlias;
        StringBuffer sql = new StringBuffer();
        Class clazz = object.getClass();
        Field[] thisfields = clazz.getDeclaredFields();
        Field[] Superfields = clazz.getSuperclass().getDeclaredFields();
        Field[] fields = (Field[]) ArrayUtils.addAll(thisfields, Superfields);
        for (int i = 0; i < fields.length; i++) {
            String fieldname = fields[i].getName();
            if (fieldname.equals("serialVersionUID")) {
                continue;
            }
            Object resultObject = invokeMethod(object, fields[i].getName());
            Class fieldType = fields[i].getType();
            if (resultObject != null) {
                if (tableAliasMap != null && tableAliasMap.size() > 0) {
                    alias = tableAliasMap.get(fieldname);
                    if (alias == null || alias.length() == 0) {
                    	alias = tableAlias;
                    }
                }
                if (sqlTypeMap == null || sqlTypeMap.size() == 0) {
                    sql.append(getSqlTypeByInstance(fieldType, fieldname, alias));
                } else {
                    String sqlType = sqlTypeMap.get(fieldname);
                    if (sqlType == null || sqlType.length() < 1) {
                        sql.append(getSqlTypeByInstance(fieldType, fieldname, alias));
                    } else {
                        sql.append(getSqlTypeByInstance(fieldname, alias, sqlType));
                    }
                }
            }
        }
        return sql.toString();
    }

    public static Object invokeMethod(Object owner, String fieldname) {
        Class ownerClass = owner.getClass();
        Method method = null;
        try {
            method = ownerClass.getMethod(toGetter(fieldname));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = method.invoke(owner);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * Get getter method name by field name
     * 
     * @param fieldname
     * @return
     */
    public static String toGetter(String fieldname) {

        if (fieldname == null || fieldname.length() == 0) {
            return null;
        }

        if (fieldname.length() > 2) {
            String second = fieldname.substring(1, 2);
            if (second.equals(second.toUpperCase())) {
                return new StringBuffer("get").append(fieldname).toString();
            }
        }

        fieldname = new StringBuffer("get").append(fieldname.substring(0, 1).toUpperCase())
                .append(fieldname.substring(1)).toString();
        return fieldname;
    }

    public static String getSqlTypeByInstance(Class value, String fieldname, String tableAlias) {
        if (value.equals(String.class)) {
            return " and " + tableAlias + "." + fieldname + " like " + ":" + fieldname;
        } else if (value.equals(Long.class) || value.equals(Number.class) || value.equals(Integer.class)
                || value.equals(Short.class) || value.equals(BigInteger.class) || value.equals(boolean.class)
                || value.equals(byte[].class) || value.equals(int.class) || value.equals(long.class)
                || value.equals(short.class) || value.equals(Boolean.class) || value.equals(Byte.class)) {
            return " and " + tableAlias + "." + fieldname + " = " + ":" + fieldname;
        } else if (value.isArray() || value.equals(List.class) || value.equals(Set.class) || value.equals(Map.class)) {
            return " and " + tableAlias + "." + fieldname + " in " + ":" + fieldname;
        } else if (value.equals(Date.class) || value.equals(Time.class) || value.equals(BigDecimal.class)
                || value.equals(Timestamp.class) || value.equals(Double.class) || value.equals(Float.class)
                || value.equals(double.class) || value.equals(float.class)) {
            return " and " + tableAlias + "." + fieldname + " >= :start" + fieldname + " and " + tableAlias + "."
                    + fieldname + " <= :end" + fieldname;
        } else {
            return " and " + tableAlias + "." + fieldname + " like " + ":" + fieldname;
        }
    }

    public static String getSqlTypeByInstance(String fieldname, String tableAlias, String sqlType) {
        if (sqlType.equals("between")) {
            return " and " + tableAlias + "." + fieldname + " >= :start" + fieldname + " and " + tableAlias + "."
                    + fieldname + " <= :end" + fieldname;
        } else {
            return " and " + tableAlias + "." + fieldname + " " + sqlType + " " + ":" + fieldname;
        }
    }

    /**
     * 根据page得到json
     * 
     * @param pagers
     * @param dtb
     * @return
     */
    public static <T> Map<Object, Object> getJsonByPagers(Page<T> pagers, DataTableBean dtb) {
        int draw = dtb.getDraw();
        long recordsTotal = pagers.getTotalCount();
        long recordsFiltered = recordsTotal;
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", pagers.getResult());
        info.put("recordsTotal", recordsTotal);
        info.put("recordsFiltered", recordsFiltered);
        info.put("draw", draw);
        return info;
    }

    public static <T> Map<Object, Object> getJsonByList(List<T> lst) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        info.put("recordsTotal", lst.size());
        info.put("recordsFiltered", lst.size());
        return info;
    }

    public static <T> Map<Object, Object> getJsonByPagers(FacetedPage apiInfo, DataTableBean dtb) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", apiInfo.getContent());
        info.put("recordsTotal", apiInfo.getTotalElements());
        info.put("recordsFiltered", apiInfo.getTotalElements());
        info.put("draw", dtb.getDraw());
        return info;
    }

    /**
     * 从request中取得检索条件，并做成Map<br>
     * 如果页面项目是s_开头的，去除s_字段
     * 
     * @param request
     * @return
     */
    public static HashMap<String, String> getSearchFormData(HttpServletRequest request) {
        String searchFormJsonStr = request.getParameter("searchFormStr");
        HashMap<String, String> retVal = new HashMap<String, String>();

        JSONArray array = (JSONArray) JSON.parse(searchFormJsonStr);
        for (int i = 0; array != null && i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            String name = obj.getString("name");
            if (name.startsWith("s_")) {
                name = name.substring(2);
            }
            retVal.put(name, obj.getString("value"));
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

    public static boolean isNumber(String str) {
        return match(str, "[0-9]*");
    }

    public static boolean match(String str, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher match = pattern.matcher(str.trim());
        return match.matches();
    }

    public static boolean isBigDecimal(String str) {
        java.util.regex.Matcher match = null;
        if (isNumber(str) == true) {
            Pattern pattern = Pattern.compile("[0-9]*");
            match = pattern.matcher(str.trim());
        } else {
            if (str.trim().indexOf(".") == -1) {
                Pattern pattern = Pattern.compile("^[+-]?[0-9]*");
                match = pattern.matcher(str.trim());
            } else {
                Pattern pattern = Pattern.compile("^[0-9]*[.]?[0-9]*");
                match = pattern.matcher(str.trim());
            }
        }
        return match.matches();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void saveLog(File file, String log) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            bw.write(log + "\n");
            bw.flush();
        } catch (FileNotFoundException e) {
            System.out.println("File not find. miniad:" + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static String getResult_json(String address) {
        String httpUrl = address;
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        URL url;
        BufferedReader bufferedReader = null;
        try {
            url = new URL(httpUrl);
            // 判断是http请求还是https请求
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);// 设置超时时间5秒
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("GET");// 设置请求类型为get
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            // 请求广告设备的原始地址
            urlConnection.connect();
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            out.flush();
            out.close();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() > 0) {
                    result.append(line.trim());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

}
