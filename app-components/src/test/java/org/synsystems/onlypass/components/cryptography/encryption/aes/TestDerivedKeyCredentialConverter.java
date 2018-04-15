package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class TestDerivedKeyCredentialConverter {
  private DerivedKeyCredentialConverter converter;

  @Before
  public void setup() {
    converter = new DerivedKeyCredentialConverter();
  }

  @Test(expected = RuntimeException.class)
  public void testToSecretKeySpec_nullCredential() {
    converter.toSecretKeySpec(null);
  }
}