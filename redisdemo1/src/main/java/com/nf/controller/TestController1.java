package com.nf.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nf.entity.Stock;


@RestController
public class TestController1 {

	
	@Autowired
	private RedisTemplate<String, Stock> redisTemplate;
	
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;

	@RequestMapping(value = "hello")
	public Object testRedisZSet() {
		// 保存对象
		Stock stock = new Stock(54, 456);
		Stock stock1 = new Stock(45, 234);
		Stock stock2 = new Stock(47, 245);
		redisTemplate.delete("000001SZ");
//		key                value                           score
//		000001SZ   {"id":0,"price":54,"amount":456}  1.0
//			       {"id":0,"price":45,"amount":234}  2.0
//			   	   {"id":0,"price":47,"amount":245}  3.0
		
        //将值添加到键中的排序集合，如果已存在，则更新其分数。
        System.out.println("添加元素："+redisTemplate.opsForZSet().add("000001SZ", stock, 1.0));//true （这里的1.0可以用1代替,因为用double收参） 
        System.out.println("添加元素："+redisTemplate.opsForZSet().add("000001SZ",stock1,2.0));//2
        System.out.println("添加元素："+redisTemplate.opsForZSet().add("000001SZ",stock2,3.0));       
 
        System.out.println("总数："+redisTemplate.opsForZSet().zCard("000001SZ"));
        
        //获取有序集合中元素的索引
        long menber = redisTemplate.opsForZSet().rank("000001SZ", stock1);
        System.out.println("stock1的索引："+menber);
        
        System.out.println("查询指定范围的元素："+redisTemplate.opsForZSet().range("000001SZ", 1, 1));
        
        Set<Stock> set =  redisTemplate.opsForZSet().range("000001SZ", 1, 100);
        
        //通过索引区间返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列
        System.out.println("查询所有的元素："+redisTemplate.opsForZSet().range("000001SZ", 0, -1));
        
        //通过索引区间返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从大到小)顺序排列
        System.out.println("查询所有的元素："+redisTemplate.opsForZSet().reverseRange("000001SZ", 0, -1));
		return set;
	}
	
	@Autowired
	private RedisTemplate<String, Map<String, Integer>> redisTemplate1;
	
	@RequestMapping(value = "hello1")
	public Object testRedisList() {
		
		
		redisTemplate1.delete("000003SZ");
		
		Collection<Map<String, Integer>> stockList = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> stock = new HashMap<String, Integer>();
		stock.put("price", 54);
		stock.put("amount", 456);
		Map<String, Integer> stock1 = new HashMap<String, Integer>();
		stock1.put("price", 55);
		stock1.put("amount", 456);
		
		Map<String, Integer> stock2 = new HashMap<String, Integer>();
		stock2.put("price", 56);
		stock2.put("amount", 456);
		
		Map<String, Integer> stock3 = new HashMap<String, Integer>();
		stock3.put("price", 57);
		stock3.put("amount", 456);
		
		stockList.add(stock);
		stockList.add(stock1);
		stockList.add(stock2);
		stockList.add(stock3);
		// 在头部插入数据
//		redisTemplate1.opsForList().leftPush("000003SZ", stock);
//		redisTemplate1.opsForList().leftPush("000003SZ", stock1);
//		redisTemplate1.opsForList().leftPush("000003SZ", stock2);
		redisTemplate1.opsForList().leftPushAll("000003SZ",stockList);
		//查询指定范围的数据
		List<Stock> list = redisTemplate.opsForList().range("000003SZ", 0 ,-1);
		System.out.println("查询指定范围的数据:"+list);
		// 在尾部插入数据
		redisTemplate1.opsForList().rightPush("000003SZ", stock3);
		// 移出第一个数据
		redisTemplate1.opsForList().leftPop("000003SZ");
		// 移出最后一个数据
		redisTemplate1.opsForList().rightPop("000003SZ");
		//查询指定范围的数据:
		System.out.println("查询指定范围的数据:"+redisTemplate1.opsForList().range("000003SZ", 0 ,-1));
		//查询指定下标的数据
		System.out.println("查询指定下标的数据:"+redisTemplate1.opsForList().index("000003SZ", 1));
		//通过索引设置列表元素的值
		redisTemplate1.opsForList().set("000003SZ", 1, stock2);
		//对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
		redisTemplate1.opsForList().trim("000003SZ", 0, 1);
		
		//		count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
		//		count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
		//		count = 0 : 移除表中所有与 VALUE 相等的值。
		System.out.println(redisTemplate1.opsForList().remove("000003SZ", 0, stock));
		//获取集合的数量
		System.out.println("获取集合的数量:"+redisTemplate1.opsForList().size("000003SZ"));
		//查询指定范围的数据:
		System.out.println("查询指定范围的数据:"+redisTemplate1.opsForList().range("000003SZ", 0 ,-1));
		return new String();
	}
	
