package com.csu.bakery;

import com.csu.bakery.persistence.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BakeryApplicationTests {

	@Autowired
	private CategoryMapper categoryMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void test(){

	}

}
