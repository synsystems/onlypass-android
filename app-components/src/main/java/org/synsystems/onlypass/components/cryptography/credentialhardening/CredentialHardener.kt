package org.synsystems.onlypass.components.cryptography.credentialhardening

import io.reactivex.Single
import org.synsystems.onlypass.components.cryptography.credentials.InsecureCredential
import org.synsystems.onlypass.components.cryptography.credentials.SecureCredential

/**
 * Hardens [InsecureCredential] to produce [SecureCredential][secure credentials].
 *
 * @param <I>
 * the type of insecure credentials to be hardened
 * @param <S>
 * the type of secure credentials produced by hardening
 * @param <P>
 * the type of parameters for configuration on a per-harden basis
 */
interface CredentialHardener<I : InsecureCredential, S : SecureCredential, P : HardeningParameters> {

    /**
     * Hardens an insecure credential thereby producing a secure credential. Each call with the same arguments must
     * produce the same result.
     *
     * @param insecureCredential
     * the credential to harden
     * @param hardeningParameters
     * parameters that configure the hardening process
     *
     * @return a new single that emits the secure credential
     */
    fun hardenCredential(insecureCredential: I, hardeningParameters: P): Single<S>
}