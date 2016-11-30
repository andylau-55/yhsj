package com.yhsjedu.web.agent.controller.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yhsjedu.datacloud.dao.Page;
import com.yhsjedu.datacloud.utils.DataTableBean;

public class Common {
	/**
	 * 根据开始时间和结束时间返回时间段内的时间集合
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return List
	 * @throws ParseException
	 */
	public static String[] getDatesBetweenTwoDate(String startDate,
			String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf.parse(startDate);
		Date end = sdf.parse(endDate);
		List<String> lDate = new ArrayList<String>();
		lDate.add(sdf.format(start));// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(start);
		boolean bContinue = true;
		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (end.after(cal.getTime())) {
				lDate.add(sdf.format(cal.getTime()));
			} else {
				break;
			}
		}
		if (!start.equals(end)) {
			lDate.add(sdf.format(end));// 把结束时间加入集合
		}
		
		String dates[] = new String[lDate.size()];
		for (int i = 0; i < lDate.size(); i++) {
			dates[i] = lDate.get(i);
		}
		return dates;
	}

	/**根据page得到json
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
	
	/**
     *  查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public static String beforLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return sdf.format(date);
    }
}
