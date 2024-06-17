package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // 싱글테이블일 때 DB입장에서 값을 저쟝할 때 구분할 수 있어야 한다. 그 떄 넣는 값
@Getter
@Setter
public class Book extends Item {

    private String author; // 작가
    private String isbn;  // 기타 정보
}
