package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class TestHashingSalt {
  @Test(expected = RuntimeException.class)
  public void testCreate_nullSalt() {
    HashingSalt.create(null);
  }

  @Test
  public void testCreate_emptySalt() {
    final HashingSalt hashingSalt = HashingSalt.create(new byte[0]);

    assertThat(hashingSalt.getSalt(), is(new byte[0]));
  }

  @Test
  public void testCreate_nonEmptySalt() {
    final HashingSalt hashingSalt = HashingSalt.create(new byte[]{1, 2, 3});

    assertThat(hashingSalt.getSalt(), is(new byte[]{1, 2, 3}));
  }
}