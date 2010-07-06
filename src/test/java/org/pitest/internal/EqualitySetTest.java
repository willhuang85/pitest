package org.pitest.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EqualitySetTest {

  EqualitySet<String> testee;

  @Before
  public void createTestee() {
    final EqualityStrategy<String> e = new EqualityStrategy<String>() {
      public boolean isEqual(final String lhs, final String rhs) {
        return lhs.equals(rhs);
      }
    };
    this.testee = new EqualitySet<String>(e);
  }

  @Test
  public void testCanIterateMembers() {
    this.testee.add("one");
    this.testee.add("two");
    final List<String> expected = Arrays.asList("one", "two");
    final List<String> actual = new ArrayList<String>();
    for (final String each : this.testee) {
      actual.add(each);
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testContainsReturnsTrueWhenMemberPresent() {
    this.testee.add("one");
    assertTrue(this.testee.contains("one"));
  }

  @Test
  public void testContainsReturnsFalseWhenMemberNotPresent() {
    this.testee.add("one");
    assertFalse(this.testee.contains("two"));
  }

  @Test
  public void testToCollectionReturnsAllMembers() {
    this.testee.add("one");
    this.testee.add("two");
    final List<String> expected = Arrays.asList("one", "two");
    assertEquals(expected, this.testee.toCollection());
  }

  @Test
  public void testCanOnlyAddOneInstanceOfEachValue() {
    this.testee.add("one");
    this.testee.add("two");
    this.testee.add("one");
    this.testee.add("two");
    final List<String> expected = Arrays.asList("one", "two");
    assertEquals(expected, this.testee.toCollection());
  }

  @Test
  public void testCanOnlyAddOneInstanceOfEachValueViaAddAll() {
    final List<String> expected = Arrays.asList("one", "two");
    this.testee.addAll(expected);
    this.testee.addAll(expected);
    assertEquals(expected, this.testee.toCollection());
  }

  @Test
  public void testIsEmptyReturnsTrueWhenEmpty() {
    assertTrue(this.testee.isEmpty());
  }

  @Test
  public void testIsEmptyReturnsFalseWhenNotEmpty() {
    this.testee.add("foo");
    assertFalse(this.testee.isEmpty());
  }

  @Test
  public void testSizeReturnsNumberofEntries() {
    assertEquals(0, this.testee.size());
    this.testee.add("foo");
    assertEquals(1, this.testee.size());
  }
}