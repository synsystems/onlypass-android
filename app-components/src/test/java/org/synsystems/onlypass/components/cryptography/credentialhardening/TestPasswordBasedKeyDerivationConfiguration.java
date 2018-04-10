package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestPasswordBasedKeyDerivationConfiguration {
  @Test(expected = RuntimeException.class)
  public void testBuild_iterationCountNotSet() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setDerivedKeyBitlength(256)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_iterationCountLessThan1() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(0)
        .setDerivedKeyBitlength(256)
        .build();
  }

  @Test
  public void testBuild_iterationCountEqualTo1() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(1)
        .setDerivedKeyBitlength(256)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthNotSet() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthLessThan8() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .setDerivedKeyBitlength(7)
        .build();
  }

  @Test
  public void testBuild_derivedKeyBitlengthEqualTo8() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthNotAMultipleOf8() {
    PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .setDerivedKeyBitlength(9)
        .build();
  }

  @Test
  public void testBuild_allValidValues() {
    final PasswordBasedKeyDerivationConfiguration parameters = PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .setDerivedKeyBitlength(256)
        .build();

    assertThat(parameters.getIterationCount(), is(64));
    assertThat(parameters.getDerivedKeyBitlength(), is(256));
  }

  @Test
  public void testToBuilderThenBuild() {
    final PasswordBasedKeyDerivationConfiguration parameters = PasswordBasedKeyDerivationConfiguration
        .builder()
        .setIterationCount(64)
        .setDerivedKeyBitlength(256)
        .build()
        .toBuilder()
        .build();

    assertThat(parameters.getIterationCount(), is(64));
    assertThat(parameters.getDerivedKeyBitlength(), is(256));
  }
}