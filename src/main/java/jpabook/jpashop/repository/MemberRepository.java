package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링에서 제공하는 @Component scan 의 대상이 되서 자동으로 스프링 빈으로 관리됨
public class MemberRepository {

    @PersistenceContext //JPA 표준 어노테이션
    private EntityManager em; // JPA 에서 @PersistenceContex 스프링이 생성한 매니저를 EntityManager 에 주입해줌

    // 저장 로직
    public void save(Member member) {
        em.persist(member);  // persist 하면 여기서도 member 객체를 넣는다. // 트래젝션이 커밋되는 시점에 반영됨
    }

    // 조회 로직 (단건 조회)
    public Member findOne(Long id) {
        return em.find(Member.class, id);  // JPA 가 제공하는 find 메서드가 멤버를 찾아서 반환해줌  (타입, pk)
    }

    // 회원 목록 조회 (리스트 조회)
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // 전부 다 찾을 때는 em.createQuery 라는 JPQL 을 작성해야함 (JPQL, 반환타입)
                .getResultList();   // 멤버를 리스트로 만들어줌  // return 한번에 합치는 단축키 (ctrl + alt + N)  // JPQL 는 테이블이 아닌 엔티티(객체)를 대상으로 쿼리
    }

    // 이름으로 회원 검색 (멤버 검색)
    public List<Member> findByName(String name) { // 파라미터를 name 으로 넘기면
        return em.createQuery("select m from Member m where m.name = :name", Member.class)  // :name 파라미터 바인딩 해주고 , 조회 타입은 멈버 클래스
                .setParameter("name", name) // 파라미터 바인딩 해주고
                .getResultList();
    }

}
