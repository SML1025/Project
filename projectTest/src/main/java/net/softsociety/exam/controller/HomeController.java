package net.softsociety.exam.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.domain.Tags;
import net.softsociety.exam.service.SuksoService;
import net.softsociety.exam.service.TagService;
import net.softsociety.exam.util.PageNavigator;

@Slf4j
@Controller
public class HomeController {
	@Autowired
	SuksoService service;
	
	@Autowired
	TagService tagservice;
	
	@Value("${user.board.page}")
	int countPerPage;
	
	@Value("${user.board.group}")
	int pagePerGroup;
	

	/**
	 * 메인화면(작업 중)
	 * 추가된 내용 : 태그를 파라미터로 입력받는다.
	 */
	@GetMapping({"", "/"})
	public String home(Model model
			, @RequestParam(name="page", defaultValue="1")int page
//	        , @RequestParam(name="suk_nearby", required=false) String suk_nearby
	        , String suk_nearby
//	        , @RequestParam(name="suk_location", required=false) String suk_location
	        , String suk_location
//			, @RequestParam(name="tags", required=false) String tags
			, String tags
			, @AuthenticationPrincipal UserDetails user
			) {
		log.debug("tags 확인 : {}", tags);
		
		// 여기다가 tags를 arraylist로 바꾸는 코드 입력하고, 바꾼 tagArr을 tagList로 해서
		// 다른 XML 파일들이 사용할 수 있도록 서비스랑 다오까지 싹다 바꾸기.
		// xml파일에는 반복문을 통해서, 여러 개 가져오는 코드를 입력.
		// ----------------------------------------------------------------
		// 현재 에러! 가져오는 여러개를 가져오면 에러 뜸
		
		List<String> tagList = null;
		
		log.debug("전달된 tags : {}", tags);
		
		// 처리하기
		if (tags != null) {
			
			tags = tags.replace("[", "");
			tags = tags.replace("]", "");
			tags = tags.replace("value", "");
			tags = tags.replace("{", "");
			tags = tags.replace("}", "");
			tags = tags.replace(":", "");
			tags = tags.replace("\"", "");
			
			log.debug("처리된 tags : {}", tags);	
			
			// 문자열 나누기
			String[] tagArr = tags.split(",");
			
			// 검색 조건으로 사용할 태그 리스트
			tagList = Arrays.asList(tagArr);
		}else {
			tagList = new ArrayList<>();
		}
		
		//////////////////////////// 여기 실험중==> 아닌가 ? impl에서 하고 있나?
		
		// 찾는 태그가 1개면 실행.
		if (tagList.size() == 1) {
			
		} else if( tagList.size() > 1 || tagList.size() == 0 ) {
			
		}
		
		PageNavigator navi = 
				service.getPageNavigator(pagePerGroup, countPerPage, page, suk_location, suk_nearby, tagList);
		log.debug("navi: {}", navi);
		
//		List<Sukso> suksolist = service.suksolist(page, countPerPage, suk_location, suk_nearby, tagList);		
//		ArrayList<Sukso> suksolist = service.suksolist(navi.getStartRecord(), countPerPage, suk_location, suk_nearby, tagList);
		
		List<Sukso> suksolist = service.selectByTags(navi.getStartRecord(), countPerPage, suk_location, suk_nearby, tagList);
		
		
		log.debug("navi: {}", navi);
		
		model.addAttribute("nearby", suk_nearby);
		log.debug("nearby : {}", suk_nearby);
		model.addAttribute("location", suk_location);
		log.debug("location : {}", suk_location);
		model.addAttribute("tagList", tagList);
		log.debug("tagList : {}", tagList);
		
		
		model.addAttribute("suksolist", suksolist);
		model.addAttribute("navi", navi);

		
		return "mainpage";
	}
	
}
