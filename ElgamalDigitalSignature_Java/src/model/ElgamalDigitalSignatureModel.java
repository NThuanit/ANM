package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class ElgamalDigitalSignatureModel {
    
    private String fileName;
	private String content;
	private BigInteger p, g, x, y;
    private SecureRandom ngauNhien = new SecureRandom();
	
	public ElgamalDigitalSignatureModel(){
		this.fileName = "";
		this.content = "";
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

    public void sinhKhoaTuDong() {
        p = BigInteger.probablePrime(512, ngauNhien);
        g = timCanPrimitive(p);
        x = new BigInteger(p.bitLength() - 1, ngauNhien);
        y = g.modPow(x, p);
    }

    public void sinhKhoaTuChon(String strP, String strG, String strX) {
        p = new BigInteger(strP);
        g = new BigInteger(strG);
        x = new BigInteger(strX);
        y = g.modPow(x, p);
    }

    private BigInteger timCanPrimitive(BigInteger p) {
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger q = pMinusOne.divide(BigInteger.TWO);
        BigInteger g;
        do {
            g = new BigInteger(p.bitLength() - 1, ngauNhien);
        } while (g.modPow(q, p).equals(BigInteger.ONE) || g.compareTo(BigInteger.ONE) <= 0 || g.compareTo(pMinusOne) >= 0);
        return g;
    }

    public String kyVanBan(String vanBan) {
        byte[] bytes = vanBan.getBytes(StandardCharsets.UTF_8);
        BigInteger m = new BigInteger(1, bytes);

        BigInteger k;
        do {
            k = new BigInteger(p.bitLength() - 1, ngauNhien);
        } while (k.compareTo(BigInteger.ONE) <= 0 || k.compareTo(p.subtract(BigInteger.ONE)) >= 0 || !k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

        BigInteger r = g.modPow(k, p);
        BigInteger s = k.modInverse(p.subtract(BigInteger.ONE))
                        .multiply(m.subtract(x.multiply(r)))
                        .mod(p.subtract(BigInteger.ONE));

        return Base64.getEncoder().encodeToString(r.toByteArray()) + ";" + 
               Base64.getEncoder().encodeToString(s.toByteArray());
    }

    public boolean kiemTraChuKy(String vanBan, String chuKy) {
        String[] parts = chuKy.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Chữ ký không hợp lệ");
        }

        BigInteger r = new BigInteger(Base64.getDecoder().decode(parts[0]));
        BigInteger s = new BigInteger(Base64.getDecoder().decode(parts[1]));

        byte[] bytes = vanBan.getBytes(StandardCharsets.UTF_8);
        BigInteger m = new BigInteger(1, bytes);

        if (r.compareTo(BigInteger.ONE) <= 0 || r.compareTo(p.subtract(BigInteger.ONE)) >= 0) {
            return false;
        }

        BigInteger v1 = g.modPow(m, p);
        BigInteger v2 = y.modPow(r, p).multiply(r.modPow(s, p)).mod(p);

        return v1.equals(v2);
    }

    // Getters
    public BigInteger getP() { return p; }
    public BigInteger getG() { return g; }
    public BigInteger getX() { return x; }
    public BigInteger getY() { return y; }
    public void setY(BigInteger y) { this.y = y; }
}

