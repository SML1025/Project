package net.softsociety.exam.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.dao.ReservationDAO;
import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.bookSukso;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	ReservationDAO dao;
	@Override
	public int insertRes(Reservation res) {
		int n= dao.insertRes(res);
		return n;
	}
	@Override
	public ArrayList<bookSukso> checkbook(String mem_id) {
		
		ArrayList<bookSukso> n=dao.checkbook(mem_id);
		log.debug("bs:{}", n);
		return n;
	}
	@Override
	public ArrayList<Reservation> findRes(String mem_id) {
		 ArrayList<Reservation> res = dao.findRes(mem_id);
		return res;
	}
	@Override
	public int updateRes(Reservation res) {
		int n=dao.updateRes(res);
		return n;
	}
	@Override
	public int cancer(int num) {
		int n=dao.deleteRes(num);
		return n;
	}
	@Override
	public int countRes(String mem_id) {
		int n=dao.countRes(mem_id);
		return n;
	}

	@Override
	public boolean checkdate(Reservation res) {
	Reservation res1=dao.checkdate(res);
	return res1 != null;
	}
	
	@Override
	public Reservation getReservationByMemid(String mem_id) {
		Reservation reservation = dao.getReservationByMemid(mem_id);
		return reservation;
	}
	
	@Override
	public boolean checkdate2(Reservation res) {
	   Reservation res2 = dao.checkdate2(res);
	   return res2 != null;
	}
	@Override
	public ArrayList<Reservation> getAllRes() {
		ArrayList<Reservation> res = dao.getAllRes();
		return res;
	}
		
	@Override
	public String getNow() {
		String now = dao.getNow();
		return now;
	}

	@Override
	public int updateTo1(int res_num) {
		int update = dao.updateTo1(res_num);
		return update;
	}
	@Override
	public int updateTo2(int res_num) {
		int update = dao.updateTo2(res_num);
		return update;
	}
	@Override
	public int updateTo3(int res_num) {
		int update = dao.updateTo3(res_num);
		return update;
	}
	
   @Override
   public ArrayList<bookSukso> findRes2(String mem_id) {
      ArrayList<bookSukso> res=dao.findRes2(mem_id);
      return res;
   }
   @Override
   public ArrayList<bookSukso> checkbook2(String mem_id) {
      ArrayList<bookSukso> res=dao.checkbook2(mem_id);
      return res;
   }
	
   @Override
   public int ResMsg(int suk_num, String mem_id) {
      HashMap<String, Object> map = new HashMap<>();
      map.put("suk_num", suk_num);
      map.put("mem_id", mem_id);
      int n= dao.ResMsg(map);
      return n;
   }
   
}
