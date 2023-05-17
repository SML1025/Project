package net.softsociety.exam.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.domain.MSG;
import net.softsociety.exam.service.MSGService;
import net.softsociety.exam.util.FileService;




@Slf4j
@RequestMapping("msg")
@Controller
public class MsgController {
	@Autowired
	MSGService service;


	@GetMapping("checkMSG")
	public String checkMSG(Model model,@AuthenticationPrincipal UserDetails user, ArrayList<MSG> msg) {
		String receiver_mem_id=user.getUsername();
		msg = service.findMSG(receiver_mem_id);
		model.addAttribute("msg", msg);
		return "MSG/checkMSG";
	}
	
	
	

 

	
	@PostMapping("/SendMsg")
	public String SendMSG(MSG msg, @AuthenticationPrincipal UserDetails user) {
		
		msg.setSender_mem_id(user.getUsername());
		service.sendMSG(msg);
		log.debug("msg : {}", msg);
		 return "redirect:/";
	}
	
	@GetMapping("letterMSG")
	public String letterMSG(Model model,@AuthenticationPrincipal UserDetails user, @RequestParam(name="sender_mem_id", defaultValue="0") String sender_mem_id, 
			MSG msg, @RequestParam(name="suk_num", defaultValue="0") int suk_num) {
		msg.setSender_mem_id(user.getUsername());
		msg.setReceiver_mem_id(sender_mem_id);
		msg.setSuk_num(suk_num);
		model.addAttribute("msg", msg);
		
		return "MSG/sendMSG";
	}
	
	@GetMapping("letterMSG1")
	   public String letterMSG1(Model model, @AuthenticationPrincipal UserDetails user, @RequestParam(name="receiver_mem_id", defaultValue="0") String receiver_mem_id, 
	         MSG msg, @RequestParam(name="suk_num", defaultValue="0") int suk_num) {
	      msg.setSender_mem_id(user.getUsername());
	      msg.setReceiver_mem_id(receiver_mem_id);
	      msg.setSuk_num(suk_num);
	      model.addAttribute("msg", msg);
	      
	      return "MSG/sendMSG";
	   }
	
	
	

	
	
	
	
	
	
}
