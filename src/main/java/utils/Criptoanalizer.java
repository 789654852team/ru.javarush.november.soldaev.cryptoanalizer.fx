package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;

public class Criptoanalizer {

    private static final int LENGTHALPHADETRU = 77;
    private static final int LENGTHALPHADETEN = 63;
    private static final int SIZEBUFFERS = 1024;
    private static final int MAXUSERKEY = 60;
    private static StringBuilder builder = new StringBuilder();
    private static String newFileName;
    private static char[] alfafitCharacters;
    private static long lengthFiles;
    private static long positionRead;
    private static HashMap<Character, Character> encryptionTable = new HashMap<>();
    public static int brutforsKey;

    public static void encrypt(File outFile, int userKey) throws IOException {

        createEncryptTables(userKey);
        newFileName = "_encrypt";
        Path path = Paths.get(outFile.getPath());
        File file = Paths.get(getNewFileName(path.toFile().getAbsolutePath(), newFileName)).toFile();
        FileChannel writeFileChannel = new FileOutputStream(file).getChannel();

        boolean write = true;
        positionRead = 0;

        while (write) {
            readFiles(outFile);

            coding();

            String someText = builder.toString();
            ByteBuffer writeBuffer = ByteBuffer.wrap(someText.getBytes(StandardCharsets.UTF_8));
            writeFileChannel.write(writeBuffer);
            writeBuffer.clear();
            builder.delete(0, SIZEBUFFERS);

            if (lengthFiles == 0) {
                write = false;
            }
        }
        writeFileChannel.close();

    }

    public static void decrypt(File outFile, int userKey) throws IOException {
        createDecryptTables(userKey);

        newFileName = "_decrypt";
        Path path = Paths.get(outFile.getPath());
        File file = Paths.get(getNewFileName(path.toFile().getAbsolutePath(), newFileName)).toFile();
        FileChannel writeFileChannel = new FileOutputStream(file).getChannel();

        boolean decrypt = true;
        positionRead = 0;
        while (decrypt) {
            readFiles(outFile);

            coding();

            String someText = builder.toString();
            ByteBuffer writeBuffer = ByteBuffer.wrap(someText.getBytes(StandardCharsets.UTF_8));
            writeFileChannel.write(writeBuffer);
            writeBuffer.clear();
            builder.delete(0, SIZEBUFFERS);

            if (lengthFiles == 0) {
                decrypt = false;
            }
        }
        writeFileChannel.close();
    }

    public static void decryptorBrutforse(File outFile) throws IOException {

        brutforsKey = 1;
        createDecryptTables(brutforsKey);

        newFileName = "decryptbrutfors";

        boolean needBrutforse = true;

        while (needBrutforse) {
            readFiles(outFile);
            coding();

            String someText = builder.toString();

            for (int i = 0; i < someText.length() - 10; i += 2) {
                if (builder.substring(i + 1, i + 3).equals(". ")) {
                    builder.delete(0, SIZEBUFFERS);
                    decrypt(outFile, brutforsKey);
                    needBrutforse = false;
                    break;
                }
            }
            if (needBrutforse) {
                brutforsKey++;
                createDecryptTables(brutforsKey);
                builder.delete(0, SIZEBUFFERS);
            }
        }
    }


    private static void coding() {
        for (int i = 0; i < builder.length(); i++) {
            char temp = builder.charAt(i);
            if (encryptionTable.get(temp) == null) {
                continue;
            }
            builder.setCharAt(i, encryptionTable.get(temp));
        }

    }


    private static void readFiles(File readFiles) {

        try (FileChannel readFileChannel = new FileInputStream(readFiles).getChannel()) {

            readFileChannel.position(positionRead);
            lengthFiles = readFileChannel.size();

            ByteBuffer readBuffer = ByteBuffer.allocate(SIZEBUFFERS);
            readFileChannel.read(readBuffer);
            readBuffer.flip();

            while (readBuffer.hasRemaining()) {
                builder.append(StandardCharsets.UTF_8.decode(readBuffer));
            }

            positionRead += readBuffer.position();
            lengthFiles -= positionRead;
            readBuffer.clear();

        } catch (IOException ignored) {

        }
    }

    public static void createAlphabet(String language) throws IOException {
        Path alphadetru = Path.of("src/alfafitCriptoanalizer/alfafitru.txt");
        Path alphadeten = Path.of("src/alfafitCriptoanalizer/alfafiten.txt");

        if (language.equals("ru")) {
            FileReader reader = new FileReader(alphadetru.toFile(), StandardCharsets.UTF_8);

            alfafitCharacters = new char[LENGTHALPHADETRU];
            while (reader.ready()) {
                reader.read(alfafitCharacters);
            }
        } else if (language.equals("en")) {
            FileReader reader = new FileReader(alphadeten.toFile(), StandardCharsets.UTF_8);

            alfafitCharacters = new char[LENGTHALPHADETEN];
            while (reader.ready()) {
                reader.read(alfafitCharacters);
            }
        }

    }

    private static void createDecryptTables(int userKey) {

        while (userKey > MAXUSERKEY) {
            userKey -= MAXUSERKEY;
        }

        for (int i = 0; i < alfafitCharacters.length; i++) {
            if (userKey == alfafitCharacters.length) {
                userKey = 0;
            }
            encryptionTable.put(alfafitCharacters[userKey], alfafitCharacters[i]);
            userKey++;

        }
    }

    private static void createEncryptTables(int userKey) {

        while (userKey > MAXUSERKEY) {
            userKey -= MAXUSERKEY;
        }
        for (int i = 0; i < alfafitCharacters.length; i++) {
            if (userKey == alfafitCharacters.length) {
                userKey = 0;
            }
            encryptionTable.put(alfafitCharacters[i], alfafitCharacters[userKey]);
            userKey++;
        }

    }

    private static String getNewFileName(String oldFileName, String name) {
        int dotIndex = oldFileName.lastIndexOf(".");
        return oldFileName.substring(0, dotIndex) + name + oldFileName.substring(dotIndex);
    }

}
