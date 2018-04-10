package org.synsystems.onlypass.components.cryptography.credentials;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class TestCleartextPassword {
  @Test(expected = RuntimeException.class)
  public void testCreate_nullPassword() {
    CleartextPassword.create(null);
  }

  @Test
  public void testCreate_nonNullPassword() {
    final CleartextPassword credential = CleartextPassword.create("password");

    assertThat(credential.getPassword(), is("password"));
  }
}