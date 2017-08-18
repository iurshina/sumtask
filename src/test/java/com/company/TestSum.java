package com.company;

import org.junit.Assert;
import org.junit.Test;

public class TestSum {

    @Test
    public void basicTest() {
        Assert.assertEquals(UnsignedIntSum.sumUnsignedInts("src/test/resources/simple"), 15);
    }
}
