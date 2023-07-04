package org.criptografia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * A TextChunk is a string that can be viewed as either a
 * Java String or as a BigInteger encoding the 8-bit characters
 * in the string as a giant integer value.  Either view can
 * be used to construct a new TextChunk.  The characters in
 * the string must be ISO Latin-1 8-bit characters with int
 * values in the range 0 to 255.  No checking is done to verify
 * this condition.
 *
 * @author Hal Perkins
 * @version 10/30/01
 */
public class TextChunk {
    // instance variables
    String stringVal;       // this TextChunk as a Java String
    // 8-bit ISO Latin-1 characters only.
    // The first characters in stringVal
    // are the least significant part of
    // the BigInteger representation.

    /**
     * Construct this TextChunk from a string
     */
    public TextChunk(String s) {
        stringVal = s;
    }

    /**
     * Return the string representation of this TextChunk
     */
    public String toString() {
        return stringVal;
    }

    /**
     * Construct this TextChunk from a BigInteger
     */
    public TextChunk(BigInteger n) {
        BigInteger big256 = BigInteger.valueOf(256);
        BigInteger big0 = BigInteger.ZERO;

        if (n.compareTo(big0) == 0) {
            stringVal = "0";
        } else {
            // extract characters and append to front of string
            // until remaining number is 0
            stringVal = "";
            while (n.compareTo(big0) > 0) {
                BigInteger[] ans = n.divideAndRemainder(big256);
                int charNum = ans[1].intValue();
                stringVal = stringVal + (char) charNum;
                n = ans[0];
            }
        }
    }


    /**
     * Return big integer encoding of this TextChunk.
     */
    public BigInteger bigIntValue() {
        BigInteger big256 = new BigInteger("256");
        BigInteger result = new BigInteger("0");

        // in this encoding, first characters of stringVal are the
        // least significant part of the result
        for (int i = stringVal.length() - 1; i >= 0; i--) {
            result = result.multiply(big256);
            result = result.add(BigInteger.valueOf((int) stringVal.charAt(i)));
        }
        return result;
    }


    /**
     * Return number of Latin-1 (8-bit) characters that can be encoded
     * in a big integer < N.
     * Utility function for RSA encoding.
     */
    public static int blockSize(BigInteger n) {
        // value computed is log_2(N-1)
        BigInteger big1 = BigInteger.ONE;
        BigInteger big2 = BigInteger.TWO;

        int blockSize = 0;  // result
        BigInteger temp = n.subtract(big1);
        while (temp.compareTo(big1) > 0) {
            temp = temp.divide(big2);
            blockSize++;
        }
        return blockSize / 8;
    }

    public static List<String> splitChunk(String textFile, int blockSize) {
        int textFileLenght = textFile.length();
        StringBuilder sb = new StringBuilder(textFile);
        int indexToStop;

        List<String> stringList = new ArrayList<>();
        for (int indexFrom = 0; indexFrom < textFileLenght; indexFrom += blockSize) {
            indexToStop = indexFrom + blockSize;
            if (indexToStop > textFileLenght) {
                indexToStop = textFileLenght;
            }

            stringList.add(sb.substring(indexFrom, indexToStop));
        }

        return stringList;
    }

    /**
     * test program
     */
    public static void main(String[] args) {
        String test = "asdfasdf232435@#%@";
        TextChunk chunk1 = new TextChunk(test);
        BigInteger n = chunk1.bigIntValue();
        System.out.println("biginteger value of " + test + " = " + n);
        System.out.println("blocksize for that is " + blockSize(n));
        TextChunk chunk2 = new TextChunk(n);
        String s = chunk2.toString();
        System.out.println("converted back to string = " + s);
        if (s.compareTo(test) == 0) {
            System.out.println("success");
        } else {
            System.out.println("FAIL");
        }
    }
}
