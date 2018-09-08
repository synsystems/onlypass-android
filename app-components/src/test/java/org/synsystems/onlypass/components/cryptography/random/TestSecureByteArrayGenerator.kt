package org.synsystems.onlypass.components.cryptography.random

import org.junit.Before
import org.junit.Test

import java.security.SecureRandom

import java.util.concurrent.TimeUnit.MILLISECONDS

class TestSecureByteArrayGenerator {
  
  private lateinit var generator: SecureByteArrayGenerator

  @Before
  fun setup() {
    generator = SecureByteArrayGenerator(SecureRandom())
  }

  @Test(expected = RuntimeException::class)
  fun testGenerateWithLength_lengthOfNegativeOne() {
    generator.generateWithLength(-1)
  }

  @Test
  fun testGenerateWithLength_lengthOfZero() {
    generator
        .generateWithLength(0)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue { value -> value.isEmpty() }
        .assertComplete()
  }

  @Test
  fun testGenerateWithLength_lengthOfOne() {
    generator
        .generateWithLength(1)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue { value -> value.size == 1 }
        .assertComplete()
  }
}