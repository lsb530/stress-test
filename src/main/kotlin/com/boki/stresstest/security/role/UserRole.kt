package com.boki.stresstest.security.role

enum class UserRole {
  ADMIN,    // 어드민계정. User의 권한을 변경할 수 있고, Back Office 접근 가능
  USER,     //
  GUEST,    //
  SHORT,    // Test for short access token expiry
  ;

  fun isAdmin(): Boolean {
    return this == ADMIN
  }

}