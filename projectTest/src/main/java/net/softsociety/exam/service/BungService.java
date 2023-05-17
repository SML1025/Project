package net.softsociety.exam.service;

import java.util.ArrayList;

import net.softsociety.exam.domain.Bung;
import net.softsociety.exam.domain.BungComment;
import net.softsociety.exam.domain.Sukso;

public interface BungService {

	int bungWrite(Bung bung);

	ArrayList<Bung> list();

	int bungWriteComm(BungComment bungComment);

	ArrayList<Bung> bungSearch(String bung_cate);

	Bung getBungByNum(int bung_num);

	Sukso getAddress(int suk_num);

	Bung getBungBySukNum(int suk_num);

}
