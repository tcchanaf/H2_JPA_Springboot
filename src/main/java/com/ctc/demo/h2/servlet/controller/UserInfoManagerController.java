package com.ctc.demo.h2.servlet.controller;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctc.demo.h2.dao.UserInfoDAO;
import com.ctc.demo.h2.dto.criteria.QueryCriteriaDTO;
import com.ctc.demo.h2.entity.UserInfo;
import com.ctc.h2.enumtype.Gender;

@RestController
public class UserInfoManagerController {

	@Autowired
	private UserInfoDAO userInfoDAO;

	@RequestMapping("/countUserByName/{name}")
	public String countUserByName(@PathVariable String name) {
		int count = userInfoDAO.countUserByName(name);

		return MessageFormat.format("countUserByName: {0}", count);
	}

	@RequestMapping("/findOldestUser")
	public String findOldestUser() {
		UserInfo users = userInfoDAO.findOldestUser();

		return MessageFormat.format("UserInfo: {0}", users);
	}

	@RequestMapping("/findLatestUser")
	public String findLatestUserJPQL() {
		UserInfo users = userInfoDAO.findLatestUserJPQL();

		return MessageFormat.format("UserInfo: {0}", users);
	}

	@RequestMapping("/findOneTopUser")
	public String findOneTopUser() {
		UserInfo users = userInfoDAO.findOneTopUser();

		return MessageFormat.format("UserInfo: {0}", users);
	}

	@RequestMapping("/findAllUser")
	public String findAllUser() {
		List<UserInfo> users = userInfoDAO.findAllUser();

		return MessageFormat.format("UserInfo: {0}", users);
	}

	@RequestMapping("/create/{name}")
	public String createUser(
			@PathVariable String name,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) Short age) {

		userInfoDAO.createUser(name, gender, age);

		return MessageFormat.format("Name: {0}, Gender: {1}, Age: {2}", name, gender, age);
	}


	@RequestMapping("/findUserByIdJPQL/{id}")
	public String findUserByIdJPQL(
			@PathVariable Long id) {
		UserInfo user = userInfoDAO.findUserByIdJPQL(id);

		return MessageFormat.format("UserInfo: {0}", user);
	}


	@RequestMapping("/findUserById/{id}")
	public String findById(
			@PathVariable Long id) {
		UserInfo user = userInfoDAO.findUserById(id);

		return MessageFormat.format("UserInfo: {0}", user);
	}


	@RequestMapping("/findUsersByGender/{gender}")
	public String findUsersByGender(
			@PathVariable String gender) {
		List<UserInfo> users = userInfoDAO.findUsersByGender(gender);

		return MessageFormat.format("UserInfo: {0}", users);
	}


	@RequestMapping("/findUsersByCriteria/")
	public String findUsersByCriteria(
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo) throws ParseException {
		QueryCriteriaDTO criteria = new QueryCriteriaDTO();

		if (StringUtils.hasText(userName))
			criteria.setUserName(userName);


		if (StringUtils.hasText(dateFrom)) {
			Date start = parseDateOrDatetime(dateFrom, false);
			criteria.setCreateDatetimeStart(start);
		}

		if (StringUtils.hasText(dateTo)) {
			Date end = parseDateOrDatetime(dateTo, true);
			criteria.setCreateDatetimeEnd(end);
		}

		System.out.println(MessageFormat.format("criteria: {0}", criteria));

		Page<UserInfo> res = userInfoDAO.findUserByCriteria(criteria, 0, 10);
		System.out.println(MessageFormat.format("res: {0}", res));
		List<UserInfo> users = new ArrayList<UserInfo>();
		System.out.println(MessageFormat.format("users: {0}", users));

		if (!res.isEmpty())
			users = res.getContent();

		return MessageFormat.format("UserInfo: {0}", users);
	}


	@RequestMapping("/findUserByTestSpecification")
	public String findUserByTestSpecification() {
		Page<UserInfo> res = userInfoDAO.findUserByTestSpecification(0, 10);

		List<UserInfo> users = new ArrayList<UserInfo>();
		if (!res.isEmpty())
			users = res.getContent();

		return MessageFormat.format("UserInfo: {0}", users);
	}

	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id) {

		boolean removed = userInfoDAO.deleteUser(id);

		return MessageFormat.format("deleted: {0}", removed);
	}

	@RequestMapping("/update/{id}")
	public String updateUser(@PathVariable Long id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) Short age) {

		UserInfo userInfo = userInfoDAO.findUserById(id);
		if (userInfo == null)
			return "User not found";

		if (name != null)
			userInfo.setUserName(name);
		if (gender != null)
			userInfo.setGender(Gender.parse(gender));
		if (age != null)
			userInfo.setAge(age);

		if (userInfoDAO.updateUser(userInfo))
			return "Updated user";
		else
			return "Failed to update user";
	}

	protected Date parseDateOrDatetime(String dateOrDatetime, boolean defaultDateEnd) throws ParseException {

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

		try {
			return defaultDateEnd ?
					sdf2.parse(dateOrDatetime + "235959") : sdf1.parse(dateOrDatetime);
		} catch (ParseException pe) {
			return sdf2.parse(dateOrDatetime);
		}

	}
}
