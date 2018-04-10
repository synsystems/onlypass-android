package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestAesConfiguration {
  @Test(expected = RuntimeException.class)
  public void testBuild_initialisationVectorBitlengthNotSet() {
    AesConfiguration
        .builder()
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_initialisationVectorBitlengthLessThan8() {
    AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(7)
        .build();
  }

  @Test
  public void testBuild_initialisationVectorBitlengthEqualTo8() {
    AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_initialisationVectorBitlengthNotAMultipleOf8() {
    AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(9)
        .build();
  }

  @Test
  public void testBuild_allValidValues() {
    final AesConfiguration parameters = AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(8)
        .build();

    assertThat(parameters.getInitialisationVectorBitlength(), is(8));
  }

  @Test
  public void testToBuilderThenBuild_allValidValues() {
    final AesConfiguration parameters = AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(8)
        .build()
        .toBuilder()
        .build();

    assertThat(parameters.getInitialisationVectorBitlength(), is(8));
  }
}