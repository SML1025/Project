package net.softsociety.exam.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
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
import net.softsociety.exam.domain.Intag;
import net.softsociety.exam.domain.Member;
import net.softsociety.exam.domain.Review;
import net.softsociety.exam.domain.SUM;
import net.softsociety.exam.domain.Suk_files;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.domain.Sukso_spec;
import net.softsociety.exam.domain.Tags;
import net.softsociety.exam.service.SuksoService;
import net.softsociety.exam.service.TagService;
import net.softsociety.exam.util.FileService;
import net.softsociety.exam.util.PageNavigator;

@Slf4j
@RequestMapping("sukso")
@Controller
public class SuksoController {
	
	@Value("${user.board.page}")
	int countPerPage;
	
	@Value("${user.board.group}")
	int pagePerGroup;
	
	
	@Autowired
	SuksoService service;
	
	@Autowired
	TagService tagservice;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveData(List<Integer> dataList) {
	    String sql = "INSERT INTO pp_suk_files () VALUES (?,?,?,?)";
	    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
	        @Override
	        public void setValues(PreparedStatement ps, int i) throws SQLException {
	            ps.setInt(1, dataList.get(i));
	        }

	        @Override
	        public int getBatchSize() {
	            return dataList.size();
	        }
	    });
	}
	
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	
	@GetMapping("insertsukso")
	public String insertsukso() {
		return "insertSuk";
	}
	
	
	// 태기파이 추가됨
	@PostMapping("join")
	public String writeform(Sukso sukso, Sukso_spec spec, String tags,
			@AuthenticationPrincipal UserDetails user, MultipartFile upload, ArrayList<MultipartFile> upload1) {
		
		if(upload!=null && !upload.isEmpty()) {
			String filename=FileService.saveFile(upload, "c:/upload");
			sukso.setSuk_files_orz(upload.getOriginalFilename());
			sukso.setSuk_files_saved(filename);
		}
		
		
		
		  
		log.debug("sukso1:{}", sukso);
		sukso.setMem_id(user.getUsername());
		log.debug("sukso2:{}", sukso);
		service.insertsukso(sukso);
		log.debug("spec:{}", spec);
		spec.setSuk_num(sukso.getSuk_num());
		service.insertspec(spec);
		
		if(upload1 != null && !upload1.isEmpty()) {
		
			for(MultipartFile mf : upload1) {
				String filename=FileService.saveFile(mf, "c:/upload");
				Suk_files sf = new Suk_files();
				sf.setSuk_num(sukso.getSuk_num());
				sf.setSuk_files_orz(mf.getOriginalFilename());
				sf.setSuk_files_saved(filename);
				
				service.picture(sf);
			}
			  
		}
		
		
		
		// 태기파이 입력
		
		ArrayList<Integer> taglist = new ArrayList<>();
		Tags tagOne;
		String ar[] = tags.split(",");
		
		for (String s : ar) {
			System.out.println("분리된 문자열:" + s);
			log.debug("전달된 tags : {}", tags);
			s = s.replace("[", "");
			s = s.replace("]", "");
			s = s.replace("value", "");
			s = s.replace("{", "");
			s = s.replace("}", "");
			s = s.replace(":", "");
			s = s.replace("\"", "");
			log.debug("처리된 tags : {}", tags);
			
			
			tagOne = tagservice.selectOne(s);
			if (tagOne != null) {
				taglist.add(tagOne.getId());
			} else {
				tagOne = new Tags();
				tagOne.setName(s);
				service.insert(tagOne);		// 태그에 넣는 인서트
				taglist.add(tagOne.getId());	// 태그리스트에 추가
			}
			log.debug(s);
		}
		
		for (int s : taglist) {
			log.debug("태그 아이디 번호 넣기 : {}", s);
			Intag i = new Intag();
			i.setSuk_num(sukso.getSuk_num());
			i.setId(s);
			
			service.insert2(i);		// intag에 넣는 인서트(호스트)
		}

		return "redirect:/";
	}
	
	@GetMapping("read")
	public String read(
			@RequestParam(name="num", defaultValue="0") int num, Model model,
			@RequestParam(name="page", defaultValue="1") int page
			, @AuthenticationPrincipal UserDetails user
			) {
		
		PageNavigator navi = 
				service.getPageNavigator1(pagePerGroup, countPerPage, page, num);
		
		Sukso sukso=service.read(num);
		log.debug("bbbbbbaa:{}", sukso);
		model.addAttribute("sukso", sukso);
		ArrayList<Review> review=service.readReview(navi.getStartRecord(), countPerPage, num);
		log.debug("bbbreview:{}", review);
		
		model.addAttribute("review", review);
		
		// 전체 intag 리스트를 가져오기
		List<Intag> intagList = service.getIntagList();
		model.addAttribute("intagList", intagList);

		// 전체 태그 리스트를 가져오기
		List<Tags> tagsList = service.getTagsList();
		model.addAttribute("tagsList", tagsList);
		
		// 숙소 ID로 intag의 모든 태그들 가져오기
		List<Intag> intagListBySuk_Id = service.getIntagListBySuk(sukso.getSuk_num());
		model.addAttribute("intagListBySuk_Id", intagListBySuk_Id);
		
		// 숙소 ID에 맞는 호스트가 넣은 태그 가져오기
		List<Tags> tagsListHost = service.getTagsListByHost(sukso.getSuk_num());
		model.addAttribute("tagsListByHost", tagsListHost);
		
		// 숙소 ID에 맞는 리뷰어가 넣은 태그 가져오기
		List<Tags> tagsListRev = service.getTagsListByRev(sukso.getSuk_num());
		model.addAttribute("tagsListByRev", tagsListRev);
		
		model.addAttribute("navi", navi);
		
		if(user != null) {
		String mem_id = user.getUsername();
		int suk_num=sukso.getSuk_num();
		boolean res= service.rescheck(mem_id, suk_num);
		boolean res2 = service.checkRes(mem_id);
		log.debug("이프문 실행전");
		log.debug("결과 : {}", res2 );

			if (res) {
				log.debug("모델 실행전");
				model.addAttribute("res", res);
			}
			if (res2) {
				model.addAttribute("res2", res2);
				log.debug("res2:{}", res2);
			}
		
		}
		
		
	  ArrayList<Suk_files> files= service.getpicture(num);
	  model.addAttribute("files", files);
		
		
		
		log.debug("이프문 빠져나옴");
		
		// 성호씨 코드 

		Member member = service.member(sukso.getMem_id());
	    log.debug("member", member);
	    model.addAttribute("member", member);
		
	    Sukso_spec sukso_spec = service.sukso_spec(num);

	    log.debug("확인:{}", sukso_spec.getSuk_notice());
	     model.addAttribute("sukso_spec", sukso_spec);
	     
	     
	     SUM sum = service.star(num);
	     
	     model.addAttribute("sum", sum);
	     
		
		return "suksoInfo3";
	}
       
 
	@GetMapping("download")
	public void download(
			@RequestParam(name="num", defaultValue="0") int num
			,HttpServletResponse response) {
		Sukso sukso=service.selectSukso(num);
		
		if(sukso==null || sukso.getSuk_files_saved()==null) {
			
			return ;
		}
	String file= uploadPath + "/" + sukso.getSuk_files_saved();
	FileInputStream in = null;
	ServletOutputStream out = null;
	
	try {
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sukso.getSuk_files_orz(), "UTF-8"));
		in=new FileInputStream(file);
		out = response.getOutputStream();
		FileCopyUtils.copy(in,out);
		in.close();
		out.close();
	}
	catch(IOException e){
		
	}
	return ;
	}
	
	
	@GetMapping("review/download")
	public void reviewdownload(
			@RequestParam(name="num", defaultValue="0") int num
			,HttpServletResponse response) {
		Review review=service.readRev(num);
		if(review==null || review.getRev_files_saved()==null) {
			
			return ;
		}
	String file= uploadPath + "/" + review.getRev_files_saved();
	FileInputStream in = null;
	ServletOutputStream out = null;
	
	try {
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(review.getRev_files_orz(), "UTF-8"));
		in=new FileInputStream(file);
		out = response.getOutputStream();
		FileCopyUtils.copy(in,out);
		in.close();
		out.close();
	}
	catch(IOException e){
		
	}
	return ;
	}
	
	
	@GetMapping("files/download")
	public void filesdownload(
			@RequestParam(name="num", defaultValue="0") int num
			,HttpServletResponse response) {
		Suk_files files=service.getpic(num);
		if(files==null || files.getSuk_files_saved()==null) {
			
			return ;
		}
	String file= uploadPath + "/" + files.getSuk_files_saved();
	FileInputStream in = null;
	ServletOutputStream out = null;
	
	try {
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(files.getSuk_files_orz(), "UTF-8"));
		in=new FileInputStream(file);
		out = response.getOutputStream();
		FileCopyUtils.copy(in,out);
		in.close();
		out.close();
	}
	catch(IOException e){
		
	}
	return ;
	}
	
	
	
	@PostMapping("/writeReview")
	public String writeReply(Review review
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile upload
			, String tags
			, int rev_star_access
			, int rev_star_clean
			,int rev_star_service
			,int rev_star_faci
			//Reservation res, int suk_num, int res_state
			) {
		log.debug("upload:{}", upload.getOriginalFilename());
		if(upload!=null && !upload.isEmpty()) {
			String filename=FileService.saveFile(upload, "c:/upload");
			review.setRev_files_orz(upload.getOriginalFilename());
			review.setRev_files_saved(filename);
		}
//		res.setMem_id(user.getUsername());
//		res.setSuk_num(suk_num);
	
	    review.setRev_star_avg((rev_star_clean+rev_star_access+rev_star_service+rev_star_faci)/4);
		log.debug("리뷰 별점1 : {}", review.getRev_star_clean());
		review.setMem_id(user.getUsername());
		log.debug("reivew:{}", review);
		service.writeReview(review);
		log.debug("{}", review.getRev_num());
		log.debug("리뷰 별점2 : {}", review.getRev_star_clean());
		
	
		
		Sukso sukso = service.selectSukso(review.getSuk_num());
		log.debug("누나1 {}", tags);
		ArrayList<Integer> taglist = new ArrayList<>();
		Tags tagOne; 
		String ar[] = tags.split(",");
		
		log.debug("누나2 : {}", tags);
		
		for (String s : ar) {
			System.out.println("분리된 문자열:" + s);
			log.debug("전달된 tags : {}", tags);
			
			s = s.replace("[", "");
			s = s.replace("]", "");
			s = s.replace("value", "");
			s = s.replace("{", "");
			s = s.replace("}", "");
			s = s.replace(":", "");
			s = s.replace("\"", "");

			tagOne = tagservice.selectOne(s);
			if (tagOne != null) {
				taglist.add(tagOne.getId());
			} else {
				tagOne = new Tags();
				tagOne.setName(s);
				service.insert(tagOne);		// 태그에 넣는 인서트
				taglist.add(tagOne.getId());	// 태그리스트에 추가
			}
			log.debug(s);
		}

		for (int s : taglist) {
			log.debug("태그 아이디 번호 넣기 : {}", s);
			Intag i = new Intag();
			i.setSuk_num(sukso.getSuk_num());
			i.setId(s);

			service.insert3(i);		// intag에 넣는 인서트(리뷰)
		}
		
		 return "redirect:read?num=" + review.getSuk_num();
	}

	
	
	
}
