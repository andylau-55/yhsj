package com.yhsjedu.datacloud.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yhsjedu.datacloud.core.entity.Replace;
import com.yhsjedu.datacloud.dao.BaseDao;


@Repository
@Transactional(readOnly = true)
public class ReplaceDao extends BaseDao<Replace, Long> {

	
}
