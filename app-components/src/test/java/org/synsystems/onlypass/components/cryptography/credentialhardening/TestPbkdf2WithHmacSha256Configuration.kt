package org.synsystems.onlypass.components.cryptography.credentialhardening

import org.junit.Test

class TestPbkdf2WithHmacSha256Configuration {
  @Test(expected = RuntimeException::class)
  fun testBuild_emptySaltSet() = Pbkdf2WithHmacSha256Configuration(arrayOf(), 1, 8)

  @Test
  fun testBuild_nonEmptySaltSet() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 8)

  @Test(expected = RuntimeException::class)
  fun testBuild_iterationCountLessThan1() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 0, 8)

  @Test
  fun testBuild_iterationCountEqualTo1() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 8)

  @Test
  fun testBuild_iterationCountGreaterThan1() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 2, 8)

  @Test(expected = RuntimeException::class)
  fun testBuild_derivedKeyBitlengthLessThan8() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 7)

  @Test
  fun testBuild_derivedKeyBitlengthEqualTo8() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 8)

  @Test
  fun testBuild_derivedKeyBitlengthGreaterThan8() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 16)

  @Test(expected = RuntimeException::class)
  fun testBuild_derivedKeyBitlengthNotAMultipleOf8() = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 1, 9)
}