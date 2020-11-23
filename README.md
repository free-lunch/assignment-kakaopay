# 카카오페이 과제

## 뿌리기 기능 구현하기
----
## 언어:  Java
---
## 1. 문제해결전략
### 문제1) 자바를 해본 적이 없다.
언어적인 면은 어렵지 않았으나, 환경구성 및 선택요소들 때문에 시간이 많이 잡힘.

아래는 하다가 고민하거나, 검색해본 포인트

- IDE는 뭘로 선택해야되나? -> vscode를 이용하려고 하다가, STS를 선택

- String 비교의 경우에는 노드와 달라, 버그가 발생했었음.
- Maven과 Gradle의 선택 -> gradle을 요즘 많이 사용하는 추세이며 script도 되는거 같아 사용하려 했으나, 이번 케이스에서는 스크립트 사용할 일도 없고 검색 결과에서는 maven이 많이 나와 maven으로 선택
- node의 nullable -> java의 optional 

### 문제 2) Spring 프레임워크
스프링이 뭔가 했으나, 노드에서 사용하던 nest.js와 비슷하여 감이 약간 잡힌 상태에서 시작.
nest.js보다 제공되는 기능이 더 많아 전체적으로 편리한 느낌이었음.

- node에는 nest.js를 사용했었고, 모듈이라는 개념으로 di를 많이 사용. => spring에서는 di를 위한 annotation을 많이 제공해서 편했음.
- lombok에서 제공해주는 다양한 annotation이 매우 편했음.
- 테스트에 필요한 mocking, di들을 매우 많이 제공해줘서, 매우 편했음.
### 문제 3) 동시성의 해소
- 여러 인스턴스에서 실행한다고 해서, 분산락으로 해결
- redis 분산락을 사용했고, 이를 위해 lettuce에서 redisson으로 갈아탐.

---
## 2. 데이터베이스
레디스만으로 전체 요구사항을 구현함.

선택한 이유는 분산락 때문에 레디스를 이용할 계획이었으며, 요구사항에 있는 time limit 요구사항이 적합했기 때문.
만약 이력을 영구적으로 남겨야되는 요구사항이 있었으면, RDB도 추가했을 듯.

추가적으로 Hash를 선택. 이유는 나눠지는 갯수에 따라 value가 많이 늘어날 수 있어서. 그리고 token이라는 값으로 기준으로 여러가지 값들이 들어가는 형태가 Hash가 적합하다고 생각. 

DAO
```
Scatter
{
	private String token;
	private String roomId;
	private String ownerUserId;
	private Long price;
	private int dividerNumber;
	private ArrayList<ScatterDetail> details;
	private Date createdAt;
}

ScatterDetail
{
	private Long dividedPrice;
	private String preemptedUserId;
	private Boolean isPreempted;

}
```

---
## 3. API 명세서
[Postman Link](https://documenter.getpostman.com/view/1049673/TVev6RGt)
### 뿌리기 API
- URL: /transfer/v1/scatter
- Method: POST
- Headers: 
```
	String X-USER-ID (required),
	String X-ROOM-ID (required)
```
- Body Params:
```
{
	String token
}
```
- Success Response:
```
http code: 201
{
	String token
}

```


### 받기 API
- URL: /transfer/v1/scatter/:token
- Method: PUT
- Headers:
```
	String X-USER-ID (required),
	String X-ROOM-ID (required)
```
- URL Params:
```
	String token(required, length 3)
```
- Success Response:
```
http code: 200
{
	Int price
}
```
- Error Response: 
```
http code: 400
{
    "errorCode": "E001",
    "errorMessage": "중복된 요청 에러입니다."
}

http code: 400
{
    "errorCode": "E002",
    "errorMessage": "같은 방의 요청이 아닙니다."
}

http code: 400
{
    "errorCode": "E003",
    "errorMessage": "뿌림이 완료되었습니다."
}

http code: 400
{
    "errorCode": "E004",
    "errorMessage": "자신이 뿌린 건 입니다."
}

http code: 400
{
    "errorCode": "E005",
    "errorMessage": "요청한 토큰에 맞는 뿌리기가 없거나, 유효기간이 지났습니다"
}

http code: 400
{
    "errorCode": "E006",
    "errorMessage": "시간이 만료된 요청입니다."
}

```

### 조회 API
- URL: /transfer/v1/scatter/:token
- Method: GET
- Headers:
```
	String X-USER-ID (required),
	String X-ROOM-ID (required)
```
- URL Params:
```
	String token(required, length 3)
```
- Success Response:
```
http code: 200
{
	String createdAt,
	Int price,
	Int preemptedPrice,
	details: [
		{
			Int dividedPrice,
			String preemptedUserId
		}
	]
}
```
- Error Response: 
```
http code: 400
{
    "errorCode": "E007",
    "errorMessage": "잘못된 유저의 뿌림조회 요청입니다."
}
```




