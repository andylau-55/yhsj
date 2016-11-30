package com.yhsjedu.datacloud.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.yhsjedu.datacloud.core.dao.ReplaceDao;
import com.yhsjedu.datacloud.core.entity.Replace;
import com.yhsjedu.datacloud.dao.Page;
import com.yhsjedu.datacloud.utils.CommonUtil;
import com.yhsjedu.datacloud.utils.DataTableBean;

@Service
@Transactional(readOnly = true)
public class ReplaceService {

	
	@Autowired
	private ReplaceDao deplaceDao;
	
	public Replace getReplaceById(Long id){
		return deplaceDao.get(id);
	}
	
	public List<Replace> getAllReplaces(){
		return deplaceDao.getAll();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Replace saveReplace(Replace replace){
		return deplaceDao.save(replace);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteReplace(Replace replace){
		deplaceDao.delete(replace);
	}
	
	public Page<Replace> getReplacePageList(DataTableBean dtb,Replace replace){
		Page<Replace> page = new Page<Replace>(dtb);
		StringBuffer sql = new StringBuffer();
		String querySql = CommonUtil.getSqlByObject(replace, "r", null, null);
		sql.append("select * from replacePage as r where 1=1 ");
		sql.append(querySql + dtb.getOrderSql());
		return deplaceDao.getPageBySql(page,Replace.class, sql.toString(),replace);
	}
	
	public List<Replace> getReplaceList(Replace replace){
		StringBuffer sql = new StringBuffer();
		String querySql = CommonUtil.getSqlByObject(replace, "r", null, null);
		sql.append("select * from replacePage as r where 1=1 ");
		sql.append(querySql);
		sql.append(" ORDER BY r.type ASC,r.pageOrder asc");
		return deplaceDao.sqlQuery(Replace.class, sql.toString(),replace);
	}
	
	@Transactional(readOnly = false)
    public int updateReplaceState(Replace replace) {
        String sql = "update replacePage set state=:state where id=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", replace.getState());
        params.put("id", replace.getId());
        return deplaceDao.sqlUpdate(sql, params);
    }
}
