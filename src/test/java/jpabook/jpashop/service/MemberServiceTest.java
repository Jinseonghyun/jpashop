package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class) // JUnit4 한테 알려준다. spring이랑 관련된 걸로 테스트할거야
@ExtendWith(SpringExtension.class) // JUnit5 한테 알려준다. Junit5 실행할 때 스프링이랑 엮어서 실행할래 라는 의미
@SpringBootTest // 스프링부트 띄운상태에서 테스를를 할라면 있어야함 // 없으면 @Autowired 실패한다.
@Transactional // 데이터 변경해야 하기 때문에 이게 있어야 롤백이 된다.  // 계속 테스트 해야하니까 여기서 롤백으로 데이터 초기화해주는것
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em; // 롤백이지만 그래도 일단 db에 쿼리 날리는거 보고 싶다면 해주자

    @Test
    //@Rollback(false)  // 눈으로 보고 싶으면 false 해주면 롤백을 안하고 커밋을 해서 등록 쿼리를 볼 수 있다.
    public void 회원가입() throws Exception {
        //gitven
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);// join 을 하면 id 가 나온다.

        //then    // Transactional 해줬기 때문에 가능 (jpa 에서 같은 트랜잭션 안에서 같은 엔티티, 그러니까 id값이 똑같으면 pk값이 똑같으면 애는 같은 영속성 컨텍스트에서 관리가 된다.) 딱 1개로 관리됨
        //em.flush(); // 이렇게 하면 위에 join(member) 가 쿼리로 db에 반영된다. (영속성 컨텍스트에 있는 쿼리를 강제로 날린다.)
        assertEquals(member, memberRepository.findOne(savedId)); // member 와 memberRepository에 찾아온 member값와 같아야한다.
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when    // JUnit5 에서 아래 번잡한 try , catch 문을 아래 assertThrows 로 대체 가능
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

//        memberService.join(member1);
//        try {
//            memberService.join(member2); // 예외가 발생해야 한다.! -> 같은 이름을 두번 넣음 -> try 로 예외처리하자
//        } catch (IllegalStateException e) { // 에러 나면 (catch 못하면) IllegalStateException 타고 return 으로 나간다.
//            return;
//        }

        //then
        //fail("예외가 발생해야 한다."); // 코드가 돌다가 여기오면 뭔가 잘못 된 거다.

    }

}