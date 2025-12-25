package com.aidiary.diary;

import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.jpa.User;
import com.aidiary.user.repository.UserRepository;
import com.aidiary.user.util.SecurityConfig;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class DiaryApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DiaryApplicationTests.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SecurityConfig securityConfig;



	@Test
	void contextLoads() {
	}

	@Test
	void Test1(User user){
		Optional<User> user1 = userRepository.findByLoginId("ujin2597");
		if(user1.isPresent()){
			User u = user1.get();
			log.info(u.getLoginId());
		}else{
			log.info("유저없음");
		}

	}

	@Test
	void Test2(){
		List<User> users = userRepository.findByLoginIdAndPassword("ujin2597","1234");
		if(users.size() > 0){
			for(User u : users){
				log.info("FullName:{}",u.getFullName());
			}
		}else{
			log.info("유저 없음");
		}
	}

	@Test
	void Test3(){
		String hashedPassword = securityConfig.passwordEncoder().encode("1234");
		log.info("비밀번호 : {}",hashedPassword);
	}



//	@Test
//	void Test{
//		List<User> users = userRepository.findByPasswordAndLoginId("ujin2597","1234");
//		if(users.size() > 0){
//			for(User u : users){
//				log.info(u.getFullName());
//			}
//		}
//	}

	@Test
	@Transactional
	void MembershipTest() throws ParseException {
		User user = new User();
		user.setLoginId("ujin");
		user.setPassword("1234");
		user.setEmail("ahn519@naver.com");
		user.setFullName("AhnYoujin");

		DateTimeFormatter ldt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		user.setBirthday(LocalDate.parse("1996-07-25",ldt));

		userRepository.save(user);

		Optional<User> isLoginUser = userRepository.findByLoginId(user.getLoginId());

		if(isLoginUser.isPresent()) log.info("성공");
		else log.info("실패");


	}

	//유효성 검사
	@Transactional
	@Test
	void validationTest(){

		//로그인 아이디 : 영문 포함 8자
		//패스워드 : 4자 이상 12자 이하
		MemberShipDTO member = new MemberShipDTO().builder()
				.loginId("ujin9607")
				.password("1234")
				.verifyPassword("1234")
				.birthday(LocalDate.parse("1996-07-25",DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();

		boolean valTest = false;

		boolean emptyLoginId =  !(member.getLoginId() == null || member.getLoginId().isEmpty());
		boolean notLoginIdMethod = false;
		boolean duplicateLoginId = false;
		boolean emptyPassword = !(member.getPassword() == null || member.getPassword().isEmpty());
		boolean notPasswordMethod = false;
		boolean verifyPassword = false;
		boolean emptyBirthday = false;
		boolean isBirthdayAge = false;


		if (!emptyLoginId) log.info("아이디를 입력해주세요.");


		if(emptyLoginId){
			if (!member.getLoginId().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,12}$")) log.info("아이디는 8자에서 12자 이상 영문자와 숫자를 혼합한 형태여야 합니다.");
			else notLoginIdMethod = true;
		}

		if(notLoginIdMethod){
			Optional<User> duplicateUser =  userRepository.findByLoginId(member.getLoginId());
			if(duplicateUser.isPresent()) log.info("아이디가 중복됩니다.");
			else duplicateLoginId = true;
		}


		if (!emptyPassword) log.info("비밀번호를 입력하세요.");


		if (emptyPassword){
			if(!(member.getPassword().length() >= 4 && member.getPassword().length() <=8)) log.info("비밀번호는 4자이상 8자이하여야 합니다.");
			else notPasswordMethod = true;
		}

		if(notPasswordMethod){
			if(!member.getVerifyPassword().equals(member.getPassword())) log.info("비밀번호가 일치하지 않습니다.");
			else verifyPassword = true;
		}

		if(member.getBirthday() == null) log.info("생년월일을 입력해주세요.");
		else emptyBirthday = true;

		if(emptyBirthday){
			if(!LocalDate.now().minusYears(18).isAfter(member.getBirthday())) log.info("만 18세 이상만 가입 가능합니다.");
			else isBirthdayAge = true;
		}

//		boolean emptyLoginId =  member.getLoginId() == null || member.getLoginId().isEmpty();
//		boolean notLoginIdMethod = false;
//		boolean duplicateLoginId = false;
//		boolean emptyPassword = member.getPassword() == null || member.getPassword().isEmpty();
//		boolean notPasswordMethod = false;
//		boolean verifyPassword = false;
//		boolean emptyBirthday = false;
//		boolean isBirthdayAge = false;

		log.info("emptyLoginId:{}",emptyLoginId);
		log.info("notLoginIdMethod:{}",notLoginIdMethod);
		log.info("duplicateLoginId:{}",duplicateLoginId);
		log.info("emptyPassword:{}",emptyPassword);
		log.info("notPasswordMethod:{}",notPasswordMethod);
		log.info("notPasswordMethod:{}",member.getPassword());
		log.info("verifyPassword:{}",verifyPassword);
		log.info("emptyBirthday:{}",emptyBirthday);
		log.info("isBirthdayAge:{}",isBirthdayAge);

		if(emptyLoginId && notLoginIdMethod && duplicateLoginId && emptyPassword  && notPasswordMethod && verifyPassword && emptyBirthday && isBirthdayAge) {valTest = true;}

		if(valTest)log.info("검증 통과");
		else log.info("검증 실패");



	}

}
