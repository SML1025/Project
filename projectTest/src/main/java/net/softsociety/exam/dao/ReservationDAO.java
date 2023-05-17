package net.softsociety.exam.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.bookSukso;

@Mapper
public interface ReservationDAO {

	int insertRes(Reservation res);

	ArrayList<bookSukso> checkbook(String mem_id);

	ArrayList<Reservation> findRes(String mem_id);

	int updateRes(Reservation res);

	int deleteRes(int num);

	int countRes(String mem_id);

	Reservation checkdate(Reservation res);

	Reservation getReservationByMemid(String mem_id);

	Reservation checkdate2(Reservation res);

	String getNow();

	ArrayList<Reservation> getAllRes();

	int updateTo1(int res_num);

	int updateTo2(int res_num);
	
	int updateTo3(int res_num);

	ArrayList<bookSukso> findRes2(String mem_id);

	ArrayList<bookSukso> checkbook2(String mem_id);

	int ResMsg(HashMap<String, Object> map);
}
