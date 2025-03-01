package com.am.design.development.sabestore;

import com.am.design.development.SabeTest;
import com.am.design.development.data.defaultdb.repository.TestRepository;
import com.am.design.development.data.userdb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SabeTest
class SabeStoreApplicationTests {

	@Autowired
	UserRepository userRepository;
	@Autowired
	TestRepository testRepository;

	@Test
	void contextLoads() {
		System.out.println(userRepository.findAll());
		System.out.println(testRepository.findAll());
	}

}
