package net.softsociety.exam.service;

import java.util.ArrayList;

import net.softsociety.exam.domain.MSG;

public interface MSGService {

	int sendMSG(MSG msg);

	ArrayList<MSG> findMSG(String receiver_mem_id);

	

}
