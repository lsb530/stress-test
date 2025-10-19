package com.boki.stresstest.common.exception.code

enum class ErrorCode(val code: String, val message: String) {
    /** Common **/
    NOT_FOUND("CE001", "해당 자원을 찾을 수 없습니다."),
    DUPLICATED("CE002", "이미 존재하는 자원입니다."),

    /** System **/
    NOT_SUPPORT_API("SE001", "지원되지 않는 API PATH 요청입니다."),
    NOT_SUPPORT_HTTP_METHOD("SE002", "지원되지 않는 API METHOD 요청입니다."),
    MISSING_PARAMETER("SE003", "필요한 파라미터가 존재하지 않습니다."),
    TYPE_MISMATCH("SE004", "입력받은 파라미터가 타입이 일치하지 않습니다."),
    ARGUMENT_NOT_VALIDATED("SE005", "입력받은 데이터가 유효성 검증 실패하였습니다."),
    NOT_READABLE_JSON("SE006", "잘못된 JSON 형식 또는 읽을 수 없는 요청입니다."),
    ILLEGAL_ARGUMENT("SE007", "요청 파라미터가 유효하지 않습니다."),
    ILLEGAL_STATE("SE008", "요청 파라미터가 유효하지 않습니다."),
    UNINITIALIZED_PROPERTY("SE009", "lateinit error: 초기화되지 않은 프로퍼티에 접근했습니다."),
    NULL_POINTER("SE010", "Null Pointer 접근 시도."),

    /** System - Database **/
    DATA_ACCESS_ERROR("SE010", "데이터베이스 처리 중 오류가 발생했습니다."),
    RUNTIME_ERROR("SE011", "Runtime Exception이 발생하였습니다."),
    SYSTEM_ERROR("SE012", "Unknown Exception 발생."),

    /** System - Business **/
    BUSINESS_ERROR("SE013", "애플리케이션에 문제가 발생했습니다."),

    /** Auth **/
    FAILED_LOGIN("AE001", "로그인에 실패하였습니다."),
    FAILED_ACCESS("AE002", "접근 권한이 없습니다."),
    NOT_FOUND_TOKEN("AE003", "존재하지 않는 JWT입니다."),

    FAILED_REGISTER_ADMIN("AE004", "이미 등록된 이메일입니다."),

    /** Firebase **/
    FAILED_FIREBASE_AUTH("FE001", "Firebase 인증에 실패하였습니다."),
    FAILED_FIREBASE_REQUEST("FE002", "Firebase 관련 요청이 실패하였습니다."),

    /** Role **/
    NOT_FOUND_ROLE("RE001", "해당 권한은 존재하지 않습니다."),
    DUPLICATED_ROLE("RE002", "이미 존재하는 권한입니다."),

    /** User **/
    NOT_FOUND_USER("UE001", "등록된 회원 정보가 없습니다"),
    DUPLICATED_USER("UE002", "이미 존재하는 회원입니다."),

    /**
     * Product
     */
    NOT_FOUND_PRODUCT("PE001", "해당 제품은 존재하지 않습니다."),
    DUPLICATED_PRODUCT("PE002", "이미 존재하는 제품입니다."),

    /**
     * Shipping
     */
    INVALID_INIT_SHIPPING_STATE("SH001", "최초 배송 상태로의 변경은 불가합니다."),

    /**
     * Order
     */
    INVALID_INIT_ORDER_STATE("OR001", "최초 배송 상태로의 변경은 불가합니다."),
    DUPLICATE_ORDER_NUMBER("OR002", "중복된 주문번호입니다."),

    /** File **/
    NOT_FOUND_FILE("FE001", "해당 파일은 존재하지 않습니다."),
    DUPLICATED_FILE("FE002", "이미 존재하는 파일입니다."),

    /** FAQ **/
    EMPTY_FAQ_PASSWORD("FA001", "비밀 Q&A을 볼 수 있는 정보가 없습니다."),
    INVALID_FAQ_PASSWORD("FA002", "비밀 Q&A에 설정된 비밀번호와 다릅니다."),
    INVALID_FAQ_ACCESS("FA003", "본인이 작성한 Q&A 게시글이 아닙니다."),
}