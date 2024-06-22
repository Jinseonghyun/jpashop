package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링이 제공하는 service 어노테이션
@Transactional(readOnly = true) // JPA 의 모든 데이터 변경이나, 어떤 로직들은 가급적이면 트레젝션 안에서 다 실행되어야 한다. (아래 메서드들은 모두 트레젝션 안에 걸려 들어간다.)(join, findAll, findOne)
//@AllArgsConstructor // 롬복 아래 필드를 가지고 아래의 생성자를 자동으로 만들어줌
@RequiredArgsConstructor // final 이 붙은 필드만을 가지고 아래의 생성자를 자동으로 만들어줌
public class MemberService {

    //@Autowired  // 필드 인젝션 (단점 : 테스트 할 때 바꿔야 하는 상황도 올 수 있는데 못 바꾼다.) 그래서 사용한 아래 setter 인젝션
    private final MemberRepository memberRepository; // 아래에 생성자 인젝션을 사용함으로 변경할 일이 없다 final 을 권장!! -> 컴파일 시점에서 생성자 안해주면 알려준다.

//    @Autowired  // setter 인젝션 (장점 : 테스트 코드 작성할 때 목 같은 걸 직접 주입할 수 있다. , 필드는 그냥 주입하기가 되게 까다롭다.) // (단점 : 런타임에 실제 애플리케이션 돌아가는 시점에 누군가가 이걸 바꿀 수 있다.)
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    // 생성자가 하나일 때 생략도 가능
//    //@Autowired  // 그래서 위의 필드와, setter 주입 말고 요즘 사용하는 생성자 인젝션
//    public MemberService(MemberRepository memberRepository) { // 스프링이 뜰 때 생성자에서 인젝션을 해준다. // 의존성을 명확히 알 수 있다. //
//        this.memberRepository = memberRepository; // 생성자 인젝션은 한번 생성할 때 이게 다 끝나버리기 때문에 중간에 셋 해서 이걸 바꿀 수가 없다. // 테스트 케이스를 할 때 직접 주입을 해줘야 한다고 컴파일이 알려줘서 잘 챙길 수 있다.
//    }

    /**
     * 회원가입
     */
    // 회원 가입
    @Transactional  // 여기는 readOnly = true 하면 안됨 , 데이터 변경이 안됩니다. // Transactional 위의 기본값과 다른 false 이기에 우선권 가질 수 있도록 새로 설정해놓음
    public Long join(Member member) {

        validateDuplicateMember(member); // 같은 이름 방지 (중복 회원 검증)
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());  // 멀티스레드 상황에서 동시에 두명의 같은 이름이 가입을 하면 문제가 생길 수 있다.
        if (!findMembers.isEmpty()) {                                              // 데이터베이스에 member 의 name 을 유니크 제약 조건으로 잡아주는 것이 좋다. (위의 문제 방지)
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    //@Transactional(readOnly = true)  // JPA 가 좀 더 성능 최적화 한다. (젤 위에서 기본값으로 메서드에 해줌으로써 지워준다.)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 한건만 조회
    //@Transactional(readOnly = true)  // JPA 가 좀 더 성능 최적화 한다.
    public Member findOne(Long memberid) {
        return memberRepository.findOne(memberid);
    }
}
