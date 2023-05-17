package net.softsociety.exam.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.exam.domain.Bung;
import net.softsociety.exam.domain.BungComment;
import net.softsociety.exam.domain.Sukso;

@Mapper
public interface BungDAO {

	int bungWrite(Bung bung);

	ArrayList<Bung> list();

	int bungWriteComm(BungComment bungComment);

	Bung getBungByNum(int bung_num);

	Sukso getAddress(int suk_num);

	Bung getBungBySukNum(int suk_num);

}
