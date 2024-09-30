import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.sentencepiece.*;

public class Tokenizer {
    private SentencePieceProcessor spModel;
    private int nWords;
    private int bosId;
    private int eosId;
    private int padId;

    public Tokenizer(String modelPath) throws IOException {
        // Load the SentencePiece model
        this.spModel = new SentencePieceProcessor();
        if (!new File(modelPath).exists()) {
            throw new IOException("Model file not found: " + modelPath);
        }
        this.spModel.load(modelPath);
        this.nWords = spModel.getPieceSize();
        this.bosId = spModel.bosId();
        this.eosId = spModel.eosId();
        this.padId = spModel.padId();
        System.out.println("#words: " + nWords + " - BOS ID: " + bosId + " - EOS ID: " + eosId);
    }

    public List<Integer> encode(String s, boolean bos, boolean eos) {
        // Encode a string into a list of token IDs
        List<Integer> tokenIds = new ArrayList<>();
        int[] ids = spModel.encode(s);
        for (int id : ids) {
            tokenIds.add(id);
        }
        if (bos) {
            tokenIds.add(0, bosId); // Prepend BOS ID
        }
        if (eos) {
            tokenIds.add(eosId); // Append EOS ID
        }
        return tokenIds;
    }

    public String decode(List<Integer> tokenIds) {
        // Decode a list of token IDs into a string
        int[] ids = tokenIds.stream().mapToInt(i -> i).toArray();
        return spModel.decode(ids);
    }

    public static void main(String[] args) {
        try {
            String modelPath = "path/to/your/sentencepiece/model.model";
            Tokenizer tokenizer = new Tokenizer(modelPath);

            // Example usage
            String text = "Hello, world!";
            List<Integer> encoded = tokenizer.encode(text, true, true);
            System.out.println("Encoded: " + encoded);
            String decoded = tokenizer.decode(encoded);
            System.out.println("Decoded: " + decoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
