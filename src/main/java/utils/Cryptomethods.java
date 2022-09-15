package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Cryptomethods {
    private static final Path ALPHADETRU = Path.of("src/needtext/alfafitru.txt");
    private static final Path ALPHADETEN = Path.of("src/needtext/alfafiten.txt");
    private static String newNeme;
    private static FileReader reader;
    private static char[] arrayOfCharacters;
    private static HashMap<Character, Character> encryptionTable = new HashMap<>();

    public static int brutforsKey = 1;

    public static void toBrutfors (File inFiles, String Language, String whoColled) throws IOException {
        createAlphabet(Language);
        File inFile = inFiles;

        try (FileChannel inFileChannel = new FileInputStream(inFile).getChannel()) {


            long count = inFileChannel.size();
            int sizeBuff = 2654;
            StringBuilder builderBrutforse = new StringBuilder();

            ByteBuffer buffer = ByteBuffer.allocate(sizeBuff);
            inFileChannel.read(buffer);
            buffer.flip();

            while (buffer.hasRemaining()) {
                // перекодируем в формат UTF_8
                builderBrutforse.append(StandardCharsets.UTF_8.decode(buffer));
            }

            while (true) {
                boolean prov = true;

                for (int i = 0; i < builderBrutforse.length(); i++) {
                    char temp = builderBrutforse.charAt(i);

                    if (encryptionTable.get(temp) == null) {
                        builderBrutforse.setCharAt(i, temp);
                        continue;
                    }
                    builderBrutforse.setCharAt(i, encryptionTable.get(temp));
                }
                String someText = builderBrutforse.toString();

                for (int i = 30; i < someText.length() - 10; i += 2) {
                    if (builderBrutforse.substring(i + 1, i + 3).equals(". ")) {
                        doEncryptOrDecrytion(inFiles, brutforsKey, Language, "decryptbrutfors");
                        prov = false;
                        break;
                    }
                }
                if (prov) {
                    brutforsKey++;
                    builderBrutforse.delete(0, sizeBuff);
                    buffer.clear();
                    createEncryptAndDecryptTables(brutforsKey, whoColled);
                    toBrutfors(inFiles,Language, whoColled);
                }
                buffer.clear();
                builderBrutforse.delete(0, sizeBuff);
                break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doEncryptOrDecrytion(File inFiles, int userKey, String Language, String whoColled) throws IOException {
        File inFile = inFiles;
        Path path = Paths.get(inFile.getPath());

        createAlphabet(Language);

        if (whoColled.equals("encrypt")) {
            newNeme = "_encrypt";
        } else if (whoColled.equals("decrypt")){
            newNeme = "_decrypt";
        } else {
            newNeme = "_decryptbrutfors";
        }

        File outFile = Paths.get(getNewFileName(path.toFile().getAbsolutePath(), newNeme)).toFile();

        try (FileChannel srcFileChannel = new FileInputStream(inFile).getChannel();
             FileChannel dstFileChannel = new FileOutputStream(outFile).getChannel()) {

            long count = srcFileChannel.size();
            int sizeBuff = 2654;
            StringBuilder builder = new StringBuilder();

            long transferred = 0;
            while (count > 0) {
                ByteBuffer buffer = ByteBuffer.allocate(sizeBuff);
                srcFileChannel.read(buffer);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    // перекодируем в формат UTF_8
                    builder.append(StandardCharsets.UTF_8.decode(buffer));
                }
                buffer.clear();

                createEncryptAndDecryptTables(userKey, whoColled);

                for (int i = 0; i < builder.length(); i++) {
                    char temp = builder.charAt(i);
                    // игнорирование символов которых нет
                    if (encryptionTable.get(temp) == null) {
                        builder.setCharAt(i, temp);
                        continue;
                    }
                    builder.setCharAt(i, encryptionTable.get(temp));
                }
                String someText = builder.toString();
                buffer = ByteBuffer.wrap(someText.getBytes(StandardCharsets.UTF_8));
                dstFileChannel.write(buffer);


                long trans = buffer.position();
                transferred += buffer.position();
                srcFileChannel.position(transferred);
                count -= trans;

                buffer.clear();
                builder.delete(0, sizeBuff);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void createEncryptAndDecryptTables(int userKey, String whoColled) {

        while (userKey > 60){
            userKey -=60;
        }

        if (whoColled.equals("decrypt")) {
            for (int i = 0; i < arrayOfCharacters.length; i++) {
                if (userKey == arrayOfCharacters.length) {
                    userKey = 0;
                }
                encryptionTable.put(arrayOfCharacters[userKey], arrayOfCharacters[i]);
                userKey++;
            }
        } else if (whoColled.equals("encrypt")) {
            for (int i = 0; i < arrayOfCharacters.length; i++) {
                if (userKey == arrayOfCharacters.length) {
                    userKey = 0;
                }
                encryptionTable.put(arrayOfCharacters[i], arrayOfCharacters[userKey]);
                userKey++;
            }
        }
    }

    protected static void createAlphabet(String language) throws IOException {

        if (language.equals("ru")) {
            reader = new FileReader(ALPHADETRU.toFile(), StandardCharsets.UTF_8);
            // 78
            arrayOfCharacters = new char[77];
            while (reader.ready()) {
                reader.read(arrayOfCharacters);

            }

        } else if (language.equals("en")) {
            reader = new FileReader(ALPHADETEN.toFile(), StandardCharsets.UTF_8);
            // 64
            arrayOfCharacters = new char[63];
            while (reader.ready()) {
                reader.read(arrayOfCharacters);
            }

        }

    }

    protected static String getNewFileName(String oldFileName, String number) {
        int dotIndex = oldFileName.lastIndexOf(".");
        return oldFileName.substring(0, dotIndex) + number + oldFileName.substring(dotIndex);
    }

}
