package com.ctc.demo.h2.dao.impl;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ctc.demo.h2.dao.SystemConfigDAO;
import com.ctc.demo.h2.entity.SystemConfig;

@Component
@Transactional(transactionManager="transactionManager", readOnly=true)
public interface SystemConfigDAOImpl extends JpaRepository<SystemConfig, String>, SystemConfigDAO {

	@Override
	@Transactional(readOnly = false)
	public default boolean initKeyIfNotExist(String key, String value) {
		System.out.println(MessageFormat.format("key:{0}, value: {1}",key, value));

		if (!StringUtils.hasText(key)) {
			System.out.println("initKeyIfNotExist() error. 'key' should not be blank");
			return false;
		}
		int count = countByKey(key);
		if (count == 0) {
			// create key
			SystemConfig config = new SystemConfig();
			config.setKey(key);
			config.setValue(value);
			try {
				this.save(config);
				System.out.println(MessageFormat.format("initKeyIfNotExist() success. 'key'({0}) initialized with value ({1}).",key, value));

				return true;
			} catch (Throwable t) {
				System.out.println(MessageFormat.format("initKeyIfNotExist() failed. 'key'({0}) cannot be initialized with value ({1}) caused by {2}.",key, value, t.getMessage()));

				return false;
			}
		} else if (count > 0) {
			// multiple key record exist warning
			System.out.println(MessageFormat.format("initKeyIfNotExist() error. More than one 'key'({0}) exists in SystemConfig table.  At most one record is expected",key));

			return false;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public default boolean gainLock(String key) {
		SystemConfig config = this.find(key);
		if (config != null && config.getValue() == FALSE) {
			config.setValue(TRUE);
			this.save(config);

			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public default boolean releaseLock(String key) {
		SystemConfig config = this.find(key);
		if (config != null) {
			config.setValue(FALSE);
			this.save(config);

			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	default boolean remove(SystemConfig entity) {
		if (entity == null || !StringUtils.hasText(entity.getKey()))
			return false;

		// error if does not exist
		SystemConfig systemConfig = this.find(entity.getKey());
		if (systemConfig != null) {
			this.deleteById(entity.getKey());

			return true;
		} else
			return false;
	}


	@Override
	default SystemConfig find(String key) {
		if (!StringUtils.hasText(key))
			return null;
		Optional<SystemConfig> optionalConfig = this.findById(key);
		if (optionalConfig.isPresent())
			return optionalConfig.get();
		else
			return null;
	}

	@Override
	@Transactional(readOnly = false)
	default boolean saveEntity(SystemConfig entity) {
		if (entity == null || !StringUtils.hasText(entity.getKey()))
			return false;

		this.save(entity);

		return true;
	}


	@Query(value = "SELECT COUNT(s) FROM SystemConfig s where s.key = :key")
	int countByKey(@Param("key") String key);
}
