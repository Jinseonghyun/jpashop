package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

//@RunWith(SpringRunner.class)  // JUnit4 한테 알려준다. spring이랑 관련된 걸로 테스트할거야
@ExtendWith(SpringExtension.class) // // JUnit5 한테 알려준다. spring이랑 관련된 걸로 테스트할거야
@SpringBootTest //  스프링 부트로 테스트 돌린다.
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // 테스트가 끝난다음에 db를 롤백해버림 그래서 데이터가 없어짐 (데이터가 있으면 반복적인 테스트 불가해서)
    @Rollback(false) // Transactional 있으면 무조건 롤백하기에 false로 지정해서 롤백안하고 바로 커밋해버리게 설정
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member); // 저장된 id 뽑고
        Member findMember = memberRepository.find(savedId); // 맞는지 확인

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId()); // findMember 와 member 의 id가 같다.
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername()); // findMember 와 member 의 username이 같다.
        Assertions.assertThat(findMember).isEqualTo(member); //  저장한거랑 조회한거랑 같다.  findMember == member (true)

    }
}