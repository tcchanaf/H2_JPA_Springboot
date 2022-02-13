package com.ctc.demo.h2.servlet.controller;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctc.demo.h2.dao.SystemConfigDAO;
import com.ctc.demo.h2.entity.SystemConfig;

@RestController
public class SystemConfigController {

		@Autowired
		private SystemConfigDAO systemConfigDAO;

		@RequestMapping("/initKeyIfNotExist/{key}")
		public String initKeyIfNotExist(
				@PathVariable String key,
				@RequestParam(required = false) String value) {
			boolean result = systemConfigDAO.initKeyIfNotExist(key, value);

			return MessageFormat.format("Result of init key({0}): {1}", key, result);
		}

		@RequestMapping("/gainLock/{key}")
		public String gainLock(
				@PathVariable String key) {
			boolean result = systemConfigDAO.gainLock(key);

			return MessageFormat.format("Result of gaining lock({0}): {1}", key, result);
		}

		@RequestMapping("/releaseLock/{key}")
		public String releaseLock(
				@PathVariable String key) {
			boolean result = systemConfigDAO.releaseLock(key);

			return MessageFormat.format("Result of releasing lock({0}): {1}", key, result);
		}

		@RequestMapping("/saveEntity/{key}")
		public String saveEntity(
				@PathVariable String key,
				@RequestParam(required = false) String value) {
			SystemConfig systemConfig = new SystemConfig();
			systemConfig.setKey(key);
			systemConfig.setValue(value);
			boolean result = systemConfigDAO.saveEntity(systemConfig);

			return MessageFormat.format("Result of saveEntity key({0}): {1}", key, result);
		}


		@RequestMapping("/find/{key}")
		public String find(
				@PathVariable String key) {
			SystemConfig systemConfig = systemConfigDAO.find(key);

			return MessageFormat.format("Result of find key:({0}): {1}", key, systemConfig);
		}

		@RequestMapping("/remove/{key}")
		public String remove(
				@PathVariable String key) {
			SystemConfig systemConfig = systemConfigDAO.find(key);

			if (systemConfig != null)
				systemConfigDAO.remove(systemConfig);

			return MessageFormat.format("Result of find key:({0}): {1}", key, systemConfig);
		}
}
