package com.ctc.demo.h2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ctc.demo.h2.entity.SystemConfig;

public interface SystemConfigDAO {

	public static final String TRUE="true";
	public static final String FALSE="false";

	public SystemConfig find(String key);

	public boolean remove(SystemConfig entity);

	public boolean saveEntity(SystemConfig entity);

	@Query(value = "SELECT s FROM SystemConfig s")
	public List<SystemConfig> findAll();

	// DB Locking for request key
	//TODO TC test
	public boolean gainLock(@Param("key") String key);

	// release DB Lock for request key
	public boolean releaseLock(String key);

	public boolean initKeyIfNotExist(String key, String value);
}