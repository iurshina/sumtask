package com.company;

import org.junit.Assert;
import org.junit.Test;

public class TestSum {

    @Test
    public void basicTest() {
        Assert.assertEquals(15, UnsignedIntSum.sumUnsignedInts("src/test/resources/simple"));
    }
}
