package com.zcc.wallet.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zcc.wallet.common.SysCode;
import com.zcc.wallet.service.HomeService;
import com.zcc.wallet.vo.Account;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private HomeService homeService;
	
	//退出登陆
	@RequestMapping("/test")
	public void test(HttpServletRequest request,HttpServletResponse response){
		homeService.getCompPos();
	}
}
