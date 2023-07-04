package org.criptografia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Descriptografar {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Para executar o sistema, os seguintes arquivos são necessários: <key_file> <input_file> <output_file>");
            return;
        }

        String chave = args[0];
        String arquivoCriptografado = args[1];
        String arquivoDescriptografado = args[2];

        BufferedReader keyReader = new BufferedReader(new FileReader(chave));
        BigInteger chave1 = new BigInteger(keyReader.readLine());
        BigInteger chave2 = new BigInteger(keyReader.readLine());
        keyReader.close();

        BufferedReader inputReader = new BufferedReader(new FileReader(arquivoCriptografado));
        String line;
        StringBuilder encodedChunksBuilder = new StringBuilder();
        while ((line = inputReader.readLine()) != null) {
            encodedChunksBuilder.append(line).append("\n");
        }
        inputReader.close();

        String[] encodedChunks = encodedChunksBuilder.toString().split("\n");

        StringBuilder decryptedData = new StringBuilder();
        for (String encodedChunk : encodedChunks) {
            BigInteger chunk = new BigInteger(encodedChunk);
            BigInteger decoded = chunk.modPow(chave2, chave1);
            String b64Chunk = new TextChunk(decoded).toString();
            decryptedData.append(b64Chunk);
        }

        String base64String = decryptedData.toString();
        byte[] plaintextBytes = Base64.getDecoder().decode(base64String);
        String decodedText = new String(plaintextBytes, StandardCharsets.UTF_8);

        BufferedWriter retornoArquivoDescriptografado = new BufferedWriter(new FileWriter(arquivoDescriptografado));
        retornoArquivoDescriptografado.write(decodedText);
        retornoArquivoDescriptografado.close();
    }
}