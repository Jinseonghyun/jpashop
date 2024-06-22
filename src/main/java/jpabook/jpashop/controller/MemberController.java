package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // 데이터를 실어서 보낸다.
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) { // 자바 표준 어노테이션 Valid  -> validation 쓰게 해준다. // BindingResult 은 왼쪽 폼에서 에러가 나면 원래 팅기지만 그 에러를 BindingResult 통해 담아서 아래 코드 실행되게 해줌

        if (result.hasErrors()) {  // 만약 에러가 있으면
            return "members/createMemberForm"; // 다시 가입 페이지로 돌아간다.
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";  // 첫번 째 페이지로 넘겨버린다.
    }
}
