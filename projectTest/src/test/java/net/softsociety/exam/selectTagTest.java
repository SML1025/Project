package net.softsociety.exam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.softsociety.exam.dao.SuksoDAO;
import net.softsociety.exam.domain.SukNumCount;
import net.softsociety.exam.service.SuksoService;

@SpringBootTest
public class selectTagTest {

	@Autowired
	SuksoDAO dao;
	
	@Test
	public void suksoTest() {
		List<String> list = new ArrayList<>();
		list.add("해변");
		list.add("모래사장");
//		suksoService.selectByTags(0, 0, "강원도", "바다", list);
		HashMap<String, Object> map = new HashMap<>();
	    map.put("tags", list);
	    map.put("suk_location", "강원도");
	    map.put("suk_nearby", "바다");
		RowBounds rb= new RowBounds(0, 5);
	    List<SukNumCount> SukList = dao.selectSuksoList(map, rb);
		System.out.println("SukList :" +  SukList);
	}
}
