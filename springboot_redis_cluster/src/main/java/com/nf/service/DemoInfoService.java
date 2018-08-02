package com.nf.service;

import java.util.List;

import com.nf.controller.User;

public interface DemoInfoService {
	
	public User findById(String id);
    
    public void deleteFromCache(String id);

    void test();
    
    public List<User> findAll(String id) ;
    
    public User updateUser(String id);
}
