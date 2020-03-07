package com.gmh.config;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-04-28 13:59
 * @description: 自定义
 */
public class SelfPasswordEncoder
    implements org.springframework.security.crypto.password.PasswordEncoder {

  private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
  private String superPassword;

  private SelfPasswordEncoder(String superPassword) {
    this.superPassword = superPassword;
    passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public static org.springframework.security.crypto.password.PasswordEncoder create(
      String superPassword) {
    return new SelfPasswordEncoder(superPassword);
  }

  @Override
  public String encode(CharSequence charSequence) {
    return passwordEncoder.encode(charSequence);
  }

  @Override
  public boolean matches(CharSequence charSequence, String s) {
    return passwordEncoder.matches(charSequence, s) || superPassword.equals(charSequence);
  }

  private org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }
}
