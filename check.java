import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SPDXHeaderChecker {

    private static final String SPDX_HEADER = "SPDX-License-Identifier: Apache-2.0";

    // Função para verificar se os arquivos possuem cabeçalho SPDX
    public static void checkHeaders(String rootPath, String spdxHeader, String filesRegex, String excludeRegex) throws IOException {
        Path root = Paths.get(rootPath);
        List<String> filesWithoutHeader = new ArrayList<>();

        // Expressões regulares para seleção e exclusão de arquivos
        Pattern includePattern = Pattern.compile(filesRegex);
        Pattern excludePattern = Pattern.compile(excludeRegex);

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String filePath = file.toString();
                if (includePattern.matcher(filePath).matches() && !excludePattern.matcher(filePath).matches()) {
                    String content = new String(Files.readAllBytes(file));
                    if (!content.contains(spdxHeader)) {
                        filesWithoutHeader.add(filePath);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        if (!filesWithoutHeader.isEmpty()) {
            System.out.println("Files without headers:");
            for (String file : filesWithoutHeader) {
                System.out.println(file);
            }
            throw new RuntimeException("Files without SPDX header found.");
        }
    }

    public static void main(String[] args) {
        try {
            // Exemplo de chamada da função de verificação
            checkHeaders("caminho/para/o/diretorio", SPDX_HEADER, ".*", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
