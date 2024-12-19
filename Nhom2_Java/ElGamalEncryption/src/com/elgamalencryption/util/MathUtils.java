/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author adm
 */
package com.elgamalencryption.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MathUtils {
    private static final SecureRandom random = new SecureRandom();

    public static BigInteger generateLargePrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, random);
    }

    public static BigInteger generateRandomBigInteger(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        int bitLength = range.bitLength();
        BigInteger result;
        do {
            result = new BigInteger(bitLength, random);
        } while (result.compareTo(range) >= 0);
        return result.add(min);
    }

    public static BigInteger findPrimitiveRoot(BigInteger p) {
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger q = pMinusOne.divide(BigInteger.TWO);
        
        for (BigInteger g = BigInteger.TWO; g.compareTo(p) < 0; g = g.add(BigInteger.ONE)) {
            if (g.modPow(pMinusOne, p).equals(BigInteger.ONE) &&
                !g.modPow(q, p).equals(BigInteger.ONE)) {
                return g;
            }
        }
        throw new RuntimeException("Primitive root not found");
    }
}
