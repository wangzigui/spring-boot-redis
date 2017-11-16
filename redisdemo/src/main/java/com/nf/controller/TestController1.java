package com.nf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@RequestMapping(value = "hello")
	public String helloworld() {
		// 保存对象
		User user = new User("超人", "sdf");
		user.setId("sdf");
		redisTemplate.opsForValue().set(user.getUsername(), user);
		user = new User("蝙蝠侠", "sdf");
		user.setId("sdf");
		redisTemplate.opsForValue().set(user.getUsername(), user);
		user = new User("蜘蛛侠", "sdf");
		user.setId("sdf");
		redisTemplate.opsForValue().set(user.getUsername(), user);

		System.out.println(redisTemplate.opsForValue().get("超人").toString());
		System.out.println(redisTemplate.opsForValue().get("蝙蝠侠").toString());
		System.out.println(redisTemplate.opsForValue().get("蜘蛛侠").toString());

		return "hello";
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
		List<User> loadeds = demoInfoService.findAll(user);
		System.out.println(loadeds.toString());
//		System.out.println(redisTemplate.opsForZSet().range("userInfo", 0, 10));
//		System.out.println(redisTemplate.opsForValue().get("超人").toString());
		return "index";
	}

	@RequestMapping(value = "/test1")
	public String helloworld1(@RequestParam String id,@RequestParam String id1) {
		List<User> users = (redisTemplate.opsForList().range("userInfosa", 0, 100));
		if(users != null &&!users.isEmpty())
		{
			System.out.println("users:"+users.toString());
			return "hello";
		}
		
		
		List<User> loadedAll = (List<User>) demoInfoRepository.findAll();
		redisTemplate.opsForList().leftPushAll("userInfosa", loadedAll);
		System.out.println("loadedAll:"+loadedAll.toString());
		
		return "index";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam String id) {
		demoInfoService.deleteFromCache(id);
		return "ok";
	}
}
