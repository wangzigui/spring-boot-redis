package com.nf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nf.controller.User;
import com.nf.entity.Product;
import com.nf.service.DemoInfoService;

@Controller
@SpringBootApplication
public class TestApplication {
	public static void main(String[] args) {
		
		SpringApplication.run(TestApplication.class, args);
	}
	
	
}
