package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class TestPbkdf2WithHmacSha256Parameters {
  @Test(expected = RuntimeException.class)
  public void testBuild_saltNotSet() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_emptySaltSet() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[0])
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_nullSalt() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(null)
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_iterationCountNotSet() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_iterationCountLessThan1() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(0)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test
  public void testBuild_iterationCountEqualTo1() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthNotSet() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthLessThan8() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(7)
        .build();
  }

  @Test
  public void testBuild_derivedKeyBitlengthEqualTo8() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void testBuild_derivedKeyBitlengthNotAMultipleOf8() {
    Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(9)
        .build();
  }

  @Test
  public void testBuild_allValidValues() {
    final Pbkdf2WithHmacSha256Parameters parameters = Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build();

    assertThat(parameters.getSalt(), is(new byte[]{1}));
    assertThat(parameters.getIterationCount(), is(1));
    assertThat(parameters.getDerivedKeyBitlength(), Is.is(8));
  }

  @Test
  public void testToBuilderThenBuild() {
    final Pbkdf2WithHmacSha256Parameters parameters = Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setIterationCount(1)
        .setDerivedKeyBitlength(8)
        .build()
        .toBuilder()
        .build();

    assertThat(parameters.getSalt(), is(new byte[]{1}));
    assertThat(parameters.getIterationCount(), is(1));
    assertThat(parameters.getDerivedKeyBitlength(), Is.is(8));
  }
}