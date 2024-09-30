import java.nio.ByteBuffer
import java.security.MessageDigest

const val BUFFER_SIZE = 213
const val DIGEST_SIZE = 64

// NoxusAI.kt representa um digest de mensagem para o Noxus T1
class NoxusAI {
    private val buffer = ByteArray(BUFFER_SIZE)
    private var bufferSize = 0

    // Digest retorna o digest da mensagem
    fun digest(): ByteArray {
        if (bufferSize != BUFFER_SIZE) {
            throw IllegalStateException("Buffer not filled with $BUFFER_SIZE bytes")
        }
        return MessageDigest.getInstance("SHA-256").digest(buffer)
    }

    // Update atualiza o digest com um novo valor
    fun update(value: Byte) {
        if (bufferSize >= BUFFER_SIZE) {
            throw IllegalStateException("Buffer overflow")
        }
        buffer[bufferSize] = value
        bufferSize++
    }

    // UpdateBytes atualiza o digest com um array de bytes
    fun updateBytes(bytes: ByteArray) {
        for (value in bytes) {
            update(value)
        }
    }

    // UpdateBigEndianInt atualiza o digest com um inteiro em big-endian
    fun updateBigEndianInt(value: Int) {
        val byteBuffer = ByteBuffer.allocate(4)
        byteBuffer.putInt(value)
        updateBytes(byteBuffer.array().reversedArray())
    }

    // Reset reseta o buffer do digest
    fun reset() {
        bufferSize = 0
    }
}

fun main() {
    // Testes
    val noxusDigest = NoxusAI()

    // Atualizar o digest com 213 bytes
    for (i in 0 until BUFFER_SIZE) {
        noxusDigest.update(0)
    }

    try {
        val digest = noxusDigest.digest()
        println(digest.joinToString("") { String.format("%02x", it) }) // Imprime o digest esperado
    } catch (e: IllegalStateException) {
        println("Erro ao calcular o digest: ${e.message}")
    }

    // Mais testes aqui...
}
