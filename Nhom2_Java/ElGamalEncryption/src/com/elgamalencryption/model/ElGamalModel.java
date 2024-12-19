/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elgamalencryption.model;

/**
 *
 * @author adm
 */


import com.elgamalencryption.util.MathUtils;
import java.math.BigInteger;
import java.util.Base64;

public class ElGamalModel {
    private BigInteger p, g, x, y, k;

    public void generateKeys(int bitLength) {
        p = MathUtils.generateLargePrime(bitLength);
        g = MathUtils.findPrimitiveRoot(p);
        x = MathUtils.generateRandomBigInteger(BigInteger.TWO, p.subtract(BigInteger.TWO));
        y = g.modPow(x, p);
        k = MathUtils.generateRandomBigInteger(BigInteger.TWO, p.subtract(BigInteger.TWO));
    }

    public String encrypt(String message) {
        byte[] messageBytes = message.getBytes();
        BigInteger m = new BigInteger(messageBytes);
        
        BigInteger c1 = g.modPow(k, p);
        BigInteger c2 = m.multiply(y.modPow(k, p)).mod(p);
        
        String encodedC1 = Base64.getEncoder().encodeToString(c1.toByteArray());
        String encodedC2 = Base64.getEncoder().encodeToString(c2.toByteArray());
        
        return encodedC1 + ";" + encodedC2;
    }

    public String decrypt(String ciphertext) {
        String[] parts = ciphertext.split(";");
        BigInteger c1 = new BigInteger(Base64.getDecoder().decode(parts[0]));
        BigInteger c2 = new BigInteger(Base64.getDecoder().decode(parts[1]));
        
        BigInteger s = c1.modPow(x, p);
        BigInteger m = c2.multiply(s.modInverse(p)).mod(p);
        
        return new String(m.toByteArray());
    }

    // Getters and setters
    public BigInteger getP() { return p; }
    public BigInteger getG() { return g; }
    public BigInteger getY() { return y; }
    public BigInteger getX() { return x; }
    public BigInteger getK() { return k; }

    public void setP(BigInteger p) { this.p = p; }
    public void setG(BigInteger g) { this.g = g; }
    public void setY(BigInteger y) { this.y = y; }
    public void setX(BigInteger x) { this.x = x; }
    public void setK(BigInteger k) { this.k = k; }
}