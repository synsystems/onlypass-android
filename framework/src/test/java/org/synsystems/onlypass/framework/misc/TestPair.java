package org.synsystems.onlypass.framework.misc;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class TestPair {
  private static final String s1 = "s1";

  private static final String s2 = "s2";

  @Test(expected = RuntimeException.class)
  public void testCreate_nullKey() {
    Pair.create(null, new Object());
  }

  @Test(expected = RuntimeException.class)
  public void testCreate_nullValue() {
    Pair.create(new Object(), null);
  }

  @Test
  public void testCreateThenGetKey() {
    final Pair<String, String> pair = Pair.create(s1, s2);

    assertThat(pair.getKey(), is(s1));
  }

  @Test
  public void testCreateThenGetKeyRx() {
    final Pair<String, String> pair = Pair.create(s1, s2);

    pair
        .getKeyRx()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValue(s1)
        .assertComplete();
  }

  @Test
  public void testCreateThenGetValue() {
    final Pair<String, String> pair = Pair.create(s1, s2);

    assertThat(pair.getValue(), is(s2));
  }

  @Test
  public void testCreateThenGetValueRx() {
    final Pair<String, String> pair = Pair.create(s1, s2);

    pair
        .getValueRx()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValue(s2)
        .assertComplete();
  }
}