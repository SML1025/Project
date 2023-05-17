package net.softsociety.exam.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.dao.SuksoDAO;
import net.softsociety.exam.dao.TagDAO;
import net.softsociety.exam.domain.Intag;
import net.softsociety.exam.domain.Member;
import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.Review;
import net.softsociety.exam.domain.SUM;
import net.softsociety.exam.domain.SukNumCount;
import net.softsociety.exam.domain.Suk_files;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.domain.Sukso_spec;
import net.softsociety.exam.domain.Tags;
import net.softsociety.exam.util.PageNavigator;

@Slf4j
@Service
public class SuksoServiceImpl implements SuksoService {
	@Autowired
	SuksoDAO dao;
	
	@Autowired
	TagDAO tag;
	
	@Override
	public int insertsukso(Sukso sukso) {
		int n = dao.insertsukso(sukso);
		return n;
	}

	@Override
	public Sukso read(int num) {
		Sukso sukso = dao.selectSukso(num);
		return sukso;
	}

	@Override
	public Sukso selectSukso(int num) {
		Sukso sukso = dao.selectSukso(num);
		return sukso;
	}

	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String suk_location, String suk_nearby, List<String> tagList) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("suk_nearby", suk_nearby);
		map.put("suk_location", suk_location);
		map.put("tagList", tagList); // 여기 문제 있음(리스트로 어떻게 가져오는가?)
		int t = dao.searchCount(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, t);
		return navi;
	}

	
	
//	@Override
//	public ArrayList<Sukso> suksolist(int startRecord, int countPerPage, String suk_location, String suk_nearby, List<String> tagList) {
//		RowBounds rb= new RowBounds(startRecord, countPerPage);
//		HashMap<String, String> map = new HashMap<>();
//		map.put("suk_nearby", suk_nearby);
//		map.put("suk_location", suk_location);
////		map.put("tags", tagList); // 여기 리스트로 해야함
//		ArrayList<Sukso> list = dao.selectSuksoList(map, rb);
//		return list;
//	}

	@Override
	public List<Sukso> selectByTags(int startRecord, int countPerPage, String suk_location, String suk_nearby, List<String> tagList) {
		RowBounds rb= new RowBounds(startRecord, countPerPage);
		HashMap<String, Object> map = new HashMap<>();
	    map.put("tags", tagList);
	    map.put("suk_location", suk_location);
	    map.put("suk_nearby", suk_nearby);
	    
	    log.debug("serviceImpl : {}", map);
		List<SukNumCount> SukList = dao.selectSuksoList(map, rb);
		log.debug("SukNumCount : {}", SukList);
		List<Sukso> sk = new ArrayList<>();
		
		for(SukNumCount snc : SukList) {
			if(snc.getCount() >= 2 || snc.getCount() == 0) {
				sk.add(dao.getSuksoBySukNUm(snc.getSuk_num(), rb));
			}
//			여기 넣어야하나?
			else if (snc.getCount() == 1) {
				sk.add(dao.getSuksoBySukNUm(snc.getSuk_num(), rb));
			}
		}
		
		return sk;
	}	

	// 
//	@Override
//	public List<Sukso> step1(int startRecord, int countPerPage, String suk_location, String suk_nearby, List<Sukso> tagList) {
//		RowBounds rb= new RowBounds(startRecord, countPerPage);
//		HashMap<String, Object> map = new HashMap<>();
//	    map.put("tags", tagList);
//	    map.put("suk_location", suk_location);
//	    map.put("suk_nearby", suk_nearby);
//		List<Sukso> SukList = dao.selectSuksoList(map, rb);
//		return SukList;
//	}	
	
	// 임시 저장
