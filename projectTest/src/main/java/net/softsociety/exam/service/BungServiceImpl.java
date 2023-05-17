package net.softsociety.exam.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.dao.BungDAO;
import net.softsociety.exam.domain.Bung;
import net.softsociety.exam.domain.BungComment;
import net.softsociety.exam.domain.Sukso;

@Slf4j
@Service
public class BungServiceImpl implements BungService {
	@Autowired
	BungDAO dao;
	

	@Override
	public int bungWrite(Bung bung) {
		int result = dao.bungWrite(bung);
		return result;
	}

	@Override
	public ArrayList<Bung> list() {
		ArrayList<Bung> list = dao.list();
		return list;
	}

	@Override
	public int bungWriteComm(BungComment bungComment) {
		int result = dao.bungWriteComm(bungComment);
		return result;
	}

	@Override
	public ArrayList<Bung> bungSearch(String bung_cate) {

		return null;
	}

	@Override
	public Bung getBungByNum(int bung_num) {
		Bung result=dao.getBungByNum(bung_num);
		return result;
	}

	@Override
	public Sukso getAddress(int suk_num) {
		Sukso result=dao.getAddress(suk_num);
		return result;
	}
	

   @Override
   public Bung getBungBySukNum(int suk_num) {
      Bung result=dao.getBungBySukNum(suk_num);
      return result;
   }

}
