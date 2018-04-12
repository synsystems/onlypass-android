package org.synsystems.onlypass.components.cryptography.credentialhardening;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * Parameters to configure password based key derivation on a per-derivation basis.
 */
@AutoValue
public abstract class Pbkdf2WithHmacSha256Parameters implements HardeningParameters {
  /**
   * @return the salt to use to derive the key
   */
  @SuppressWarnings("mutable")
  @NonNull
  public abstract byte[] getSalt();

  /**
   * @return the number of iterations to use to derive the key
   */
  public abstract int getIterationCount();

  /**
   * @return the length of the derived key
   */
  public abstract int getDerivedKeyBitlength();

  /**
   * @return a new {@link Builder} with values copied from this Pbkdf2WithHmacSha256Parameters
   */
  public Builder toBuilder() {
    return builder()
        .setSalt(getSalt())
        .setIterationCount(getIterationCount())
        .setDerivedKeyBitlength(getDerivedKeyBitlength());
  }

  /**
   * @return a new {@link Builder} with empty values
   */
  public static Builder builder() {
    return new AutoValue_Pbkdf2WithHmacSha256Parameters.Builder();
  }

  /**
   * Builds new {@link Pbkdf2WithHmacSha256Parameters} instances.
   */
  @SuppressWarnings("NullableProblems")
  @AutoValue.Builder
  public static abstract class Builder {
    /**
     * Sets the salt to use to derive the key. The array must not be empty.
     *
     * @param salt
     *     the salt to use to derive the key
     *
     * @return this Builder
     */
    @NonNull
    public abstract Builder setSalt(@NonNull byte[] salt);

    /**
     * Sets the number of iterations to use to derive the key.
     *
     * @param iterationCount
     *     the number of iterations to use to derive the key
     *
     * @return this Builder
     */
    @NonNull
    public abstract Builder setIterationCount(int iterationCount);

    /**
     * Sets the length of the derived key.
     *
     * @param derivedKeyLength
     *     the length of the derived key
     *
     * @return this Builder
     */
    @NonNull
    public abstract Builder setDerivedKeyBitlength(int derivedKeyLength);

    @NonNull
    abstract Pbkdf2WithHmacSha256Parameters autoBuild();

    /**
     * Creates a new {@link Pbkdf2WithHmacSha256Parameters} based on this builder. Building will fail if any of
     * the values have not been set or were set to invalid values.
     *
     * @return the new password based key derivation parameters
     */
    @NonNull
    public Pbkdf2WithHmacSha256Parameters build() {
      final Pbkdf2WithHmacSha256Parameters parameters = autoBuild();

      if (parameters.getSalt().length == 0) {
        throw new IllegalStateException("Salt cannot be empty.");
      }

      if (parameters.getIterationCount() < 1) {
        throw new IllegalStateException("Iteration count must be at least 1.");
      }

      if (parameters.getDerivedKeyBitlength() < 8) {
        throw new IllegalStateException("Derived key bitlength must be at least 8.");
      }

      if (parameters.getDerivedKeyBitlength() % 8 != 0) {
        throw new IllegalStateException("Derived key bitlength must be a multiple of 8.");
      }

      return parameters;
    }
  }
}