package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyException;
import java.util.HashMap;

public class Criptoanalizer {
    private static final int LENGTHALPHADETRU = 77;
    private static final int LENGTHALPHADETEN = 63;
    private static final int SIZEBUFFERS = 2048;
    private static final int MAXUSERKEY = 60;
    private static final int MINUSERKEY = 0;
    private static StringBuilder builder = new StringBuilder();
    private static char[] alfafitCharacters;
    private static long lengthFiles;
    private static long positionRead;
    private static HashMap<Character, Character> encryptionTable = new HashMap<>();
    public static int brutforsKey;

    public static void encrypt( File outFile, int userKey) throws KeyException {
        createEncryptTables(userKey);
        String newFileName = "_encrypt";
        Path path = Paths.get(outFile.getPath());
        File file = Paths.get(getNewFileName(path.toFile().getAbsolutePath(), newFileName)).toFile();
        try (FileChannel writeFileChannel = new FileOutputStream(file).getChannel()){
            writeFiles(outFile, writeFileChannel);
        } catch (IOException ignored){
            System.out.println(ignored.getMessage());
        }
    }

    public static void decrypt(File outFile, int userKey) throws KeyException {
        createDecryptTables(userKey);
        String newFileName = "_decrypt";
        Path path = Paths.get(outFile.getPath());
        File file = Paths.get(getNewFileName(path.toFile().getAbsolutePath(), newFileName)).toFile();
        try (FileChannel writeFileChannel = new FileOutputStream(file).getChannel()){
            writeFiles(outFile, writeFileChannel);
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    private static void writeFiles(File outFile, FileChannel writeFileChannel) throws IOException {
        boolean encrypt = true;
        positionRead = 0;
        while (encrypt) {
            readFiles(outFile);
            coding();
            String someText = builder.toString();
            ByteBuffer writeBuffer = ByteBuffer.wrap(someText.getBytes(StandardCharsets.UTF_8));
            writeFileChannel.write(writeBuffer);
            writeBuffer.clear();
            builder.delete(0, SIZEBUFFERS);
            if (lengthFiles == 0) {
                encrypt = false;
            }
        }
    }

    public static void decryptorBrutforse(File outFile) throws IOException, KeyException {
        brutforsKey = 0;
        createDecryptTables(brutforsKey);

        boolean needBrutforse = true;

        while (needBrutforse) {
            positionRead = 0;
            readFiles(outFile);
            coding();
            String someText = builder.toString();
            for (int i = 0; i < someText.length() - 10; i += 1) {
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
            System.out.println(ignored.getMessage());
        }
    }

    public static void createAlphabet(String language) throws IOException {

        Path alphadetru = Path.of("src/main/resources/alfafitru.txt");
        Path alphadeten = Path.of("src/main/resources/alfafiten.txt");

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
    private static void createDecryptTables(int userKey) throws KeyException {
        checkKey(userKey);
        int shift = userKey;
        for (int i = 0; i < alfafitCharacters.length; i++) {
            if (shift == alfafitCharacters.length) {
                shift = 0;
            }
            encryptionTable.put(alfafitCharacters[shift], alfafitCharacters[i]);
            shift++;
        }
    }

    private static void createEncryptTables(int userKey) throws KeyException {
        checkKey(userKey);
        int shift = userKey;
        for (int i = 0; i < alfafitCharacters.length; i++) {
            if (shift == alfafitCharacters.length) {
                shift = 0;
            }
            encryptionTable.put(alfafitCharacters[i], alfafitCharacters[shift]);
            shift++;
        }

    }

    private static int checkKey(int userKey) throws KeyException {
        if (userKey < MINUSERKEY){
            throw new KeyException("The key cannot be less than 0");
        } else if (userKey > MAXUSERKEY){
            throw new KeyException("The key cannot be less than 60");
        }
        return userKey;
    }

    private static String getNewFileName(String oldFileName, String name) {
        int dotIndex = oldFileName.lastIndexOf(".");
        return oldFileName.substring(0, dotIndex) + name + oldFileName.substring(dotIndex);
    }

}
