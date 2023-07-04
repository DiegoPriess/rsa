package org.criptografia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class Criptografar {
    public static void main(String[] args) throws IOException {
        String chave = args[0];
        String arquivoDescriptografado = args[1];
        String arquivoCriptografado = args[2];

        BufferedReader keyReader = new BufferedReader(new FileReader(chave));
        BigInteger chave1 = new BigInteger(keyReader.readLine());
        BigInteger chave2 = new BigInteger(keyReader.readLine());
        keyReader.close();

        String plaintext = new String(Files.readAllBytes(Paths.get(arquivoDescriptografado)), StandardCharsets.UTF_8);

        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        String base64Text = Base64.getEncoder().encodeToString(plaintextBytes);

        List<String> encodedChunks = TextChunk.splitChunk(base64Text, TextChunk.blockSize(chave1));
        StringBuilder encodedChunksBuilder = new StringBuilder();

        encodedChunks.stream().forEach((chunk) -> {
            BigInteger encodedChunk = new TextChunk(chunk).bigIntValue().modPow(chave2, chave1);
            encodedChunksBuilder.append(encodedChunk).append("\n");
        });

        BufferedWriter retornoArquivoCriptografado = new BufferedWriter(new FileWriter(arquivoCriptografado));
        retornoArquivoCriptografado.write(encodedChunksBuilder.toString());
        retornoArquivoCriptografado.close();
    }
}
