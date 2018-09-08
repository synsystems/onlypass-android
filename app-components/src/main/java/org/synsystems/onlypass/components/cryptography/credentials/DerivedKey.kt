package org.synsystems.onlypass.components.cryptography.credentials

import javax.crypto.SecretKey

/**
 * A key that has been derived from a password such that recovering the password is infeasible.
 */
data class DerivedKey(val key: SecretKey) : SecureCredential