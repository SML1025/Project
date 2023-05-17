package net.softsociety.exam.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.domain.bookSukso;
import net.softsociety.exam.service.ReservationService;
import net.softsociety.exam.service.SuksoService;

@Slf4j
@RequestMapping("book")
@Controller
@Component
public class ReservationController {
	@Autowired
	ReservationService service;
	@Autowired
	SuksoService suksoService;

	
	   @PostMapping("/insertRes")
	   public String insertRes(Reservation res, @AuthenticationPrincipal UserDetails user, int qtyInput, int suk_price, String res_resdate, String res_outdate,
	         Model model) {
	      
	        res.setRes_pop(qtyInput);
	      
	      res.setMem_id(user.getUsername());
	   
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	      LocalDate resResDate = LocalDate.parse(res_resdate, formatter);
	      LocalDate resOutDate = LocalDate.parse(res_outdate, formatter);

	      long days = ChronoUnit.DAYS.between(resResDate, resOutDate);
	   
	      int date = (int) days;
	      log.debug("reservation2:{}", date);
	      res.setRes_price(qtyInput*suk_price*date);
	       log.debug("reservation3:{}", res);
	       String mem_id=user.getUsername();    
	       //

	       boolean result=service.checkdate(res);
             
	       if(result==false) {
	    	   service.insertRes(res);
	    	   return "redirect:/";
	       }

             return "redirect:/sukso/read?num=" + res.getSuk_num() + "&error";
	   }

	
	@GetMapping("bookcheck")
	public String bookcheck(ArrayList<bookSukso> bs,  Model model, @AuthenticationPrincipal UserDetails user, String mem_id) {
		mem_id=user.getUsername();
		log.debug("dddddddddd:{}", mem_id);
		ArrayList<Reservation> res=service.findRes(mem_id);
		bs=service.checkbook(mem_id);
		log.debug("res", res);
		model.addAttribute("bs", bs);
		model.addAttribute("res", res);
	
		return "book/bookcheck";
	}
	
	@GetMapping("read")
	public String read(Model model, @RequestParam(name="num", defaultValue="0") int num) {
		Sukso sukso=suksoService.read(num);
		model.addAttribute("sukso", sukso);
		return "book/sukInfo";
	}
	
	   @GetMapping("bookcheck2")
	   public String bookcheck2(ArrayList<bookSukso> bs,  Model model, @AuthenticationPrincipal UserDetails user, String mem_id) {
	      mem_id=user.getUsername();
	      log.debug("dddddddddd:{}", mem_id);
	      ArrayList<bookSukso> bs2=service.findRes2(mem_id);
	      bs=service.checkbook2(mem_id);
	      log.debug("bs2", bs2);
	      model.addAttribute("bs2", bs2);
	      model.addAttribute("bs", bs);
	   
	      return "book/bookcheck2";
	   }
	
	@PostMapping("/updateRes")
	   public String updateRes(Reservation res1, @AuthenticationPrincipal UserDetails user, int qtyInput, int suk_price, String res_resdate, 
	         String res_outdate, Model model, ArrayList<bookSukso> bs) {
	      
	        res1.setRes_pop(qtyInput);
	      
	      res1.setMem_id(user.getUsername());
	   
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	      LocalDate resResDate = LocalDate.parse(res_resdate, formatter);
	      LocalDate resOutDate = LocalDate.parse(res_outdate, formatter);

	      long days = ChronoUnit.DAYS.between(resResDate, resOutDate);
	   
	      int date = (int) days;
	      log.debug("reservation2:{}", date);
	      res1.setRes_price(qtyInput*suk_price*date);
	       log.debug("reservation3:{}", res1);
	     
	       boolean result=service.checkdate2(res1);
	       if(result == true) {
	          service.updateRes(res1);
	      log.debug("reservation1:{}", res1);
	      return "redirect:/";
	          }
	      String errorMSG="예약일이 이미 존재합니다. 다른 날짜를 선택해주세요.";
	       String mem_id=user.getUsername();
	      ArrayList<Reservation> res=service.findRes(mem_id);
	      bs=service.checkbook(mem_id);
	      model.addAttribute("bs", bs);
	      model.addAttribute("res", res);
	       model.addAttribute("errorMSG", errorMSG);
	       return "book/bookcheck";
	      
	   }
	
	
	@GetMapping("cancer")
	public String cancer(@RequestParam(name="num", defaultValue="0") int num) {
		log.debug("num:{}", num);
		service.cancer(num);
		
		return "redirect:/";
	}
	