//	@Override
//	public List<Sukso> selectByTags(List<String> tagList, String suk_location, String suk_nearby) {
//		HashMap<String, Object> map = new HashMap<>();
//	    map.put("tags", tagList);
//	    map.put("suk_location", suk_location);
//	    map.put("suk_nearby", suk_nearby);
//		List<Sukso> SukList = dao.selectSuksoList(map);
//		return SukList;
//	}
	
	@Override
	public int searchCount(String suk_location, String suk_nearby, List<String> tagList) {
		HashMap<String, Object> map = new HashMap<>();
	    map.put("tags", tagList);
	    map.put("suk_location", suk_location);
	    map.put("suk_nearby", suk_nearby);
	    
		int n = dao.searchCount(map);
		
		return n;
	}
	
	@Override
	public Member find(String mem_id) {
		Member member = dao.find(mem_id);
		return member;
	}

	@Override
	public int writeReview(Review review) {
		
		log.debug("리뷰 별점 : {}", review.getRev_star_clean());
		int n = dao.writeReview(review);
		return n;
	}

	@Override
	public ArrayList<Review> readReview(int startRecord, int countPerPage, int num) {
		RowBounds rb= new RowBounds(startRecord, countPerPage);
		ArrayList<Review> review = dao.readReview(num, rb);
		log.debug("리뷰 : {}", review);
		
		return review;
	}

	@Override
	public Review readRev(int num) {
		Review review = dao.readRev(num);
		return review;
	}

	


	@Override
	public PageNavigator getPageNavigator1(int pagePerGroup, int countPerPage, int page, int num) {
		int t= dao.rev_count(num);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, t);
		return navi;
	}

	@Override
	public int insertspec(Sukso_spec spec) {
		int n = dao.insertspec(spec);
		return n;
	}
	
	@Override
	   public boolean rescheck(String mem_id, int suk_num) {
	      HashMap<String, Object> map = new HashMap<>();
	      map.put("suk_num", suk_num);
	      map.put("mem_id", mem_id);
	      ArrayList<Reservation> res = dao.rescheck(map);
	      log.debug("res:{}", res);
	      return res != null && res.size() != 0;
	}
	
	@Override
	public int insert(Tags tags) {
		int n = dao.insert(tags);
		return n;
	}

	@Override
	public int insert2(Intag i) {
		int n = dao.insert2(i);
		
		return n;
	}
	
	@Override
	public int insert3(Intag i) {
		int n = dao.insert3(i);
		return n;
	}

	@Override
	public Sukso_spec sukso_spec(int num) {
	      Sukso_spec s = dao.selectSpec(num);
	      return s;
	}

	@Override
	public Member member(String mem_id) {
	      Member member = dao.selectMember(mem_id);
	      return member;

	}

	@Override
	public List<Intag> getIntagList() {
		List<Intag> intag = dao.getIntagList();
		return intag;
	}

	@Override
	public List<Tags> getTagsList() {
		List<Tags> tags = dao.getTagsList();
		return tags;
	}

	@Override
	public List<Intag> getIntagListBySuk(int suk_id) {
		List<Intag> intag = dao.getIntagListBySuk(suk_id);
		return intag;
	}

	@Override
	public List<Tags> getTagsListByHost(int suk_num) {
		List<Tags> tags = dao.getTagsListByHost(suk_num); 
		return tags;
	}

	@Override
	public List<Tags> getTagsListByRev(int suk_num) {
		List<Tags> tags = dao.getTagsListByRev(suk_num);
		return tags;
	}

	@Override
	public boolean checkRes(String mem_id) {
		ArrayList<Reservation> res2=dao.checkRes(mem_id);
		return res2 != null &&res2.size() != 0;
	}

	@Override
	public int picture(Suk_files sf) {
		int n = dao.picture(sf);
		return n;
	}

	// 사진 가져오기
	@Override
	   public ArrayList<Suk_files> getpicture(int num) {
	       ArrayList<Suk_files> n = dao.getpicture(num);
	      return n;
	   }

   // 사진 번호
   @Override
   public Suk_files getpic(int num) {
	   Suk_files files = dao.getpic(num);
	   return files;
   }

	@Override
	public SUM star(int num) {
		SUM sum = dao.star(num);
		return sum;
	}

}


	


