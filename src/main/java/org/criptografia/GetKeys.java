package org.criptografia;

import java.io.*;
import java.math.BigInteger;

/**
 * Calculate a private key from two prime numbers and a public exponent.
 *
 * @author Hal Perkins
 * @version 10/30/01
 */
public class GetKeys {
    private static boolean goodkey;     // true if last key was ok

    // return private exponent for modulus P*Q and public exponent E,
    // where P and Q are distinct primes and Phi = (P-1)*(Q-1).
    // Side effect: set goodkey true if private key was computed
    // successfully, otherwise set it to false
    private static BigInteger getPrivateExp(BigInteger Phi, BigInteger E) {
        // public key calculation; see sources for RSA algorithm
        BigInteger big0 = new BigInteger("0");
        BigInteger big1 = new BigInteger("1");

        BigInteger r1 = E;
        BigInteger D = new BigInteger("1");
        BigInteger[] quotrem = Phi.divideAndRemainder(E);
        BigInteger quot = quotrem[0];
        BigInteger r2 = quotrem[1];
        BigInteger x = Phi.subtract(quot);

        while (r2.compareTo(big0) != 0) {
            quotrem = r1.divideAndRemainder(r2);
            quot = quotrem[0];
            BigInteger rem = quotrem[1];
            r1 = r2;
            r2 = rem;
            BigInteger temp = x;
            x = ((Phi.add(D)).subtract(quot.multiply(x).mod(Phi))).mod(Phi);
            D = temp;
        }
        goodkey = (r1.compareTo(big1) == 0);
        return D;
    }

    public static void main(String[] args) throws IOException {
        BigInteger big1 = new BigInteger("1");
        // Set up input from console
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println();
        System.out.println("GetKeys");

        // prompt for and read two prime numbers
        System.out.println("Please enter two distinct prime numbers");
        System.out.println("Note: there is no check whether these numbers");
        System.out.println("are distinct primes.  Encryption/decryption");
        System.out.println("won't work unless they are.");
        System.out.println();

        System.out.print("first prime: ");
        String pstr = in.readLine();
        BigInteger P = new BigInteger(pstr);
        System.out.print("second prime: ");
        String qstr = in.readLine();
        BigInteger Q = new BigInteger(qstr);

        BigInteger D = null;
        BigInteger E = null;

        // keep trying until we get a good key
        goodkey = false;
        int estr = 100;
        while (!goodkey) {
            System.out.println();
            System.out.println("Enter public exponent (key): ");
//            String estr = in.readLine();
            E = new BigInteger(String.valueOf(estr));
            D = getPrivateExp(
                    (P.subtract(big1)).multiply(Q.subtract(big1)), E);
            if (!goodkey) {
                saveKeyToFile("src/main/resources/chavePublica.txt", P.toString(), E.toString());
                saveKeyToFile("src/main/resources/chavePrivada.txt", P.toString(), D.toString());
                System.out.println("No private exponent (key) is associated");
                System.out.println("with that public key.  Please try again.");
                estr++;
            }
        }

        System.out.println();
        System.out.println("The modulus, public and private key are as follows.");
        System.out.println("Modulus    : " + P.multiply(Q));
        System.out.println("Private Exp: " + D);
        System.out.println("Public Exp: " + E);
    }

    private static void saveKeyToFile(String filename, String module, String key) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(module);
        writer.newLine();
        writer.write(key);
        writer.newLine();
        writer.close();
    }
}

