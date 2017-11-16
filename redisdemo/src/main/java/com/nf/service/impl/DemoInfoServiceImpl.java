package com.nf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.nf.controller.User;
import com.nf.dao.RepoDemo;
import com.nf.service.DemoInfoService;

@Service
public class DemoInfoServiceImpl<T> implements DemoInfoService
{
	@Resource
    private RepoDemo demoInfoRepository;
   
    @Resource
    private RedisTemplate<String,T> redisTemplate;
   
    @Override
    public void test(){
           ValueOperations<String,T> valueOperations =redisTemplate.opsForValue();
//           valueOperations.set("mykey4", "random1="+Math.random());
           System.out.println(valueOperations.get("mykey4"));
    }
   
    //keyGenerator="myKeyGenerator"
    @Cacheable(value="userInfo") //缓存,这里没有指定key.
    @Override
    public User findById(String id) {
//           System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id="+id);
           return demoInfoRepository.findOne(id);
    }
    
  //keyGenerator="myKeyGenerator"
    @Cacheable(value="userInfoAll") //缓存,这里没有指定key.
    @Override
    public List<User> findAll(User id) {
           System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id=");
           return (List<User>) demoInfoRepository.findAll();
    }
   
    @CacheEvict(value="userInfo")
    @Override
    public void deleteFromCache(String id) {
           System.out.println("DemoInfoServiceImpl.delete().从缓存中删除.");
    }
}
