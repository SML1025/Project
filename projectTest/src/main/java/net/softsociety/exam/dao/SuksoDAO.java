package net.softsociety.exam.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

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


@Mapper
public interface SuksoDAO {

	int insertsukso(Sukso sukso);

	Sukso selectSukso(int num);

	int count(HashMap<String, Object> map);

//	ArrayList<Sukso> selectSuksoList(HashMap<String, String> map, RowBounds rb);

	ArrayList<SukNumCount> selectSuksoList(HashMap<String, Object> map, RowBounds rb);
	
	Member find(String mem_id);

	int writeReview(Review review);

	ArrayList<Review> readReview(int num, RowBounds rb);

	Review readRev(int num);

	int rev_count(int num);

	int insertspec(Sukso_spec spec);


	// 태그 쪽
	
	public ArrayList<Tags> selectAll();

	public Tags selectOne(String s);
	
	public int insert(Tags tags);
	
	public int insert2(Intag i);
	
	public int insert3(Intag i);
	
	// 상명씨 추가
	public ArrayList<Reservation> rescheck(HashMap<String, Object> map);

	// 성호씨
	Sukso_spec selectSpec(int num);

	Member selectMember(String mem_id);

	public List<Intag> getIntagList();

	public List<Tags> getTagsList();
	
	public List<Intag> getIntagListBySuk(int suk_id);

	public List<Tags> getTagsListByHost(int suk_num);

	public List<Tags> getTagsListByRev(int suk_num);

	ArrayList<Reservation> checkRes(String mem_id);

	List<Sukso> selectSuksoList(HashMap<String, Object> map);

	public int picture(Suk_files sf);

	public Suk_files getpic(int num);

	public ArrayList<Suk_files> getpicture(int num);

	public int searchCount(HashMap<String, Object> map);

	public SUM star(int num);

	Sukso getSuksoBySukNUm(int suk_num, RowBounds rb);


	
	
}
