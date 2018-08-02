package com.nf.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.nf.controller.User;
import com.nf.dao.RepoDemo;
import com.nf.service.DemoInfoService;

@Service
//@CacheConfig(cacheNames = "users")
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
    @Cacheable(value = "Product",key="#p0") //缓存,这里没有指定key.
    @Override
    public User findById(String id) {
           System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id="+id);
           return demoInfoRepository.findOne(id);
    }
    
  //keyGenerator="myKeyGenerator"
    @Cacheable(value="findAll") //缓存,这里没有指定key.
    @Override
    public List<User> findAll(String id) {
           System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id=");
           return (List<User>) demoInfoRepository.findAll();
    }
   
    @CacheEvict(key="#p0")
    @Override
    public void deleteFromCache(String id) {
           System.out.println("DemoInfoServiceImpl.delete().从缓存中删除.");
           demoInfoRepository.delete(id);
    }
    
    @CachePut(key="#p0")
    @Override
    public User updateUser(String id) {
           System.out.println("DemoInfoServiceImpl.update().从缓存中删除.");
           User user = demoInfoRepository.findOne(id);
           String username = UUID.randomUUID().toString();
           user.setUsername(username);
           demoInfoRepository.updateById(id, username);
           return user;
    }
}
