package net.softsociety.exam.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;


import net.softsociety.exam.domain.MSG;

@Mapper
public interface MSGDAO {


	int sendMSG(MSG msg);

	ArrayList<MSG> findMSG(String receiver_mem_id);

}
