package jpabook.jpashop.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext  // 스프링 부트가 어노테이션에 있으면 아래 매니저를 주입해준다.
    private EntityManager em;

    //저장하는 코드
    public Long save(Member member) {
        em.persist(member);     // (커맨드와 쿼리를 분리하자)
        return member.getId();  // (아이디를 조회 할 수 있도록 설계) 저장을 하고나면 사이드임팩트를 일으키는 커맨드 성이기에 리턴값안 만듬
    }

    //조회 해보기
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
