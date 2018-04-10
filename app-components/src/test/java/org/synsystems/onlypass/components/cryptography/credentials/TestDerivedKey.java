package org.synsystems.onlypass.components.cryptography.credentials;

import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ConstantConditions")
public class TestDerivedKey {
  @Test(expected = RuntimeException.class)
  public void testCreate_nullKey() {
    DerivedKey.create(null);
  }

  @Test
  public void testCreate_nonNullKey() {
    final SecretKeySpec key = mock(SecretKeySpec.class);

    final DerivedKey derivedKey = DerivedKey.create(key);

    assertThat(derivedKey.getKey(), is(key));
  }
}