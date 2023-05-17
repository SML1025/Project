package net.softsociety.exam.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.exam.controller.MsgController;
import net.softsociety.exam.dao.MSGDAO;
import net.softsociety.exam.domain.MSG;

@Slf4j
@Service
public class MSGServiceImpl implements MSGService {
	@Autowired
	MSGDAO dao;

	@Override
	public int sendMSG(MSG msg) {
		int n=dao.sendMSG(msg);
		return n;
	}

	@Override
	public ArrayList<MSG> findMSG(String receiver_mem_id) {
		ArrayList<MSG> msg = dao.findMSG(receiver_mem_id);
		return msg;
	}

}