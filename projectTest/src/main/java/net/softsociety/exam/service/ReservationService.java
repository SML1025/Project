package net.softsociety.exam.service;

import java.util.ArrayList;

import net.softsociety.exam.domain.Reservation;
import net.softsociety.exam.domain.bookSukso;

public interface ReservationService {

	int insertRes(Reservation res);

	ArrayList<bookSukso> checkbook(String mem_id);

	ArrayList<Reservation> findRes(String mem_id);

	int updateRes(Reservation res);

	int cancer(int num);

	int countRes(String mem_id);
	
	boolean checkdate(Reservation res);
	
   ArrayList<bookSukso> findRes2(String mem_id);

   ArrayList<bookSukso> checkbook2(String mem_id);
	
	Reservation getReservationByMemid(String mem_id);

	boolean checkdate2(Reservation res1);

	ArrayList<Reservation> getAllRes();

	String getNow();

	int updateTo1(int res_num);
	
	int updateTo2(int res_num);
	
	int updateTo3(int res_num);

	int ResMsg(int suk_num, String mem_id);
}
