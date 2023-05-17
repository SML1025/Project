package net.softsociety.exam.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.exam.domain.Tags;

@Mapper
public interface TagDAO {
	
	public int insert(Tags tags);

	public int insert2(ArrayList<Integer> taglist);
	
	public ArrayList<Tags> selectAll();

	public Tags selectOne(String s);

}
