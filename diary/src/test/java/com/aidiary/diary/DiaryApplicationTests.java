package com.aidiary.diary;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.diary.mapper.DiaryMapper;
import com.aidiary.diary.mapper.DiaryRepository;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.jpa.User;
import com.aidiary.user.repository.UserRepository;
//import com.aidiary.user.util.SecurityConfig;
import com.aidiary.user.service.UserService;
import com.aidiary.user.util.Util;
import com.aidiary.user.util.UtilService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.security.Key;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootTest
class DiaryApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DiaryApplicationTests.class);

	@Autowired
	private Util util;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DiaryRepository diaryRepository;

//	@Autowired
//	private SecurityConfig securityConfig;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String emailFrom;
  @Autowired
  private HttpSession httpSession;

	@Autowired
	private DiaryMapper diaryMapper;


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
	void Test3() throws Exception {
//		Key keySpec = util.getAESKey();


		String encrypt = util.encAES("1234");
		String decrypt = util.deAES(encrypt);

		log.info("암호화:{}", encrypt);
		log.info("복호화:{}",decrypt);

//		String hashedPassword = securityConfig.passwordEncoder().encode("1234");
//		log.info("비밀번호 : {}",hashedPassword);
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

	@Transactional
	@Test
	void sendMailTest(){
		//난수 생성

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 8; i++){
			int checkNum = (int)(Math.random() * 10);
			char randomChar = (char)(checkNum + '0');
			sb.append(randomChar + checkNum);
		}

		String verifyNumber = sb.toString();

		//이메일 보내기
		String title = "인증번호 메일 보냅니다.";

		String content = "인증번호는" + "<br></br> <strong>" + verifyNumber + "</strong> <br></br>" + "입니다. 해당 인증 번호를 확인란에 입력해주세요.";

		try {
			MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true,"UTF-8");



			mimeMessageHelper.setFrom(emailFrom);
			mimeMessageHelper.setTo("gwang11167@naver.com");
			mimeMessageHelper.setSubject(title);
			mimeMessageHelper.setText(content, true);
			javaMailSender.send(mimeMailMessage);

		} catch (MessagingException e) {
      throw new CustomException("메일 발송에 실패했습니다.");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증번호 발송에 실패했습니다.");
		}

		// 이메일 보낸 뒤 인증번호 세션에 저장
		HttpSession session = new HttpSession() {
			@Override
			public long getCreationTime() {
				return 0;
			}

			@Override
			public String getId() {
				return "";
			}

			@Override
			public long getLastAccessedTime() {
				return 0;
			}

			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public void setMaxInactiveInterval(int i) {

			}

			@Override
			public int getMaxInactiveInterval() {
				return 0;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public void invalidate() {

			}

			@Override
			public boolean isNew() {
				return false;
			}
		};

		session.setAttribute("VERIFY_NUMBER",false);
		session.setAttribute("verifyNumber",verifyNumber);


	}

	@Test
	void sendMailTest2(){

		HttpSession session = new HttpSession() {
			@Override
			public long getCreationTime() {
				return 0;
			}

			@Override
			public String getId() {
				return "";
			}

			@Override
			public long getLastAccessedTime() {
				return 0;
			}

			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public void setMaxInactiveInterval(int i) {

			}

			@Override
			public int getMaxInactiveInterval() {
				return 0;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public void invalidate() {

			}

			@Override
			public boolean isNew() {
				return false;
			}
		};

		String verifyNumber = "";
		if(session.getAttribute("verifyNumber") == verifyNumber){
			log.info("맞습니다.");
		}else{
			log.info("틀립니다.");
		}
	}

	@Test
	@Transactional
	void insertDiary(){
//		PageRequestDTO pageRequestDTO = new PageRequestDTO().builder()
//				.page(1)
//				.pageSize(10)
//				.build();

		LocalDateTime localDateTime = LocalDateTime.now();
		User youjin = userRepository.getUserByLoginId("ujin2597");

		for(int i = 0; i < 11; i++){
			Diary diary = new Diary("title" + i,"content" + i,localDateTime);
			diary.setWriter(youjin);
			boolean isInsert = diaryMapper.isInsertDiaryList(diary) != 0;
			if (!isInsert){
				log.info("실패");
				break;
			}
		}
	}

	// diary 리스트 가져오기
	@Test
	void readDiaries(){
		User user = userRepository.getUserByLoginId("ujin2597");
		List<Diary> diaryList =  diaryMapper.selectRequestPaginationList(new PageRequestDTO(3,10),user);

		for(Diary o : diaryList){

			log.info("diary title : {}",o.getTitle());
		}

	}

	//diary 업데이트
	@Test
	@org.springframework.transaction.annotation.Transactional
	void updateDiary(){
		Optional<User> user = userRepository.findByLoginId("ujin2597");
		Diary diary = diaryRepository.getDiaryById(1L);

		User getUser = user.orElse(null);

		diary.setContent(diary.getContent() + "업데이트");
		diary.setTitle(diary.getTitle() + "업데이트");

		int updateNum = diaryMapper.updateDiary(diary,getUser);
    try {
      if(updateNum != 0){
        log.info("업데이트 성공");
      } else{
        log.info("실패");
      }
    } catch (Exception e) {
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			CustomResponseEntity customResponseEntity = new CustomResponseEntity().builder()
					.message(httpStatus.getReasonPhrase())
					.code(httpStatus.value())
					.httpStatus(httpStatus)
					.build();
      throw new CustomException(customResponseEntity,httpStatus);
    }


  }

}
