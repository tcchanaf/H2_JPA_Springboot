package com.ctc.demo.h2.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ctc.demo.h2.dao.UserInfoDAO;
import com.ctc.demo.h2.dto.criteria.QueryCriteriaDTO;
import com.ctc.demo.h2.entity.UserInfo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(transactionManager = "transactionManager", readOnly = true)
public interface UserInfoDAOImpl
		extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo>, UserInfoDAO {

	@Override
	public default int countUserByName(String userName) {
		return __countUserByName(userName);
	}

	@Query(value = "SELECT COUNT(u) FROM UserInfo u where u.userName = :userName")
	int __countUserByName(@Param("userName") String userName);

	@Query(value = "SELECT u FROM UserInfo u ORDER BY u.createDatetime ASC")
	List<UserInfo> findAllUserOrderByCreateDatetime();

	// Customized implmentation
	@Override
	public default UserInfo findOldestUser() {
		UserInfo resultEntity = null;
		Specification<UserInfo> specification = new Specification<UserInfo>() {
			@Override
			public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList();

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		Pageable pageable = PageRequest.of(0, 1, Sort.by("createDatetime").ascending());
		Page<UserInfo> results = this.findAll(specification, pageable);

		if (results.hasContent() && !results.getContent().isEmpty() )
			resultEntity = results.getContent().get(0);

		System.out.println(MessageFormat.format("findUserOldest result: {0}", resultEntity));

		return resultEntity;
	}

	@Override
	@Transactional(readOnly = false)
	public default UserInfo createUser(String userName, String gender, Short age) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userName);
		userInfo.setGender(gender);
		userInfo.setAge(age);
		userInfo.setCreateDatetime(new Date());
		this.save(userInfo);

		return userInfo;
	}

	@Override
	public default UserInfo findUserById(Long id) {
		Optional<UserInfo> user = this.findById(id);
		if (user.isPresent())
			return user.get();
		else
			return null;
	}

	@Override
	public default Page<UserInfo> findUserByCriteria(QueryCriteriaDTO criteria, int offset, int size) {
		System.out.println(MessageFormat.format("userName: {0}", criteria.getUserName()));

		Specification<UserInfo> specification = new Specification<UserInfo>() {
			@Override
			public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new LinkedList();
                // Search for Empty String (return nth if no record with empty string in this column)
                // predicates.add(cb.equal(root.get("userName"), ""));

                // Search for null String (UNKNOWN instances)
                // predicates.add(cb.equal(root.get("userName"), null));

				if (StringUtils.hasText(criteria.getUserName()))
					predicates.add(cb.equal(root.get("userName"), criteria.getUserName()));

				if (criteria.getCreateDatetimeStart() != null)
					predicates.add(
							cb.greaterThan(root.get("createDatetime"), criteria.getCreateDatetimeStart()));
				if (criteria.getCreateDatetimeEnd() != null)
					predicates.add(
							cb.lessThan(root.get("createDatetime"), criteria.getCreateDatetimeEnd()));

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		Pageable pageable = PageRequest.of(offset, size);

		return this.findAll(specification, pageable);
	}

	@Override
	public default Page<UserInfo> findUserByTestSpecification(int offset, int size) {
		Specification<UserInfo> specification = new Specification<UserInfo>() {
			@Override
			public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new LinkedList();
				// apple and 35
				Predicate predicate1 = cb.and(cb.equal(root.get("userName"), "apple"), cb.equal(root.get("age"), 35));
				// (apple and 35) or boy
				Predicate predicate2 = cb.or(predicate1, cb.equal(root.get("gender"), "boy"));

				return predicate2;
			}
		};
		Pageable pageable = PageRequest.of(offset, size);

		return this.findAll(specification, pageable);
	}


	@Override
	@Transactional(readOnly = false)
	public default boolean updateUser(UserInfo userInfo){
		this.save(userInfo);

		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public default boolean deleteUser(Long id) {
		// error if does not exist
		UserInfo userInfo = findUserById(id);
		if (userInfo != null) {
			this.deleteById(id);

			return true;
		} else
			return false;
	}
}
