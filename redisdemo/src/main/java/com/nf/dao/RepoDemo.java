package com.nf.dao;

import org.springframework.data.repository.CrudRepository;

import com.nf.controller.User;

public interface RepoDemo extends CrudRepository<User,String>{

	User findOne(String id);

}
