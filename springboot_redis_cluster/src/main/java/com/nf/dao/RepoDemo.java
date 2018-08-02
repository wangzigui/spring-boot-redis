package com.nf.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nf.controller.User;

@CacheConfig(cacheNames = "users1")
public interface RepoDemo extends JpaRepository<User, String>{

	User findOne(String id);

	@CachePut(key="#p0")
	@Transactional
	@Modifying
    @Query("UPDATE User xe SET xe.username= :username WHERE xe.id= :id")
    void  updateById(@Param(value = "id") String id,@Param(value = "username") String username);
}
