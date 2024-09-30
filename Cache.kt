import java.math.BigInteger

// Definindo o módulo do campo
val fieldModulus = BigInteger("21888242871839275222246405745257275088696311157297823662689037894645226208583")

// FieldElement representa um elemento de um campo finito
class FieldElement(value: BigInteger) {
    var value: BigInteger = value.mod(fieldModulus)

    // Soma dois elementos de campo
    fun add(other: FieldElement): FieldElement {
        val result = this.value.add(other.value).mod(fieldModulus)
        return FieldElement(result)
    }

    // Subtrai dois elementos de campo
    fun sub(other: FieldElement): FieldElement {
        val result = this.value.subtract(other.value).mod(fieldModulus)
        return FieldElement(result)
    }

    // Multiplica dois elementos de campo
    fun mul(other: FieldElement): FieldElement {
        val result = this.value.multiply(other.value).mod(fieldModulus)
        return FieldElement(result)
    }

    // Divide dois elementos de campo
    fun div(other: FieldElement): FieldElement {
        val inverse = other.value.modInverse(fieldModulus)
        val result = this.value.multiply(inverse).mod(fieldModulus)
        return FieldElement(result)
    }

    // Eleva um elemento de campo a uma potência
    fun exp(exponent: BigInteger): FieldElement {
        val result = this.value.modPow(exponent, fieldModulus)
        return FieldElement(result)
    }

    override fun toString(): String {
        return value.toString()
    }
}

// AbstractFieldPoint representa um ponto em um campo abstrato
class AbstractFieldPoint(val x: FieldElement, val y: FieldElement)

fun main() {
    // Teste da classe FieldElement
    val a = FieldElement(BigInteger.valueOf(10))
    val b = FieldElement(BigInteger.valueOf(20))
    
    val c = a.add(b)
    println("Soma: $c") // Output: Soma: 30

    val d = a.sub(b)
    println("Subtração: $d") // Output: Subtração: (valor modificado)

    val e = a.mul(b)
    println("Multiplicação: $e") // Output: Multiplicação: (valor modificado)

    val f = b.div(a)
    println("Divisão: $f") // Output: Divisão: (valor modificado)

    // Exemplo de uso da classe AbstractFieldPoint
    val point = AbstractFieldPoint(a, b)
    println("Ponto: (${point.x}, ${point.y})") // Output: Ponto: (x, y)
}
