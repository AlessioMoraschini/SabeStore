package com.am.design.development.sabestore;

import com.am.design.development.SabeTest;
import com.am.design.development.data.defaultdb.repository.TestRepository;
import com.am.design.development.data.userdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@SabeTest
@RequiredArgsConstructor
class SabeStoreApplicationTests {

	private final UserRepository userRepository;
	private final TestRepository testRepository;

	@Test
	void contextLoads() {
		System.out.println(userRepository.findAll());
		System.out.println(testRepository.findAll());
	}

}
