package org.synsystems.onlypass.components.cryptography.credentials

/**
 * A password that has not been obfuscated or encrypted in any way.
 */
data class CleartextPassword(val password: String) : InsecureCredential