package org.synsystems.onlypass.components.cryptography.credentialhardening;

import android.support.annotation.NonNull;

import org.synsystems.onlypass.components.cryptography.credentials.InsecureCredential;
import org.synsystems.onlypass.components.cryptography.credentials.SecureCredential;

import io.reactivex.Single;

/**
 * Hardens {@link InsecureCredential}s to produce {@link SecureCredential}s.
 *
 * @param <I>
 *     the type of insecure credentials to be hardened
 * @param <S>
 *     the type of secure credentials produced by hardening
 * @param <P>
 *     the type of parameters for configuration on a per-harden basis
 */
public interface CredentialHardener<
    I extends InsecureCredential,
    S extends SecureCredential,
    P extends HardeningParameters> {

  /**
   * Hardens an insecure credential thereby producing a secure credential. Given the same credential hardener state,
   * insecure credential and hardening parameters, every call must be deterministic and reproducible. That is to say,
   * the method is idempotent for any given set of inputs.
   *
   * @param insecureCredential
   *     the credential to harden
   * @param hardeningParameters
   *     parameters that configure the hardening process
   *
   * @return a new single that emits the secure credential
   */
  @NonNull
  public Single<S> hardenCredential(@NonNull I insecureCredential, @NonNull P hardeningParameters);
}