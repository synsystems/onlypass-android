package org.synsystems.onlypass.components.cryptography.credentials;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * A password that has not been obfuscated or encrypted in any way.
 */
@AutoValue
public abstract class CleartextPassword implements InsecureCredential {
  /**
   * @return the password
   */
  @NonNull
  public abstract String getPassword();

  /**
   * Constructs a new CleartextPassword.
   *
   * @param password
   *     the password
   *
   * @return the new CleartextPassword
   */
  @NonNull
  public static CleartextPassword create(@NonNull final String password) {
    return new AutoValue_CleartextPassword(password);
  }
}