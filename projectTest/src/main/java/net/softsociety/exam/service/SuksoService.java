package net.softsociety.exam.service;

import java.util.ArrayList;
import java.util.List;

import net.softsociety.exam.domain.Intag;
import net.softsociety.exam.domain.Member;
import net.softsociety.exam.domain.Review;
import net.softsociety.exam.domain.SUM;
import net.softsociety.exam.domain.Suk_files;
import net.softsociety.exam.domain.Sukso;
import net.softsociety.exam.domain.Sukso_spec;
import net.softsociety.exam.domain.Tags;
import net.softsociety.exam.util.PageNavigator;



public interface SuksoService {

	int insertsukso(Sukso sukso);

	Sukso read(int num);

	Sukso selectSukso(int num);

	PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String suk_location, String suk_nearby, List<String> tagList);

//	ArrayList<Sukso> suksolist(int startRecord, int countPerPage, String suk_location, String suk_nearby, List<String> tagList);

	List<Sukso> selectByTags(int pagePerGroup, int countPerPage, String suk_location, String suk_nearby, List<String> tagList);

	Member find(String mem_id);

	int writeReview(Review review);

	ArrayList<Review> readReview(int startRecord, int countPerPage, int num);

	Review readRev(int num);

	PageNavigator getPageNavigator1(int pagePerGroup, int countPerPage, int page, int num);

	int insertspec(Sukso_spec spec);
	
	boolean rescheck(String mem_id, int suk_num);
	
	public int insert(Tags tags);
	
	public int insert2(Intag i);

	public int insert3(Intag i);
	
	// 성호씨
	Sukso_spec sukso_spec(int num);

	Member member(String mem_id);

	//
	public List<Intag> getIntagList();

	public List<Tags> getTagsList();
	
	public List<Intag> getIntagListBySuk(int suk_id);

	public List<Tags> getTagsListByRev(int suk_num);

	public List<Tags> getTagsListByHost(int suk_num);

	boolean checkRes(String mem_id);

	public int picture(Suk_files sf);

	public Suk_files getpic(int num);

	public ArrayList<Suk_files> getpicture(int num);

	int searchCount(String suk_location, String suk_nearby, List<String> tagList);

	SUM star(int num);











	

}
