package com.yhsjedu.datacloud.dao.hibernate;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import com.yhsjedu.datacloud.dao.Page;

@Repository("hibernateDao")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class HibernateDao {

    static final Logger logger = LoggerFactory.getLogger(HibernateDao.class);

    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    /**
     * 获取SessionFactory
     * 
     * @return
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 设置SessionFactory
     * 
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取当前session
     * 
     * @return
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public <T, K extends Serializable> T get(Class<T> entityClass, K id) {
        entityClass = getEntityClass(entityClass);
        return (T) getSession().get(entityClass, id);
    }

    public <T> List<T> getAll(Class<T> entityClass) {
        entityClass = getEntityClass(entityClass);
        return createCriteria(entityClass).list();
    }

    public <T, K extends Serializable> List<T> getByIds(Class<T> entityClass, List<K> ids) {
        entityClass = getEntityClass(entityClass);
        String idName = getClassMetadata(entityClass).getIdentifierPropertyName();
        return createCriteria(entityClass).add(Restrictions.in(idName, ids)).list();
    }

    public <T> List<T> getByProperties(Class<T> entityClass, Map<String, Object> properties) {
        entityClass = getEntityClass(entityClass);
        return createCriteria(entityClass).add(Restrictions.allEq(properties)).list();
    }

    public <T> List<T> query(Class<T> entityClass, Criterion[] criterions, Order[] orders) {
        entityClass = getEntityClass(entityClass);
        Criteria criteria = createCriteria(entityClass);

        if (criterions != null && criterions.length > 0) {
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }
        }

        if (orders != null) {
            for (Order order : orders) {
                criteria.addOrder(order);
            }
        }

        return criteria.list();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected void delete(Object entity) {
        getSession().delete(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected <K extends Serializable> void delete(Class<?> entityClass, K id) {
        delete(getClassMetadata(entityClass).instantiate(id, null));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected <T> T save(T entity) {
        return (T) getSession().merge(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected <T> void save(List<T> entities) {
        StatelessSession session = sessionFactory.openStatelessSession();
        for (T entity : entities) {
            session.update(entity);
        }
        session.close();
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */

    public List<Map<String, ?>> sqlQuery(String sql, Object... parameters) {
        return createSQLQuery(sql, parameters).setResultTransformer(new MapResultTransFormer()).list();
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */

    public <T> T sqlUniqueQuery(Class<T> beanClass, String sql, Object... parameters) {
        return (T) setQueryType(createSQLQuery(sql, parameters), beanClass).uniqueResult();
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */

    public <T> List<T> sqlQuery(Class<T> beanClass, String sql, Object... parameters) {
        return setQueryType(createSQLQuery(sql, parameters), beanClass).list();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int sqlUpdate(String sql, Object... parameters) {
        int rt = createSQLQuery(sql, parameters).executeUpdate();
        return rt;
    }

    public <T> List<T> queryExample(Class<T> beanClass, T entity) {
        Example example = Example.create(entity);
        Criteria criteria = getSession().createCriteria(getEntityClass(beanClass)).add(example);
        return criteria.list();
    }

    /**
     * sql查询
     * 
     * @param <T>
     * @param beanClass
     * @param sql
     * @param isNorm 是否标准模式，当false类型beanClass为非托管实体类
     * @param parameters
     * @return
     */
    public <T> List<T> sqlQuery(Class<T> beanClass, String sql, Boolean isNorm, Object... parameters) {
        if (!isNorm) {
            return createSQLQuery(sql, parameters).setResultTransformer(BeanResultTransFormer.get(beanClass)).list();
        } else {
            return sqlQuery(beanClass, sql, parameters);
        }
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */

    public List<Map<String, ?>> sqlPageQuery(String sql, int pageNo, int pageSize, Object... parameters) {
        Query query = createSQLQuery(sql, parameters).setResultTransformer(new MapResultTransFormer());
        return pageQuery(query, pageNo, pageSize);
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */

    public <T> List<T> sqlPageQuery(Class<T> beanClass, String sql, int pageNo, int pageSize, Object... parameters) {
        return pageQuery(setQueryType(createSQLQuery(sql, parameters), beanClass), pageNo, pageSize);
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */
    public Page<Map<String, ?>> sqlPageQuery(Page page, String sql, Object... parameters) {
        page.setResult(sqlPageQuery(sql, page.getPageNo(), page.getPageSize(), parameters));
        return page;
    }

    /**
     * @see #createSQLQuery(String, Object...)
     */
    public <T> Page<T> sqlPageQuery(Page<T> page, Class<T> beanClass, String sql, Object... parameters) {
        page.setResult(sqlPageQuery(beanClass, sql, page.getPageNo(), page.getPageSize(), parameters));
        return page;
    }

    /**
     * 执行HQL查询,返回单个实体<br/>
     * 
     * @param hql
     * @param parameters
     * @return
     * @see #createQuery(String, Object...)
     */
    public <T> T hqlUniqueQuery(String hql, Object... parameters) {
        return (T) createQuery(hql, parameters).uniqueResult();
    }

    /**
     * 执行HQL查询<br/>
     * 
     * @param hql
     * @param parameters
     * @return
     * @see #createQuery(String, Object...)
     */
    public List hqlQuery(String hql, Object... parameters) {
        return createQuery(hql, parameters).list();
    }

    /**
     * 执行HQL分页查询<br/>
     * 
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param parameters
     * @return
     * @see #createQuery(String, Object...)
     */
    public List hqlPageQuery(String hql, int pageNo, int pageSize, Object... parameters) {
        return pageQuery(createQuery(hql, parameters), pageNo, pageSize);
    }

    /**
     * @see #createQuery(String, Object...)
     */
    public Page hqlPageQuery(Page page, String hql, Object... parameters) {
        page.setResult(hqlPageQuery(hql, page.getPageNo(), page.getPageSize(), parameters));
        return page;
    }

    /**
     * 分页查询
     * 
     * @param query
     * @param pageNo 页码
     * @param pageSize 每页数据大小
     * @return
     */
    public static List pageQuery(Query query, int pageNo, int pageSize) {
        return query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
    }

    /**
     * 分页查询多查询一条
     * 
     * @param query
     * @param pageNo 页码
     * @param pageSize 每页数据大小
     * @return
     */
    public static List pageQueryAutoMoreOne(Query query, int pageNo, int pageSize) {
        return query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize + 1).list();
    }

    /**
     * 分页查询
     * 
     * @param query
     * @return
     */
    public static Page pageQuery(Query query, Page page) {
        if (page.isAutoMoreOne()) {
            page.setResult(pageQueryAutoMoreOne(query, page.getPageNo(), page.getPageSize()));
        } else {
            page.setResult(pageQuery(query, page.getPageNo(), page.getPageSize()));
        }
        return page;
    }

    /**
     * 创建SQLQuery对象,并设置参数<br/>
     * 参数值可以是任意类型<br/>
     * 针对不同对象,处理情况不同,具体如下:
     * <ul>
     * <li>简单类型,包括基本类型以及String,Date,Number...,根据其索引位置为JPA参数 ?n 赋值</li>
     * <li>Map对象,根据key值将对应的value赋值给同名参数</li>
     * <li>POJO对象,根据属性名称进行同名参数赋值</li> 对于简单类型的定义由实现类决定,以上所有类型均支持in操作
     * </ul>
     * 示例:
     * 
     * <pre>
     * &lt;code&gt;Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
     * map.put(&quot;idList&quot;, new Long[]{1L, 2L, 3L});
     * map.put(&quot;title&quot;, &quot;%测试2%&quot;);
     * EventAlias event = new EventAlias();
     * event.setTitle(&quot;%测试%&quot;);
     * sql = &quot;select e1.event_id ID,e1.event_date Date,e1.title,e1.type from event e1 where e1.title like :title or e1.title like ?1 and e1.event_id in :idList&quot;;
     * list = hibernateDao.sqlQuery(EventAlias.class, sql,event, &quot;%中国%&quot;, map);&lt;/code&gt;
     * </pre>
     * 
     * @param sql
     * @param parameters
     * @return
     */
    public SQLQuery createSQLQuery(String sql, Object... parameters) {
        return setParameters(getSession().createSQLQuery(sql), parameters);
    }

    /**
     * 创建Query对象,并设置参数<br/>
     * 参数值可以是任意类型<br/>
     * 针对不同对象,处理情况不同,具体如下:
     * <ul>
     * <li>简单类型,包括基本类型以及String,Date,Number...,根据其索引位置为JPA参数 ?n 赋值</li>
     * <li>Map对象,根据key值将对应的value赋值给同名参数</li>
     * <li>POJO对象,根据属性名称进行同名参数赋值</li> 对于简单类型的定义由实现类决定,以上所有类型均支持in操作<br/>
     * 此外,如果SQL(HQL)语句中只包含JDBC占位符,支持按索引位置赋值,但不支持in操作
     * </ul>
     * 示例:
     * 
     * <pre>
     * &lt;code&gt;Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
     * map.put(&quot;idList&quot;, new Long[]{1L, 2L, 3L});
     * map.put(&quot;title&quot;, &quot;%测试2%&quot;);
     * EventAlias event = new EventAlias();
     * event.setTitle(&quot;%测试%&quot;);
     * sql = &quot;select e1.event_id ID,e1.event_date Date,e1.title,e1.type from event e1 where e1.title like :title or e1.title like ?1 and e1.event_id in :idList&quot;;
     * list = hibernateDao.sqlQuery(EventAlias.class, sql,event, &quot;%中国%&quot;, map);&lt;/code&gt;
     * </pre>
     * 
     * @param hql
     * @param parameters
     * @return
     */
    public Query createQuery(String hql, Object... parameters) {
        return setParameters(getSession().createQuery(hql), parameters);
    }

    /**
     * 根据实体类型创建Criteria
     * 
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> Criteria createCriteria(Class<T> entityClass) {
        return getSession().createCriteria(getEntityClass(entityClass));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void executeBatch(final String[] sqlList) {
        getSession().doWork(new Work() {

            public void execute(Connection connection) throws SQLException {
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();
                for (String sql : sqlList) {
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                connection.commit();
            }
        });
    }

    @Transactional(readOnly = false)
    public void executeBatch(final String sql, final Object[]... parameters) {
        getSession().doWork(new Work() {

            public void execute(Connection connection) throws SQLException {
                connection.setAutoCommit(false);
                PreparedStatement stmt = connection.prepareStatement(sql);
                for (Object[] arr : parameters) {
                    int i = 1;
                    for (Object p : arr) {
                        stmt.setObject(i++, p);
                    }
                    stmt.addBatch();
                }
                stmt.executeBatch();
                connection.commit();
            }
        });
    }

    /**
     * 设置Query对象参数 参数值可以是任意类型<br/>
     * 针对不同对象,处理情况不同,具体如下:
     * <ul>
     * <li>简单类型,包括基本类型以及String,Date,Number...,根据其索引位置为JPA参数 ?n 赋值</li>
     * <li>Map对象,根据key值将对应的value赋值给同名参数</li>
     * <li>POJO对象,根据属性名称进行同名参数赋值</li> 对于简单类型的定义由实现类决定,以上所有类型均支持in操作
     * </ul>
     * 示例:
     * 
     * <pre>
     * &lt;code&gt;Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
     * map.put(&quot;idList&quot;, new Long[]{1L, 2L, 3L});
     * map.put(&quot;title&quot;, &quot;%测试2%&quot;);
     * EventAlias event = new EventAlias();
     * event.setTitle(&quot;%测试%&quot;);
     * sql = &quot;select e1.event_id ID,e1.event_date Date,e1.title,e1.type from event e1 where e1.title like :title or e1.title like ?1 and e1.event_id in :idList&quot;;
     * list = hibernateDao.sqlQuery(EventAlias.class, sql,event, &quot;%中国%&quot;, map);&lt;/code&gt;
     * </pre>
     * 
     * 另外,如果SQL(HQL)语句中只包含JDBC占位符的话,支持按索引位置赋值,但不支持in操作
     * 
     * @param query
     * @param parameters
     * @param <T>
     * @return
     */
    private static <T extends Query> T setParameters(T query, Object... parameters) {
        if (query.getNamedParameters().length == 0) {
            for (int i = 0; i < parameters.length; i++) {
                if (isSimple(parameters[i])) {
                    query.setParameter(i, parameters[i]);
                }
            }
            return query;
        }
        Map<String, Object> jpaMap = new HashMap<String, Object>();
        for (int i = 0; i < parameters.length; i++) {
            Object param = parameters[i];
            if (param == null || param instanceof Collection || isSimple(param) || param.getClass().isArray()) {
                jpaMap.put(String.valueOf(i), handle(param));
            } else if (param instanceof Map) {
                query.setProperties(namedParameters((Map) param));
            } else {
                query.setProperties(param);
            }
        }
        query.setProperties(jpaMap);
        return query;
    }

    /**
     * 设置Query对象类型
     * 
     * @param query
     * @param beanClass
     * @return
     */
    private SQLQuery setQueryType(SQLQuery query, Class beanClass) {
        if (getClassMetadata(beanClass) != null) {
            query.addEntity(beanClass);
        } else {
            query.setResultTransformer(BeanResultTransFormer.get(beanClass));
        }
        return query;
    }

    /**
     * 将数组的索引作为key,元素作为value,构建Map对象以支持JPA参数
     * 
     * @param parameters
     * @return
     */
    public static Map jpaParameters(Object... parameters) {
        Map<String, Object> map = new HashMap<String, Object>();
        int i = 0;
        for (Object parameter : parameters) {
            map.put(String.valueOf(i++), handle(parameter));
        }
        return map;
    }

    /**
     * 将Map中的primitive类型数组转换为对象数组,以支持In操作
     * 
     * @param map
     * @return
     */
    public static Map namedParameters(Map map) {
        for (Object o : map.entrySet()) {
            Entry en = (Entry) o;
            en.setValue(handle(en.getValue()));
        }
        return map;
    }

    /**
     * 获取实体对象的真实类型,避免Spring代理对象
     * 
     * @param entity
     * @return
     */
    private static Class getEntityClass(Object entity) {
        if (entity instanceof Class) {
            return ClassUtils.getUserClass((Class) entity);
        }
        return ClassUtils.getUserClass(entity);
    }

    /**
     * @param entityClass 实体类型
     * @return
     */
    private ClassMetadata getClassMetadata(Class entityClass) {
        return getSessionFactory().getClassMetadata(entityClass);
    }

    private static Object handle(Object value) {
        if (value != null && value.getClass().isArray()) {
            return ObjectUtils.toObjectArray(value);
        }
        return value;
    }

    private static boolean isSimple(Object value) {
        Class<? extends Object> type = value.getClass();
        if (type.isArray() || simpleTypes.contains(type))
            return true;
        for (Class clazz : simpleTypes) {
            if (clazz.isInstance(value))
                return true;
        }
        return false;
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    static final Set<Class> simpleTypes = new HashSet<Class>();

    {
        // primitive和包装类
        simpleTypes.add(boolean.class);
        simpleTypes.add(byte.class);
        simpleTypes.add(byte[].class);
        simpleTypes.add(double.class);
        simpleTypes.add(float.class);
        simpleTypes.add(int.class);
        simpleTypes.add(long.class);
        simpleTypes.add(short.class);
        simpleTypes.add(Boolean.class);
        simpleTypes.add(Byte.class);
        simpleTypes.add(Double.class);
        simpleTypes.add(Float.class);
        simpleTypes.add(Integer.class);
        simpleTypes.add(Long.class);
        simpleTypes.add(Short.class);

        // 常用简单类型
        simpleTypes.add(String.class);
        simpleTypes.add(BigDecimal.class);
        simpleTypes.add(BigInteger.class);
        simpleTypes.add(Number.class);
        simpleTypes.add(Date.class);
        simpleTypes.add(Time.class);
        simpleTypes.add(Timestamp.class);

        // 数据对象类型
        simpleTypes.add(Blob.class);
        simpleTypes.add(Clob.class);
        simpleTypes.add(InputStream.class);
        simpleTypes.add(Reader.class);
        simpleTypes.add(Ref.class);
        simpleTypes.add(SQLXML.class);
        simpleTypes.add(URL.class);

        // 类类型
        simpleTypes.add(Class.class);
    }

}
