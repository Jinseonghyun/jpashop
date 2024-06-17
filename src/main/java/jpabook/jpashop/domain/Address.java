package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable  // JPA 의 내장 타입 (어딘가에 내장될 수 있다.)
@Getter
public class Address {

    private String city;
}
