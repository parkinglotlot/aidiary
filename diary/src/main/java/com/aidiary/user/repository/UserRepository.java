package com.aidiary.user.repository;

import com.aidiary.diary.jpa.Diary;
import com.aidiary.user.jpa.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginId(String loginId);

  //List<User> findByPasswordAndLoginId(String loginId, String password);

  List<User> findByLoginIdAndPassword(String LoginId, String Password);

  //회원가입 시 해당 정보를 바탕으로 정보 insert
  //boolean save(User user);

  //아이디로 유저 찾기
  User getUserByLoginId(String loginId);
}
