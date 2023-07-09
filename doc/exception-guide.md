# Exception Guide
스프링은 예외처리를 위한 다양한 기능을 제공한다. 이 가이드에서는 스프링의 예외처리 기능을 사용하는 방법을 설명한다.

> 예외처리하면 또 나오는 것이 검증(Validation)이다. 
> 이와 관련된 내용은 [Validation](https://github.com/BEOMKING/Study/blob/main/Spring/Validation.md) 포스팅을 참조하자.

## 목차

- [일관된 응답 형식](#일관된-응답-형식)

- [전역 예외 처리](#전역-예외-처리)

- [Error Code](#Error-Code)

- [예외 처리 예제](#예외-처리-예제)

- [결론](#결론)

## 일관된 응답 형식

### Json Response

Error Response 객체는 항상 일관된 형식을 가져야 한다. 만약 API마다 응답하는 데이터의 형식이 다르다면 클라이언트에서 동일한 로직으로 처리하기가 어려울 것이다.

```json
{
    "code": "C001",
    "message": "Invalid Input Value",
    "fieldErrors": [
        {
            "field": "age",
            "value": "8",
            "reason": "20에서 30 사이여야 합니다"
        }
    ]
}
```

위와 같이 정해진 형식으로 에러 응답을 주어 일관성을 가져갈 수 있다.

- code

  해당 에러를 식별하는 코드

- message

  해당 에러에 대한 힌트를 제공

- fieldErrors

  요청 값에 대해 필드 에러가 발생했을 때, 필드 에러 정보를 담는다.

  `Bean Validation`과 사용된다.

### Error Response

```java
@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private List<CustomError> fieldErrors;
    // .. 생성자
    // .. 정적 팩터리 메서드
    
    @Getter
    public static class CustomError {
        private String field;
        private String value;
        private String reason;       
        // .. 생성자
 		    // .. 정적 팩터리 메서드
  	}
}
```

생략된 부분은 코드를 참조

## 전역 예외 처리

`@RestControllerAdvice`를 활용하여 모든 예외를 한 곳에서 처리할 수 있다. 컨트롤러별로 예외 처리가 다르게 될 일이 실제로 거의 없다. 그러므로 전역으로 예외 처리를 하는 것이 효율적이다.

> `@RestControllerAdvice`, `@ExceptionHandler`에 대한 자세한 원리는  [ExceptionHandler](https://github.com/BEOMKING/Study/blob/main/Spring/ExceptionHandler.md) 참조

```java
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(final Exception e) {
        log.error("handleException", e);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        return ErrorResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleResourceConflictException(final ResourceConflictException e) {
        log.error("handleResourceConflictException", e);
        final ErrorCode errorCode = e.getErrorCode();
        return ErrorResponse.of(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e, final Locale locale) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource, locale);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE);
        return ResponseEntity.badRequest().body(response);
    }
}
```

### 메서드 설명

- `handleException`

  개발자가 예외처리하지 않은 모든 예외 처리

- `handleBusinessException`

  비즈니스 요구사항 (개발자가 정의한) 예외 처리

- `handleResourceConflictException`

  `BusinessException`을 상속받는 커스텀 예외 처리

- `handleMethodArgumentNotValidException`

  `@Valid` 같이 `Bean Validation`을 사용해 클라이언트 검증에 걸린 예외 처리

- `handleMethodArgumentTypeMismatchException`

  `@RequestParam` 으로 보낸 타입과 실제 타입이 바인딩 되지 않을 때 발생하는 예외 처리

- `handleHttpMessageNotReadableException`

  `@RequestBody`에 정의된 필드들의 타입이 바인딩 되지 않을 때 발생하는 예외 처리

별도의 상태 코드 처리를 하지 않으면 200 응답이 간다. 하지만 상태 코드를 예외 별로 지정해주어야 한다.

이를 처리하는 것이 `@ResponseStatus(상태코드)`, `ResponseEntity.상태코드` 이다. 위 예제에서는 두 방식 모두 사용했는데 더 편한 것을 사용하면 된다.

## Error Code

```java
@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE("C001", "Invalid Input Value"),
    INVALID_TYPE_VALUE("C002", "Invalid Type Value"),
    METHOD_NOT_ALLOWED("C002", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("C003", "Server Error"),

    // Member
    EMAIL_DUPLICATION("M001", "Email is Duplication"),
    INVALID_MEMBER("M002", "Invalid Member");
  
  	// 필드, 생성자 생략
}
```

에러 코드와 메시지를 한 곳에서 관리한다.

이 메시지들이 탈중앙화 되어 있다면 제각각인 메시지들이 발생하고 에러 코드를 재사용하기 힘들다.

국제화가 필요하다면 위 메시지 대신 message 프로퍼티의 key를 넣어 커스텀하는 것도 방법이 될 수 있다.

## 예외 처리 예제

`Bean Validation`에 의해 검증 실패된 케이스를 하나 살펴보자.

```java
@Getter
public class MemberCreateRequest {
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Range(min = 20, max = 30)
    private Integer age;
}
```

이렇게 정의된 Request DTO에 아래와 같은 요청을 보낸다. (컨트롤러 생략)

```json
{
    "email": "email@naver.com",
    "name": "BJP",
    "password": "123453",
    "age": "8"
}
```

패스워드 자리수와 나이의 최솟값 검증에 실패될 것이다.

그 결과 아래 메서드의 로직이 수행된다.

```java
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e, final Locale locale) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource, locale);
        return ResponseEntity.badRequest().body(response);
    }
}
```

```java
@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private List<CustomError> fieldErrors;
		
		// .. 생성자 생략
    // .. 정적 팩터리 메서드 생략
    
    public static ErrorResponse of(final ErrorCode errorCode, final Errors errors, final MessageSource messageSource, final Locale locale) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), CustomError.of(errors, messageSource, locale));
    }

    @Getter
    public static class CustomError {
        private String field;
        private String value;
        private String reason;

        // .. 생성자 생략
        // .. 정적 팩터리 메서드 생략

        private static List<CustomError> of(final Errors errors, final MessageSource messageSource, final Locale locale) {
            final List<FieldError> fieldErrors = errors.getFieldErrors();
            return fieldErrors.stream()
                    .map(error ->
                            new CustomError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            messageSource.getMessage(error, locale)))
                    .toList();
        }
    }
}
```

필드 검증으로 발생된 `errors`의 루프를 돌면서 `CustomError`를 생성한다. 이 때, `messageSource.getMessage(error, locale)`을 통해 필드 에러 메시지 국제화를 수행한다.

> 이 방식을 활용해 메시지 국제화를 수행할 수 있다.  
> 위 예제처럼 필드 메시지를 따로 커스텀 국제화할 필요는 없어보인다. `Bean Validation`의 기본 메시지가 국제화를 잘 지원해 준다.

## 결론

일관된 `Error Response`를 통해 예외처리를 간단하게 처리할 수 있으며 클라이언트에게 예외 정보를 제공할 수 있다.
