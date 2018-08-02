package com.nf.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nf.dao.RepoDemo;
import com.nf.service.DemoInfoService;

@RestController
public class TestController1 {

	
//	ZADD key score1 member1 [score2 member2] 
//			向有序集合添加一个或多个成员，或者更新已存在成员的分数
//			2	ZCARD key 
//			获取有序集合的成员数
//			3	ZCOUNT key min max 
//			计算在有序集合中指定区间分数的成员数
//			4	ZINCRBY key increment member 
//			有序集合中对指定成员的分数加上增量 increment
	@Autowired
	private RedisTemplate<String, User> redisTemplate;
	
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;

	@RequestMapping(value = "hello")
	public Object helloworld() {
		// 保存对象
//		User user = new User("超人", "sdf");
//		user.setId("sdf");
//		redisTemplate.opsForValue().set(user.getUsername(), user);
//		user = new User("蝙蝠侠", "sdf");
//		user.setId("sdf");
//		redisTemplate.opsForValue().set(user.getUsername(), user);
//		user = new User("蜘蛛侠", "sdf");
//		user.setId("sdf");
//		redisTemplate.opsForValue().set(user.getUsername(), user);
//
//		System.out.println(redisTemplate.opsForValue().get("超人").toString());
//		System.out.println(redisTemplate.opsForValue().get("蝙蝠侠").toString());
//		System.out.println(redisTemplate.opsForValue().get("蜘蛛侠").toString());
//		System.out.println(redisTemplate.opsForZSet());
		User user = new User("超人", "sdf");
		User user1 = new User("蝙蝠侠", "sdf");
		User user2 = new User("蜘蛛侠", "sdf");
		redisTemplate.delete("fan1");
        //将值添加到键中的排序集合，如果已存在，则更新其分数。
        System.out.println(redisTemplate.opsForZSet().add("fan1", user, 1.0));//true （这里的1.0可以用1代替,因为用double收参） 
//        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("b",2.0);//这里必须是2.0，因为那边是用Double收参
//        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<Object>("c",3.0);
//        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
//        tuples.add(objectTypedTuple1);
//        tuples.add(objectTypedTuple2);
        System.out.println(redisTemplate.opsForZSet().add("fan1",user1,2.0));//2
        System.out.println(redisTemplate.opsForZSet().add("fan1",user2,3.0));
        //通过索引区间返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列
        System.out.println(redisTemplate.opsForZSet().range("fan1",0,-1));
 
        
//        System.out.println(redisTemplate.opsForZSet().incrementScore(key, value, delta));
        //获取有序集合中元素的索引
        long menber = redisTemplate.opsForZSet().rank("fan1", user1);
        System.out.println(menber);
        System.out.println(redisTemplate.opsForZSet().range("fan1", menber, menber));
        Set<User> set =  redisTemplate.opsForZSet().range("fan1", 1, 100);
		return set;
	}

	@Resource
	private RepoDemo demoInfoRepository;

	@Autowired
	DemoInfoService demoInfoService;

	@RequestMapping(value = "/test")
	public String helloworld(@RequestParam String id,@RequestParam String id1) {
		User loaded = demoInfoService.findById(id);
		System.out.println("loaded=" + loaded);
		User cached = demoInfoService.findById(id);
		System.out.println("cached=" + cached);
		loaded = demoInfoService.findById(id1);
		System.out.println("loaded2=" + loaded);
		User user = new User();
		List<User> loadeds = demoInfoService.findAll(id);
		System.out.println(loadeds.toString());
//		demoInfoService.deleteFromCache(id);
//		demoInfoService.updateUser(id1);
//		System.out.println(redisTemplate.opsForZSet().range("userInfo", 0, 10));
//		System.out.println(redisTemplate.opsForValue().get("超人").toString());
		return "index";
	}

//	@RequestMapping(value = "/test1")
//	public String helloworld1(@RequestParam String id,@RequestParam String id1) {
//		List<User> users = (redisTemplate.opsForList().range("userInfosa", 0, 100));
//		if(users != null &&!users.isEmpty())
//		{
//			System.out.println("users:"+users.toString());
//			return "hello";
//		}
//		
//		
//		List<User> loadedAll = (List<User>) demoInfoRepository.findAll();
//		redisTemplate.opsForList().leftPushAll("userInfosa", loadedAll);
//		System.out.println("loadedAll:"+loadedAll.toString());
//		
//		return "index";
//	}

	@RequestMapping("/delete")
	public String delete(@RequestParam String id) {
		demoInfoService.deleteFromCache(id);
		return "ok";
	}
	
	@RequestMapping("/update")
	public String update(@RequestParam String id) {
		demoInfoService.updateUser(id);
		return "ok";
	}
}
