package com.yhsjedu.datacloud.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yhsjedu.datacloud.dao.hibernate.BeanResultTransFormer;
import com.yhsjedu.datacloud.dao.hibernate.HibernateDao;
import com.yhsjedu.datacloud.utils.ReflectUitls;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public class BaseDao<T, PK extends Serializable> extends HibernateDao {

    /**
     * 当前实体对应的泛型类
     */
    protected Class<T> entityClass = null;

    public BaseDao() {
        entityClass = (Class<T>) ReflectUitls.getGenericType(this.getClass());
    }

    /**
     * 获取实体
     * 
     * @param id
     * @return
     */
    public T get(final PK id) {
        return this.get(entityClass, id);
    }

    /**
     * 获取全部实体列表
     * 
     * @return
     */
    public List<T> getAll() {
        return this.getAll(entityClass);
    }

    /**
     * 保存实体
     */
    @SuppressWarnings({ "hiding" })
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T> T save(T entity) {
        return (T) super.save(entity);
    }

    /**
     * 删除实体
     * 
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(final PK id) {
        this.delete(entityClass, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Object entity) {
        super.delete(entity);
    }

    /**
     * 根据sql获取总数
     * 
     * @param sql 需要保证sql为查询总数的语句
     * @return
     */
    public Long getCountBySql(String sql, Object... params) {
        Object result = createSQLQuery(sql, params).uniqueResult();
        if (result != null) {
            return Long.valueOf(result.toString());
        }
        return null;
    }

    /**
     * 获取分页
     * 
     * @param pageNo
     * @param pagesize
     * @param hql
     * @param objs
     * @return
     */
    public Page<T> getPageByHql(Integer pageNo, Integer pagesize, String hql, Object... objs) {
        List<T> list = super.hqlPageQuery(hql, pageNo, pagesize, objs);
        long count = this.getCountByHql(hql, objs);// 总记录数
        Page<T> pageBean = new Page<T>(pageNo, pagesize);
        pageBean.setResult(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }

    public Page<T> getPageBySql(Integer pageNo, Integer pagesize, String sql) {
        Page<T> page = new Page<T>(pageNo, pagesize);
        return getPageBySql(page, entityClass, sql);
    }

    /**
     * 获取分页结果,
     * 
     * @param beanClass 支持任意Bean，按结果集映射
     * @return
     */
    public <E> Page<E> getPageBySql(Class<E> beanClass, Integer pageNo, Integer pagesize, String sql) {
        Page<E> page = new Page<E>(pageNo, pagesize);
        return getPageBySql(page, beanClass, sql);
    }

    /**
     * 获取分页结果,
     * 
     * @param beanClass 支持任意Bean，按结果集映射
     * @return
     */
    public <E> Page<E> getPageBySql(Page<E> page, Class<E> beanClass, String sql, Object... parameters) {
        Query query = createSQLQuery(sql, parameters).setResultTransformer(BeanResultTransFormer.get(beanClass));
        pageQuery(query, page);
        if (page.isAutoCount()) {
            // 总记录数
            if (sql.toLowerCase().contains("group")) {
                sql = preparedGroupCount(sql);
            } else {
                sql = preparedCount(sql);
            }
            Long count = this.getCountBySql(sql, parameters);
            if (count == null) {
                count = 0L;
            }
            page.setTotalCount(count);
        }

        return page;
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数
     * 
     * @param hql
     * @param values
     * @return
     */
    public Long getCountByHql(String hql, final Object... values) {
        hql = preparedCount(hql);
        Object result = createQuery(hql, values).uniqueResult();
        if (result != null) {
            return Long.valueOf(result.toString());
        }
        return null;

    }

    private static String preparedCount(String sql) {

        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(sql);

        String tmpSql = sql.toLowerCase();
        while (matcher.find()) {
            String strFinded = matcher.group(1);
            String strReplace = strFinded.replace("from", "####");
            tmpSql = tmpSql.replace(strFinded, strReplace);
        }
        int startIndex = tmpSql.indexOf("select");
        int endIndex = tmpSql.indexOf("from");
        String repaceSql = sql.substring(startIndex + 6, endIndex);
        sql = sql.replace(repaceSql, " count(*) as total ");
        return sql;
    }

    private static String preparedGroupCount(String sql) {
        sql = "select count(*) as total from (" + sql + ") as con";
        return sql;
    }

    public List<T> query(Order[] orders) {
        return super.query(entityClass, null, orders);
    }

    public List<T> query(Criterion[] criterions) {
        return super.query(entityClass, criterions, null);
    }

    public List<T> query(Order[] orders, Object... properties) {
        if (properties != null && properties.length > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < properties.length; i = i + 2) {
                map.put(properties[i].toString(), properties[i + 1]);
            }
            return super.query(entityClass, new Criterion[] { Restrictions.allEq(map) }, orders);
        }

        return super.query(entityClass, null, orders);
    }

    public List<T> query(Criterion[] criterions, Order[] orders) {
        return super.query(entityClass, criterions, orders);
    }

    public List<T> query(Object... properties) {
        return query(null, properties);
    }

    /**
     * 执行非查询sql
     * 
     * @param sql
     */
    public int executeSQL(String sql) {
        SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return query.executeUpdate();
    }
}
