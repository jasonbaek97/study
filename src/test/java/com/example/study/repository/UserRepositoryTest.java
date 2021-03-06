package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;      // Optional 객체 사용 read()

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired  // Spring DI(Dependency Injection) , 객체를 만들지 않고 Spring 이 의존성을 주입 시킴
    private UserRepository userRepository;

    @Test
    public void create(){

        String account = "Test05";
        String password = "test05";
        String status = "REGISTERED";
        String email = "Test05@naver.com";
        String phoneNumber = "010-1111-5555";
        LocalDateTime registeredAt = LocalDateTime.now();

        // @Builder 패턴으로, 필요한 변수만 추가하여 객체를 생성 IoC
        // 클래스.builer().필요한 변수명 함수 .build()
        User user = User.builder()
                .account(account)
                .password(password)
                .status(UserStatus.REGISTERED)
                .email(email)
                .phoneNumber(phoneNumber)
                .registeredAt(registeredAt)
                .build();

        User newUser = userRepository.save(user);

        Assert.assertNotNull(newUser);

    }

    @Test
    @Transactional
    public void read(){

        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1111-2222");

        if(user != null){
            user.getOrderGroupList().stream().forEach(orderGroup -> {
                System.out.println("-------------- 주문 묶음 ---------------");
                System.out.println("수령인 : "+orderGroup.getRevName());
                System.out.println("수령지 : "+orderGroup.getRevAddress());
                System.out.println("총금액 : "+orderGroup.getTotalPrice());
                System.out.println("총수량 : "+orderGroup.getTotalQuantity());

                System.out.println("-------------- 주문 상세 ---------------");
                orderGroup.getOrderDetailList().forEach(orderDetail -> {
                    System.out.println("파트너사 이름 : "+orderDetail.getItem().getPartner().getName());
                    System.out.println("파트너 카테고리 : "+orderDetail.getItem().getPartner().getCategory().getTitle());
                    System.out.println("주문 상품 : "+orderDetail.getItem().getName());
                    System.out.println("고객센터 번호 : "+orderDetail.getItem().getPartner().getCallCenter());
                    System.out.println("주문 상태 : "+orderDetail.getStatus());
                    System.out.println("도착예정일자 : "+orderDetail.getArrivalDate());


                });
            });
        }
        Assert.assertNotNull(user);

    }

    @Test
    public void update(){
        Optional<User> user = userRepository.findById(2L);

        user.ifPresent(selectUser->{
            selectUser.setAccount("PPPP");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("updated method()");

            userRepository.save(selectUser);
            // create 와 동일하게 사용했지만, id가 존재하면 JPA 가 update 하고 없으면 insert 하기 때문에 가능
        });

    }

    @Test
    @Transactional      // Test 코드는 실행되지만, Database 수행결과는 Rollback
    public void delete(){
        Optional<User> user = userRepository.findById(4L); // id로 user를 찾음

        Assert.assertTrue(user.isPresent());       // junit Assert 사용하여, 삭제 대상 존재여뷰 체크

        user.ifPresent(selectUser->{
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(4L);

        Assert.assertFalse(deleteUser.isPresent());     // junit Assert 사용하여, 삭제된 대상 존재여뷰 체크

        //  다른 형태로 조회된 데이터가 있는지 체크 하는 로직
//        if(deleteUser.isPresent()){     // 데이터가 존재하는지 체크
//            System.out.println("데이터가 존재 : "+deleteUser.get());
//        }else {
//            System.out.println("데이터 존재 하지 않음");
//        }
    }
}

