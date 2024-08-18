package com.kuit.agarang.global.common.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
  SUCCESS(true, HttpStatus.OK, 200, "요청에 성공하였습니다."),
  AUTHORIZATION_SUCCESS(true, HttpStatus.OK, 200, "토큰 발급에 성공하였습니다."),
  BAD_REQUEST(false, HttpStatus.BAD_REQUEST, 400, "입력값을 확인해주세요."),
  UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
  FORBIDDEN(false, HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
  NOT_FOUND(false, HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),

  INVALID_MEMORY_ID(false, HttpStatus.NOT_FOUND, 4001, "존재하지 않는 추억입니다."),
  FILE_TOO_LARGE(false, HttpStatus.PAYLOAD_TOO_LARGE, 4002, "최대 파일 사이즈(1MB)를 초과했습니다."),
  INVALID_FILE_EXTENSION(false, HttpStatus.BAD_REQUEST, 4003, "지원하지 않는 파일확장자입니다."),
  NOT_FOUND_RESOURCE(false, HttpStatus.NOT_FOUND, 4004, "존재하지 않는 페이지입니다."),
  NOT_FOUND_S3_FILE(false, HttpStatus.NOT_FOUND, 4005, "S3 파일 서버에 이미지 URL과 일치하는 파일이 존재하지 않습니다."),
  NOT_FOUND_TSS_AUDIO(false, HttpStatus.NOT_FOUND, 4006, "TTS 오디오 생성 및 저장에 문제가 발생했습니다."),
  NOT_FOUND_HISTORY_CHAT(false, HttpStatus.NOT_FOUND, 4007, "카드 생성 중 이전 대화기록을 찾을 수 없습니다. id 값을 확인해주세요."),
  INVALID_MUSIC_CHOICE(false, HttpStatus.BAD_REQUEST, 4008, "음악 선택값이 올바른지 확인해주세요."),
  FAIL_FILE_READ(false, HttpStatus.FORBIDDEN, 4009, "파일 업로드에 실패했습니다. 다시 시도해주세요."),
  INVALID_MEMBER_ID(false, HttpStatus.NOT_FOUND, 40010, "존재하지 않는 회원입니다."),
  INVALID_PLAYLIST_ID(false, HttpStatus.NOT_FOUND, 4011, "존재하지 않는 플레이리스트입니다."),
  NOT_FOUND_BABY(false, HttpStatus.NOT_FOUND, 4012, "아기를 찾을 수 없습니다"),
  NOT_FOUND_CHARACTER(false, HttpStatus.NOT_FOUND, 4013, "캐릭터를 찾을 수 없습니다"),
  NOT_FOUND_MEMBER(false, HttpStatus.NOT_FOUND, 4014, "사용자를 찾을 수 없습니다"),
  NOT_FOUND_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, 4015, "리프레시 토큰을 찾을 수 없습니다. 다시 로그인해주세요."),
  EXPIRED_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, 4016, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
  FILE_DOWNLOAD_ERROR(false, HttpStatus.FORBIDDEN, 4017, "musicgen 파일 다운로드 중 오류가 발생했습니다."),
  EXPIRED_ACCESS_TOKEN(false, HttpStatus.UNAUTHORIZED, 4018, "액세스 토큰이 만료되었습니다. 토큰 재발급 요청이 필요합니다."),
  INVALID_ACCESS_TOKEN(false, HttpStatus.UNAUTHORIZED, 4019, "유효하지 않은 액세스 토큰입니다."),
  INVALID_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, 4019, "유효하지 않은 리프레시 토큰입니다."),

  SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, 5001, "알 수 없는 이유로 서버에 문제가 발생했습니다."),
  FAIL_REDIS_CONNECTION(false, HttpStatus.SERVICE_UNAVAILABLE, 5002, "레디스 서버에 연결 실패했습니다."),
  FAIL_S3_UPLOAD(false, HttpStatus.SERVICE_UNAVAILABLE, 5003, "S3 파일 서버 업로드에 실패했습니다."),
  INVALID_GPT_RESPONSE(false, HttpStatus.INTERNAL_SERVER_ERROR, 5004, "GPT 응답이 유효하지 않습니다."),
  FAIL_CREATE_MUSIC(false, HttpStatus.INTERNAL_SERVER_ERROR, 5005, "music gen 음악 생성에 실패했습니다.");


  private final boolean isSuccess;
  @JsonIgnore
  private final HttpStatus httpStatus;
  private final int code;
  private final String message;

  BaseResponseStatus(boolean isSuccess, HttpStatus httpStatus, int code, String message) {
    this.isSuccess = isSuccess;
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}
