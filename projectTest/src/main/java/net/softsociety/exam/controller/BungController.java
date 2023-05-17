package net.softsociety.exam.controller;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.domain.Bung;
import net.softsociety.exam.domain.BungComment;
import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.service.BungService;
import net.softsociety.exam.service.ReservationService;
import net.softsociety.exam.service.SuksoService;

@Slf4j
@RequestMapping("bung")
@Controller
public class BungController {
   @Autowired
   BungService bungService;
   @Autowired
   ReservationService reservationService;

   @GetMapping("bung")
   public String bung(Model model, @AuthenticationPrincipal UserDetails user, Reservation reservation, Sukso sukso) {
      ArrayList<Bung> bungList = bungService.list();
      model.addAttribute("bungList", bungList);
      reservation = reservationService.getReservationByMemid(user.getUsername());
      sukso = bungService.getAddress(reservation.getSuk_num());
      String suk_address = sukso.getSuk_address();
      model.addAttribute("suk_address_js", suk_address);

      log.debug("내 suk_address_js {}", suk_address);

      return "/bung/bung";
   }

   @GetMapping("bungWrite")
   public String bungWrite() {
      return "bung/bungWrite";
   }

   @PostMapping("bungWrite")
   public String bungWrite(Bung bung, @AuthenticationPrincipal UserDetails user,
         @RequestParam(required = false) Integer res_num, @RequestParam(required = false) Integer suk_num) {
      Reservation reservation = reservationService.getReservationByMemid(user.getUsername());
      if (reservation == null) {
         // 해당 사용자에 대한 예약이 없는 경우, 오류 페이지로 리디렉션
         return "redirect:/error/reservationNotFound";
      }
      bung.setMem_id(user.getUsername());
      bung.setRes_num(reservation.getRes_num());
      bung.setSuk_num(reservation.getSuk_num());
      /*
       * log.debug("bung {}", bung);
       */ bungService.bungWrite(bung);
      return "redirect:/bung/bung";
   }

   @PostMapping("bungWriteComm")
   public String bungWriteComm(BungComment bungComment, @AuthenticationPrincipal UserDetails user) {

      bungComment.setMem_id(user.getUsername());

      bungService.bungWriteComm(bungComment);
      /*
       * log.debug("bungComment {}", bungComment);
       */ return "/bung/bung";
   }

   @PostMapping("bungSearch")
   public String bungSearch(@RequestParam("bung_cate") String bung_cate, Model model) {
      ArrayList<Bung> bung = bungService.bungSearch(bung_cate);
      model.addAttribute("bung", bung);
      /*
       * log.debug("bung {}", bung);
       */ return "bung/bung";
   }

   @GetMapping("/post")
   public String getPostContent(@PathVariable("bung_num") int bung_num, Model model) {
      // bung_num에 해당하는 게시글의 내용을 가져와서 모델에 담아서 bung.html로 반환
      Bung bung = bungService.getBungByNum(bung_num); // bung_num에 해당하는 게시글 정보 조회
      String bung_contents = bung.getBung_contents(); // 게시글 내용 가져오기

      // 모델에 데이터를 담아서 bung.html로 반환
      model.addAttribute("bung_num", bung_num);
      model.addAttribute("bung_contents", bung_contents);
      return "bung/bung"; // bung.html이 있는 "templates/bung" 디렉터리로 매핑
   }

   @ResponseBody
   @PostMapping("getAddress")
   public ResponseEntity<Map<String, String>> getAddress(int suk_num) {
      Sukso sukso = bungService.getAddress(suk_num);
      Bung bung = bungService.getBungBySukNum(suk_num);
      if (sukso == null || sukso.getSuk_address() == null) {
         // 유효한 값이 없는 경우 404 Not Found 상태 코드 반환
         return ResponseEntity.notFound().build();
      }
      String bung_title = bung.getBung_title();
      String suk_address = sukso.getSuk_address();
      Map<String, String> response = new HashMap<>();
      response.put("suk_address", suk_address);
      response.put("bung_title", bung_title);
      return ResponseEntity.ok(response);
   }

}