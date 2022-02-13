package com.ctc.demo.h2.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ctc.demo.h2.dto.criteria.QueryCriteriaDTO;
import com.ctc.demo.h2.entity.UserInfo;

public interface UserInfoDAO {

	// JPQL use entity class name as table name
	public int countUserByName(@Param("userName") String userName);

	@Query(value = "SELECT u FROM UserInfo u")
	public List<UserInfo> findAllUser();

	// Native Query
	@Query(value = "SELECT * FROM USER_INFO u LIMIT 1", nativeQuery=true)
	public UserInfo findOneTopUser();

	@Query(value = "SELECT * FROM USER_INFO u ORDER BY CREATE_DATETIME DESC LIMIT 1", nativeQuery=true)// No limit in JPQL
	public UserInfo findLatestUserJPQL();

	@Query(value = "SELECT * FROM USER_INFO u where u.gender = :gender", nativeQuery = true)
	public List<UserInfo> findUsersByGender(String gender);

	@Query(value = "SELECT * FROM USER_INFO u where u.id = :id", nativeQuery = true)
	public UserInfo findUserByIdJPQL(@Param("id")Long id);

	public UserInfo findOldestUser();

	public UserInfo createUser(String userName, String gender, Short age);

	public UserInfo findUserById(Long id);

	public Page<UserInfo> findUserByCriteria(QueryCriteriaDTO criteria, int offset, int size);

	public Page<UserInfo> findUserByTestSpecification(int offset, int size);

	public boolean updateUser(UserInfo userInfo);

	public boolean deleteUser(Long id);

}
