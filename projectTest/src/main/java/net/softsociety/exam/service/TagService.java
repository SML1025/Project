package net.softsociety.exam.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.dao.TagDAO;
import net.softsociety.exam.domain.Tags;

@Slf4j
@Service
public class TagService {
	
	@Autowired
	TagDAO dao;
	
	
	public int insert(Tags tags) {
		int n = dao.insert(tags);
		return n;
	}
	
	public int insert2(ArrayList<Integer> taglist) {
		int n = dao.insert2(taglist);
		return n;
	}

	public ArrayList<Tags> selectAll() {
		log.debug("서비스 실행");
		ArrayList<Tags> list = dao.selectAll();
		return list;
	}

	public Tags selectOne(String s) {
		Tags res = dao.selectOne(s);
		return res; 
	}
	
	
}
