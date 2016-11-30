package com.yhsjedu.datacloud.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yhsjedu.datacloud.dao.BaseDao;

/**
 * Service处理底层
 * 
 * @author Guoxk
 */
@Service
@Transactional(readOnly = true)
public abstract class BaseService<T, PK extends Serializable> {

    abstract protected BaseDao<T, PK> getDao();

    abstract protected Class<T> getTClass();

    @Transactional(readOnly = true)
    public T getByPk(final PK id) {
        return getDao().get(id);
    }

    /**
     * 保存实体
     */
    @Transactional(readOnly = false)
    public T save(T entity) {
        return getDao().save(entity);
    }

    /**
     * 删除实体
     * 
     * @param id
     */
    @Transactional(readOnly = false)
    final public void delete(final PK id) {
        getDao().delete(id);
    }

    @Transactional(readOnly = false)
    final public void delete(Object entity) {
        getDao().delete(entity);
    }

    /**
     * 按照实例查找
     * 
     * @param beanClass
     * @param entity
     * @return
     */
    final public List<T> queryExample(T entity) {
        return getDao().queryExample(getTClass(), entity);
    }

    public List<T> query(Order... orders) {
        return getDao().query(orders);
    }

    public List<T> query(Criterion... criterions) {
        return getDao().query(criterions);
    }

    public List<T> query(Order[] orders, Object... properties) {
        return getDao().query(orders, properties);
    }

    public List<T> query(Criterion[] criterions, Order[] orders) {
        return getDao().query(criterions, orders);
    }

    public List<T> query(Object... properties) {
        return getDao().query(properties);
    }

}
