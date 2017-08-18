package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        if (filePath == null) {
            throw new RuntimeException("Filepath is not provided.");
        }

        System.out.println(Long.toUnsignedString(UnsignedIntSum.sumUnsignedInts(filePath)));
    }
}
