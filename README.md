# 상품주문관리 시스템

## 요구사항

1. 상품 주문 프로그램을 작성합니다.

2. 상품은 고유의 상품번호와 상품명, 판매가격, 재고수량 정보를 가지고 있습니다.

3. 한 번에 여러개의 상품을 같이 주문할 수 있어야 합니다.

4. 상품번호, 주문수량은 반복적으로 입력 받을 수 있습니다.

5. 단, 한번 결제가 완료되고 다음 주문에선 이전 결제와 무관하게 주문이 가능해야 합니다.

6. 주문은 상품번호, 수량을 입력받습니다.

7. empty 입력 (space + ENTER) 이 되었을 경우 해당 건에 대한 주문이 완료되고, 결제하는 것으로 판단합니다.

8. 결제 시 재고 확인을 하여야 하며 재고가 부족할 경우 결제를 시도하면 SoldOutException 이 발생되어야 합니다.

9. 주문 금액이 5만원 미만인 경우 배송료 2,500원이 추가되어야 합니다.

10. 주문이 완료되었을 경우 주문 내역과 주문 금액, 결제 금액 (배송비 포함) 을 화면에 display 합니다.

11. 'q' 또는 'quit' 을 입력하면 프로그램이 종료되어야 합니다.

12. Test 에서는 반드시 multi thread 요청으로 SoldOutException 이 정상 동작하는지 확인하는 단위테스트가 작성되어야 합니다

13. 상품의 데이터는 하단에 주어지는 데이터를 사용해주세요

14. 데이터를 불러오는 방식은 자유입니다.

15. 코드에 포함되어도 좋고, 파일을 불러도 되고, in memory db 를 사용하셔도 됩니다. 하지만 상품에 대한 상품번호, 상품명, 판매가격, 재고수량 데이터는 그대로 사용하셔야 합니다.

16. 상품 데이터 csv 파일을 같이 제공합니다.


## 상세 구현

1. CSV File Read 후 DB Insert

2. 입력, 주문, 종료 기능 구현 (이 외 문자열 입력 시 재요청)

3. 상품 번호 혹은 수량 입력 시 공백일 경우 결제 로직 실행

4. 상품번호가 존재하지 않을 시 ProductNotFoundException 발생 후 재요청

5. 요청한 상품의 수량이 남아있지 않을 시 SoldOutException 발생

6. 상품 주문시 동일한 상품 여러번 주문가능

7. 5만원 미만일 시 배송료 추가 결제

8. 이 외 요구사항 구현 완료.

## 구현방법
- JAVA 11
- Srping boot 2.75
- H2(Memory)
- JPA
- Gradle
- Lombok
- Junit5

## 검증결과
- JUNIT 테스트
- JUNIT DB 초기화
- multi thread 주문으로 인한 재고 처리 확인

## 형상관리
- git
- Branch (master, dev)

## 기타사항
- 요구사항의 콘솔화면을 위하여 System.out 사용
- 요구사항 외 로그일 경우 Sl4fj 사용
- 추후에 Redis로 변경 필요