	/*
	// 스케줄러
	// 3일 = 259200000, 하루 = 86400000, 1시간 = 3600000
	@Scheduled(fixedDelay = 3600000) // 1시간마다
	public void timeUpdate(){
		
		// 모든 reservation 가져오기
		ArrayList<Reservation> AllRes = service.getAllRes();
		String now = null;
		
		// 날짜 비교를 위해 선언할 것
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh");
	    Date compareDate = null;
	    
		// for문으로 reservation 안의 날짜 검증하는 코드
		for (int i = 0; i < AllRes.size(); ++i) {
			Date datein = null;
			try {
				datein = dateFormat.parse(AllRes.get(i).getRes_resdate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Date dateout = null;
			try {
				dateout = dateFormat.parse(AllRes.get(i).getRes_outdate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			now = service.getNow();
			
			try {
				compareDate = dateFormat.parse(now);
				// int로 3일치를 나타내는 수치(밀리세컨드인가?)를 넣는다. -----------------
//				compareDate1 = compareDate + ; 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			log.debug("지금 : {}, 입실 : {}, 퇴실 : {}", now, datein, dateout);
			
			// 날짜 비교해서 실행할 서비스 넣기
			// 3일 = 259200000, 하루 = 86400000, 1시간 = 3600000
			
			// comparedate는 어떻게 되는가?????????????------------------------------
			if(datein.compareTo(compareDate1) < 0) {
				
				// 업데이트문 실행
				service.updateTo1(AllRes.get(i).getRes_num());
				
				// 메시지 보내기
//				service.ResMsg(AllRes.get(i).getRes_num());
			}
			
			if(datein.compareTo(compareDate) < 0) {
				service.updateTo2(AllRes.get(i).getRes_num());
			}
			
			if(dateout.compareTo(compareDate) < 0) {
				service.updateTo3(AllRes.get(i).getRes_num());
			}
			
		}
		
	}
*/
	
	// 스케줄러
	// 3일 = 259200000, 하루 = 86400000, 1시간 = 3600000, 5초 = 5000

	@Scheduled(fixedDelay = 3600000) // 기존 1시간마다 -> 5초로 바꿀 것
	public void timeUpdate(){
		
		// 모든 reservation 가져오기
		ArrayList<Reservation> AllRes = service.getAllRes();
		
		if(AllRes == null) {
			return;
		}
		
		// 날짜 비교를 위해 선언할 것
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String now = null;
	    Date compareDate = null;
	    Date compareDate1 = null;
	    Date compareDate2 = null;
	    
		// for문으로 reservation 안의 날짜 검증하는 코드
		for (int i = 0; i < AllRes.size(); ++i) {
			Date datein = null;
			try {
				datein = dateFormat.parse(AllRes.get(i).getRes_resdate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Date dateout = null;
			try {
				dateout = dateFormat.parse(AllRes.get(i).getRes_outdate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			now = service.getNow();
			
			try {
				compareDate = dateFormat.parse(now);
			// int로 3일치를 나타내는 시간 수치를 넣는다.
			// -3일 + 1시간 = -255600000
			// 1시간마다 수행하기 때문에 원래 대로라면 하나씩 수행해야한다.
			// 시연 때는 이조건에 부합하는 것들이 5초마다 반복해서 실행되니, 당황하지 말 것 
				compareDate1 = new Date(compareDate.getTime() - 259200000);
				compareDate2 = new Date(compareDate.getTime() - 255600000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			log.debug("지금 : {}, 입실 : {}, 퇴실 : {}", now, datein, dateout);
			
			// 날짜 비교해서 실행할 서비스 넣기
			// 3일 = 259200000, 하루 = 86400000, 1시간 = 3600000
			
			if(datein.compareTo(compareDate1) < 0 && datein.compareTo(compareDate2) >= 0) {
				
				// 업데이트문 실행
				if (AllRes.get(i).getRes_state() < 1) {
					service.updateTo1(AllRes.get(i).getRes_num());
				}
				
				// 메시지 보내기
//				service.ResMsg(AllRes.get(i).getRes_num(), AllRes.get(i).getMem_id());
		        int suk_num= AllRes.get(i).getSuk_num();

	            String mem_id= AllRes.get(i).getMem_id();
	         
	            service.ResMsg(suk_num, mem_id);
			}
			
			if(datein.compareTo(compareDate) < 0) {
				if(AllRes.get(i).getRes_state() < 2) {
					service.updateTo2(AllRes.get(i).getRes_num());
				}
			}
			
			if(dateout.compareTo(compareDate) < 0) {
				if(AllRes.get(i).getRes_state() == 3) {
					
				} else if(AllRes.get(i).getRes_state() <= 2 ) {
					service.updateTo3(AllRes.get(i).getRes_num());
				}
			}
		}
		
	}
}
