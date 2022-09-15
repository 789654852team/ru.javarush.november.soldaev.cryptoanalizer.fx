package utils;

import java.io.File;
import java.io.IOException;

public class Crypto {

    protected File inFiles;
    protected int userKey;
    protected String Language;


    public Crypto(File inFiles, int userKey, String Language) {
        this.inFiles = inFiles;
        this.userKey = userKey;
        this.Language = Language;
    }

    public Crypto(File inFiles, String Language) {
        this.inFiles = inFiles;
        this.Language = Language;
    }

    public static void encrypt(Crypto crypto) throws IOException {
        String whoColled = "encrypt";
        Cryptomethods.doEncryptOrDecrytion(crypto.inFiles, crypto.userKey, crypto.Language, whoColled);
    }

    public static void decrypt(Crypto crypto) throws IOException {
        String whoColled = "decrypt";
        Cryptomethods.doEncryptOrDecrytion(crypto.inFiles, crypto.userKey, crypto.Language, whoColled);
    }

    public static void DecryptorBrutforse(Crypto crypto) throws IOException {
        String whoColled = "decrypt";
        Cryptomethods.toBrutfors(crypto.inFiles, crypto.Language, whoColled);
    }

}
