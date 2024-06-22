package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService { // ItemService는 itemRepository 에 단순하게 위임만 하는 클래스

    private final ItemRepository itemRepository;

    // 저장
    @Transactional // 위에가 (readOnly = true) 이기에 해줘야함 안해주면 저장 안됨 (우선권을 가지기 위해 오버라이팅 해버린것 )
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 조회  // Transactional 따로 안해주기에 맨 위 전체 @Transactional(readOnly = true) 가 먹힌다.
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 단건 조회
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

}
