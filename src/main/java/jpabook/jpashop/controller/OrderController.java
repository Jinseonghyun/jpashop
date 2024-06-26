package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    /**
     *  상품 주문
     */

    //고객이랑 아이템이랑 다 선택할 수 있어야한다.
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        //화면은 회원이랑, 상품 선택 할 수 있다.
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";

    }

    @PostMapping("/order")        // 식별자만 넘겨주는 방식
    public String order(@RequestParam("memberId") Long memberId,  // orderForm.html안에 데이터를 연결해주기 위해 select 의 name 의 값과 이름 맞추어준다.
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        // 핵심비즈니스 로직은 안에서 돌아간다.
        orderService.order(memberId, itemId, count); // 어떤 고객이, 어떤 아이템을, 몇개를 주문할거야
        return "redirect:/orders"; // 주문 내용 목록
    }

    /**
     * 주문 내역
     */
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
//        model.addAttribute("orderSearch", orderSearch); 이 코드를 위에서 자동으로 해주는 것

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
