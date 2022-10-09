package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Criptoanalizer {
    private static final int LENGTHALPHADETRU = 77;
    private static final int LENGTHALPHADETEN = 63;
    private static final int SIZEBUFFERS = 1024;
    private static final int MAXUSERKEY = 60;
    private static final int MINUSERKEY = 0;
    private static final int INDENT = 5;
    public static int brutforsKey;
    private static long lengthFiles;
    private static long positionRead;
    private static StringBuilder builder = new StringBuilder();
    private static char[] alfafitCharacters;
    private static HashMap<Character, Character> encryptionTable = new HashMap<>();

    public static void encrypt(File outFile, int userKey) throws TranscriptionalException, IOException {
        checkFileFormat(outFile);
        createEncryptTables(userKey);
        String newFileName = "_encrypt";
        writeFiles(outFile, newFileName);
    }

    public static void decrypt(File outFile, int userKey) throws TranscriptionalException, IOException {
        checkFileFormat(outFile);
        createDecryptTables(userKey);
        String newFileName = "_decrypt";
        writeFiles(outFile, newFileName);
    }

    private static void writeFiles(File outFile, String newFileName) throws IOException {
        File file = getNewFileName(outFile.getAbsolutePath(), newFileName);
        try (FileChannel writeFileChannel = new FileOutputStream(file).getChannel()) {
            boolean needCoding = true;
            positionRead = 0;
            while (needCoding) {
                readFiles(outFile);
                coding();
                String writeText = builder.toString();
                ByteBuffer writeBuffer = ByteBuffer.wrap(writeText.getBytes(StandardCharsets.UTF_8));
                writeFileChannel.write(writeBuffer);
                writeBuffer.clear();
                builder.delete(0, SIZEBUFFERS);
                if (lengthFiles == 0) {
                    needCoding = false;
                    encryptionTable.clear();
                    writeBuffer.clear();
                }
            }
        } catch (IOException io) {
            throw new IOException();
        }

    }

    public static void decryptorBrutforse(File outFile) throws TranscriptionalException, IOException {
        checkFileFormat(outFile);
        brutforsKey = 0;
        createDecryptTables(brutforsKey);
        boolean needBrutforse = true;
        while (needBrutforse) {
            positionRead = 0;
            readFiles(outFile);
            coding();
            needBrutforse = findMatch();
            if (needBrutforse) {
                brutforsKey++;
                if (brutforsKey > 60) {
                    throw new TranscriptionalException("Простите Brutforse не выполнен! Файл слишком маленький или не найдено совпадений!");
                }
                createDecryptTables(brutforsKey);
                builder.delete(0, SIZEBUFFERS);
            } else {
                builder.delete(0, SIZEBUFFERS);
                decrypt(outFile, brutforsKey);
            }
        }
    }
    private static boolean findMatch (){
        int matchСounter = 0;
        String brutforseText = builder.toString();
        for (int i = 1; i < brutforseText.length() - INDENT; i++) {
            if (builder.substring(i, i + 2).equals(". ")) {
                matchСounter++;
                if (matchСounter == 3) {
                    break;
                }
            }
        }
        for (int i = 1; i < brutforseText.length() - INDENT; i++) {
            if (builder.substring(i, i + 2).equals(", ")) {
                matchСounter++;
                if (matchСounter == 6) {
                    return false;
                }
            }
        }
        return true;
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

    private static void readFiles(File readFiles) throws IOException {

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
        } catch (IOException io) {
            throw new IOException();
        }
    }

    public static void createAlphabet(String language) throws IOException {
        Path alphadetru = Path.of("src/main/resources/alfafitru.txt");
        Path alphadeten = Path.of("src/main/resources/alfafiten.txt");
        ArrayList<Character> characters = new ArrayList<>();
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

    private static void createDecryptTables(int userKey) throws TranscriptionalException {
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

    private static void createEncryptTables(int userKey) throws TranscriptionalException {
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

    private static void checkKey(int userKey) throws TranscriptionalException {
        if (userKey < MINUSERKEY) {
            throw new TranscriptionalException(String.format("Ключ должен быть больше %d и состоять только из цифр.", MINUSERKEY));
        } else if (userKey > MAXUSERKEY) {
            throw new TranscriptionalException(String.format("Ключ не может быть больше %d и состоять только из цифр.", MAXUSERKEY));
        }
    }

    private static File getNewFileName(String oldFileName, String name) {
        int couterFiles = 1;
        int dotIndex = oldFileName.lastIndexOf(".");
        File file = Paths.get(oldFileName.substring(0, dotIndex) + name + oldFileName.substring(dotIndex)).toFile();
        while (file.exists()) {
            couterFiles++;
            file = Paths.get(oldFileName.substring(0, dotIndex) + name + couterFiles + oldFileName.substring(dotIndex)).toFile();
        }
        return file;
    }

    private static void checkFileFormat(File outFile) throws TranscriptionalException {
        String fileName = outFile.getName();
        int lengthFile = fileName.length();
        int indexFormat = fileName.lastIndexOf(".");
        String checkFormat = fileName.substring(indexFormat, lengthFile);
        if (!(checkFormat.equals(".txt"))) {
            throw new TranscriptionalException("Недопустимый формат файла, программа работает только с файлами \"txt\".");
        }
    }
}