	/**
	 * 多线程访问redis数据库测试
	 */
	@RequestMapping(value = "hello2")
	public void testRedisList1() {
		Thread t  = null;
		MyThread mr = null;
		redisTemplate1.delete("000005SZ");
		for (int i = 0; i < 10; i++) {
			mr = new MyThread();
			t = new Thread(mr);
			t.start();
		}
	}

	class MyThread implements Runnable {

		@Override
		public void run() {
			int time = 0;
			try {
				time = new Random().nextInt(1000);
				System.out.println(time);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map<String, Integer> stock2 = new HashMap<String, Integer>();
			stock2.put("price", time);
			stock2.put("amount", 456);
			redisTemplate1.opsForList().leftPush("000005SZ", stock2);
			
		}
		
	}
	
	
	@RequestMapping(value = "hello3")
	public Object testRedisHash() {
		Map<String, Map<String, Integer>> stockMap = new HashMap<String, Map<String, Integer>>();
		
		Map<String, Integer> stock = new HashMap<String, Integer>();
		stock.put("price", 54);
		stock.put("amount", 456);
		Map<String, Integer> stock1 = new HashMap<String, Integer>();
		stock1.put("price", 55);
		stock1.put("amount", 456);
		
		Map<String, Integer> stock2 = new HashMap<String, Integer>();
		stock2.put("price", 56);
		stock2.put("amount", 456);
		
		Map<String, Integer> stock3 = new HashMap<String, Integer>();
		stock3.put("price", 57);
		stock3.put("amount", 456);
		
		stockMap.put(stock.get("price").toString(), stock);
		stockMap.put(stock1.get("price").toString(), stock);
		stockMap.put(stock2.get("price").toString(), stock);
		stockMap.put(stock3.get("price").toString(), stock);
		// 删除指定key的数据
		redisTemplate1.delete("stock");
		
//		redisTemplate1.opsForHash().put("stock", stock1.get("price"), stock);
		
		// 插入数据到hash表
		redisTemplate1.opsForHash().putAll("stock", stockMap);

		// 获取所有哈希表中的字段
		System.out.println("获取hash表所有的字段:"+redisTemplate1.opsForHash().keys("stock"));
		// 删除指定hash表字段的数据
		redisTemplate1.opsForHash().delete("stock", stock.get("price").toString());
		// 查看哈希表 key 中，指定的字段是否存在。
		System.out.println(redisTemplate1.opsForHash().hasKey("stock", stock.get("price")));
		// 获取哈希表中字段的数量
		System.out.println("获取哈希表中字段的数量:"+redisTemplate1.opsForHash().size("stock"));
		//获取在哈希表中指定 key 的值
		System.out.println("获取在哈希表中指定 key的所有字段的value值:"+redisTemplate1.opsForHash().values("stock"));
		//获取在哈希表中指定 key 的所有字段和值
		System.out.println("获取在哈希表中指定 key 的所有字段和值:"+redisTemplate1.opsForHash().entries("stock"));
		
		System.out.println("查询指定字段的数据:"+redisTemplate1.opsForHash().get("stock", stock.get("price").toString()));
		Collection<Object> list = new ArrayList<Object>();
		list.add(stock.get("price").toString());
		list.add(stock1.get("price").toString());
		list.add(stock2.get("price").toString());
 		System.out.println("查询指定字段集合的数据:"+redisTemplate1.opsForHash().multiGet("stock", list));
 		
 
		return new String();
	}
	
	
	
	
	
}
