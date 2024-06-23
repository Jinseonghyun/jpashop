package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 목록
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    /**
     *
     * 상품 수정 폼
     */
    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     *
     * 상품 수정
     */
    @GetMapping("items/{itemId}/edit") // {itemId} path variable 사용한다 -> 상품마다 달라지는 변형에 따라가야 하기 때문
    public String updataItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 원래 Item 타입이지만 단순하게 하기 위해 Book 으로 캐스팅

        BookForm form = new BookForm(); // 업데이트 할때 Entity 가 아니라 Bookform 을 보낼거다.
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

//    @PostMapping(value = "/items/{itemId}/edit")
//    public String updateItem(@ModelAttribute("form") BookForm form) {  // 위의 updateItemForm 안에서 form 이라는 변수와 데이터가 연동되는데 그거를 ModelAttribute로 잡아준다.
//
//        Book book = new Book(); // 객체 인데 id 가 세팅 되어 있다. -> jpa에게 한번 들어갔다 나온 애 : 한번 데이터베이스에 갔다 온 상태로 식별자가 정확하게 데이터 베이스에 있으면 이런거를 준영속성 객체
//        book.setId(form.getId());  // 영속성 컨텍스트가 더는 관리하지 않는다. jpa가 관리하지 않음 -> 이미 db에 저장이 되어서 식별자가 존재
//        book.setName(form.getName()); // 그냥 new 로 생성한거지만 db에서 저장된 걸 불러온거다.
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor()); // 원래는 jpa가 다 보고있다. 트랜잭션 커밋 시점에 변경될 거라고 바꿔치기 하는데 애는 jpa가 관리하지 않기에 값을 set으로 값을 바꿔도 DB에 업데이트가 안된다.
//        book.setIsbn(form.getIsbn()); // 원래는 hpa
//
//        itemService.saveItem(book);
//        return "redirect:/items";
//    }
    // 변경 기능 감지를 사용하면 딱 원하는 속성만 선택을 해서 데이터를 변경할 수 있다.
    // 병합을 쓰면 모든 속성이 다 갈아진다. -> 병합 시에 파라미터의 값이 없는데도 필드에 null로 업데이트 된다 -> 모든 필드를 교체하기 때문에 선택 할 수 있는 개념이 아니다.
    // merge를 가급적 사용하지 않고 최대한 변경 감지로 해주는게 좋다.


    // 엔티티를 변경하는 가장 좋은 방법
    // 위에서는 엔티티를 어설프게 넘겼다.
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {  // 위의 updateItemForm 안에서 form 이라는 변수와 데이터가 연동되는데 그거를 ModelAttribute로 잡아준다.

        // 어설프게 엔티티를 파라미터로 사용하지 않고 필요한 데이터만 싹 받아왔다. -> 코드의 명확성, 유지보수 하기 좋다.
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        return "redirect:/items";
    }
}
