import org.bouncycastle.crypto.digests.SHA256Digest;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Blake2bfMessageDigest {
    private static final int BUFFER_SIZE = 213;
    private static final int DIGEST_SIZE = 32;  // SHA-256 tem um digest de 32 bytes

    private byte[] buffer;
    private int bufferSize;
    private byte[] digest;

    // Construtor para criar uma nova instância de Blake2bfMessageDigest
    public Blake2bfMessageDigest() {
        this.buffer = new byte[BUFFER_SIZE];
        this.bufferSize = 0;
        this.digest = new byte[DIGEST_SIZE];
    }

    // Método para retornar o digest da mensagem
    public byte[] digest() {
        if (this.bufferSize != BUFFER_SIZE) {
            throw new IllegalStateException(String.format("Buffer not filled with %d bytes", BUFFER_SIZE));
        }

        SHA256Digest sha256 = new SHA256Digest();
        sha256.update(this.buffer, 0, this.bufferSize);
        sha256.doFinal(this.digest, 0);
        return this.digest;
    }

    // Método para atualizar o digest com um novo valor
    public void update(byte value) {
        if (this.bufferSize >= BUFFER_SIZE) {
            throw new IllegalStateException("Buffer overflow");
        }
        this.buffer[this.bufferSize] = value;
        this.bufferSize++;
    }

    // Método para atualizar o digest com um array de bytes
    public void updateBytes(byte[] bytes) {
        for (byte b : bytes) {
            this.update(b);
        }
    }

    // Método para atualizar o digest com um inteiro em big-endian
    public void updateBigEndianInt(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        byte[] array = buffer.array();
        byte[] bigEndianArray = {array[3], array[2], array[1], array[0]};  // Conversão para big-endian
        this.updateBytes(bigEndianArray);
    }

    // Método para resetar o buffer do digest
    public void reset() {
        this.bufferSize = 0;
        Arrays.fill(this.buffer, (byte) 0);
    }

    // Testes
    public static void main(String[] args) {
        Blake2bfMessageDigest messageDigest = new Blake2bfMessageDigest();

        // Atualizar o digest com 213 bytes
        for (int i = 0; i < BUFFER_SIZE; i++) {
            messageDigest.update((byte) 0);
        }

        byte[] digest = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        System.out.println(hexString.toString());

        // Mais testes aqui...
    }
}
