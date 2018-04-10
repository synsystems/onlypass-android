package org.synsystems.onlypass.components.cryptography.random;

import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@SuppressWarnings("ConstantConditions")
public class TestSecureByteArrayGenerator {
  private SecureByteArrayGenerator generator;

  @Before
  public void setup() {
    generator = new SecureByteArrayGenerator(new SecureRandom());
  }

  @Test(expected = RuntimeException.class)
  public void testConstructor_nullSecureRandom() {
    new SecureByteArrayGenerator(null);
  }

  @Test(expected = RuntimeException.class)
  public void testGenerateWithLength_lengthOfNegativeOne() {
    generator.generateWithLength(-1);
  }

  @Test
  public void testGenerateWithLength_lengthOfZero() {
    generator
        .generateWithLength(0)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> value.length == 0)
        .assertComplete();
  }

  @Test
  public void testGenerateWithLength_lengthOOne() {
    generator
        .generateWithLength(1)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> value.length == 1)
        .assertComplete();
  }
}