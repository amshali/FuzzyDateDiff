package fuzzydatediff;

import org.junit.Test;

import junit.framework.Assert;

public class TestFuzzyDiff {

  @Test
  public void test1() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("12/30/2011 4:5 PM, PDT", "1/2/12 7:45 AM, PDT",
        "M/d/y h:m a, z");

    Assert.assertEquals("In 2 days and 15 hours", dString);
  }

  @Test
  public void test11() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("12/30/2011 4:5 PM, PDT",
        "12/30/2011 4:5 PM, PDT", "M/d/y h:m a, z");

    Assert.assertEquals("Just now", dString);
  }

  @Test
  public void test12() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("12/30/2011 4:5 PM, PDT",
        "7/3/2013 11:35 PM, CDT", "M/d/y h:m a, z");

    Assert.assertEquals("In 1 year and 6 months", dString);
  }

  @Test
  public void test13() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("7/3/2011 4:5 PM, PDT", "7/3/2008 4:35 PM, CDT",
        "M/d/y h:m a, z");

    Assert.assertEquals("3 years ago", dString);
  }

  @Test
  public void test2() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("4:8:12 AM", "4:5:16 AM", "h:m:s a");
    Assert.assertEquals("2 minutes and 56 seconds ago", dString);
  }

  @Test
  public void test3() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("4:8:12 AM", "4:5:12 AM", "h:m:s a");
    Assert.assertEquals("3 minutes ago", dString);
  }

  @Test
  public void test4() {
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    String dString = fdd.diff("5:8:12 AM", "7:8:12 AM", "h:m:s a");
    Assert.assertEquals("In 2 hours", dString);
  }
}
