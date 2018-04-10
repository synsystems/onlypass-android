package org.synsystems.onlypass.components.cryptography.encryption.aes;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * Configuration parameters for AES encryption and decryption.
 */
@SuppressWarnings("NullableProblems")
@AutoValue
public abstract class AesConfiguration {
  /**
   * @return the bitlength of the initialisation vector
   */
  @NonNull
  public abstract int getInitialisationVectorBitlength();

  /**
   * @return a new {@link Builder} with values copied from this configuration
   */
  @NonNull
  public Builder toBuilder() {
    return builder()
        .setInitialisationVectorBitlength(getInitialisationVectorBitlength());
  }

  /**
   * @return a new {@link Builder} with empty values
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_AesConfiguration.Builder();
  }

  /**
   * Builds new {@link AesConfiguration} instances.
   */
  @AutoValue.Builder
  public static abstract class Builder {
    /**
     * Sets the initialisation vector bit length. The value must be greater than 7, and it must be a multiple of 8.
     *
     * @param initialisationVectorBitlength
     *     the initialisation vector bit length
     *
     * @return this builder
     */
    @NonNull
    public abstract Builder setInitialisationVectorBitlength(int initialisationVectorBitlength);

    @NonNull
    abstract AesConfiguration autoBuild();

    /**
     * Creates a new {@link AesConfiguration} based on this builder. Building will fail if any of the values have not
     * been set or were set to invalid values.
     *
     * @return the new configuration
     */
    @NonNull
    public AesConfiguration build() {
      final AesConfiguration parameters = autoBuild();

      if (parameters.getInitialisationVectorBitlength() < 8) {
        throw new IllegalStateException("Initialisation vector bitlength must be at least 8.");
      }

      if (parameters.getInitialisationVectorBitlength() % 8 != 0) {
        throw new IllegalStateException("Initialisation vector bitlength must be a multiple of 8.");
      }

      return parameters;
    }
  }
}