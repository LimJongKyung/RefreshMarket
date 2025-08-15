package com.refresh.login.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.refresh.member.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/PNC")
public class PacelNavController {

	@Autowired
	private final MemberService memberService;

	@Autowired
    public PacelNavController (MemberService memberService) {
        this.memberService = memberService;
    }
	
	
	// 공동구매 하트
	@GetMapping("/like")
	public String like(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		String name = (String) memberService.getUserByName(userId);
		
		model.addAttribute("username", name);
		return "loginrefresh/parcelNav/LikeIt";
	}
}
