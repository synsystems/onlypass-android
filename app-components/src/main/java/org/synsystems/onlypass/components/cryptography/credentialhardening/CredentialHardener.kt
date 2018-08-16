package org.synsystems.onlypass.components.cryptography.credentialhardening

import io.reactivex.Single
import org.synsystems.onlypass.components.cryptography.credentials.InsecureCredential
import org.synsystems.onlypass.components.cryptography.credentials.SecureCredential

/**
 * Hardens [InsecureCredential]s to produce [SecureCredential]s.
 *
 * @param I the type of insecure credentials to be hardened
 * @param S the type of secure credentials produced by hardening
 * @param P the type of parameters used to control hardening
 */
interface CredentialHardener<I : InsecureCredential, S : SecureCredential, P : HardeningParameters> {

  /**
   * Hardens [insecureCredential]. Calls with the same arguments must be idempotent.
   *
   * @param hardeningParameters parameters that configure the hardening process
   *
   * @return the secure credential produced by hardening
   */
  fun hardenCredential(insecureCredential: I, hardeningParameters: P): Single<S>
}

/**
 * Parameters for configuring credential hardening on a per-harden basis.
 */
interface HardeningParameters