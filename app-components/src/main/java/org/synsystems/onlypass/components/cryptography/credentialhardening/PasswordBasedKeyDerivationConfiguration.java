package org.synsystems.onlypass.components.cryptography.credentialhardening;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * Configuration parameters for password based key derivation.
 */
@AutoValue
public abstract class PasswordBasedKeyDerivationConfiguration implements HardeningParameters {
  /**
   * @return the number of iterations to use
   */
  public abstract int getIterationCount();

  /**
   * @return the desired bitlength of the derived key
   */
  public abstract int getDerivedKeyBitlength();

  /**
   * @return a new {@link Builder} with values copied from this configuration
   */
  @NonNull
  public Builder toBuilder() {
    return builder()
        .setIterationCount(getIterationCount())
        .setDerivedKeyBitlength(getDerivedKeyBitlength());
  }

  /**
   * @return a new {@link Builder} with empty values
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_PasswordBasedKeyDerivationConfiguration.Builder();
  }

  /**
   * Builds new {@link PasswordBasedKeyDerivationConfiguration} instances.
   */
  @SuppressWarnings("NullableProblems")
  @AutoValue.Builder
  public static abstract class Builder {
    /**
     * Sets the number of iterations to use. The value must be greater than 0.
     *
     * @param iterationCount
     *     the number of iterations to use
     *
     * @return this builder
     */
    @NonNull
    public abstract Builder setIterationCount(int iterationCount);

    /**
     * Sets the desired bitlength of the derived key. The value must be greater than 7, and it must be a multiple of 8.
     *
     * @param derivedKeyBitlength
     *     the desired bitlength of the derived key
     *
     * @return this builder
     */
    @NonNull
    public abstract Builder setDerivedKeyBitlength(int derivedKeyBitlength);

    @NonNull
    abstract PasswordBasedKeyDerivationConfiguration autoBuild();

    /**
     * Creates a new {@link PasswordBasedKeyDerivationConfiguration} based on this builder. Building will fail if any of
     * the values have not been set or were set to invalid values.
     *
     * @return the new configuration
     */
    @NonNull
    public PasswordBasedKeyDerivationConfiguration build() {
      final PasswordBasedKeyDerivationConfiguration parameters = autoBuild();

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