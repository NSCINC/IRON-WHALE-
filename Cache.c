#include <stdio.h>
#include <stdlib.h>
#include <gmp.h> // Biblioteca GMP para inteiros grandes

// Definindo o módulo do campo
const char *fieldModulusStr = "21888242871839275222246405745257275088696311157297823662689037894645226208583";

// Estrutura FieldElement que representa um elemento de um campo finito
typedef struct {
    mpz_t value;
} FieldElement;

// Inicializa um elemento de campo
void FieldElement_init(FieldElement *element, const char *valueStr) {
    mpz_init_set_str(element->value, valueStr, 10);
    mpz_t modulus;
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_mod(element->value, element->value, modulus);
    mpz_clear(modulus);
}

// Libera a memória de um FieldElement
void FieldElement_clear(FieldElement *element) {
    mpz_clear(element->value);
}

// Soma dois elementos de campo
void FieldElement_add(FieldElement *result, FieldElement *a, FieldElement *b) {
    mpz_t modulus;
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_add(result->value, a->value, b->value);
    mpz_mod(result->value, result->value, modulus);
    mpz_clear(modulus);
}

// Subtrai dois elementos de campo
void FieldElement_sub(FieldElement *result, FieldElement *a, FieldElement *b) {
    mpz_t modulus;
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_sub(result->value, a->value, b->value);
    mpz_mod(result->value, result->value, modulus);
    mpz_clear(modulus);
}

// Multiplica dois elementos de campo
void FieldElement_mul(FieldElement *result, FieldElement *a, FieldElement *b) {
    mpz_t modulus;
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_mul(result->value, a->value, b->value);
    mpz_mod(result->value, result->value, modulus);
    mpz_clear(modulus);
}

// Divide dois elementos de campo
void FieldElement_div(FieldElement *result, FieldElement *a, FieldElement *b) {
    mpz_t modulus, inverse;
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_init(inverse);
    mpz_invert(inverse, b->value, modulus); // Calcula o inverso modular
    mpz_mul(result->value, a->value, inverse);
    mpz_mod(result->value, result->value, modulus);
    mpz_clear(modulus);
    mpz_clear(inverse);
}

// Eleva um elemento de campo a uma potência
void FieldElement_exp(FieldElement *result, FieldElement *a, const char *exponentStr) {
    mpz_t exponent, modulus;
    mpz_init_set_str(exponent, exponentStr, 10);
    mpz_init_set_str(modulus, fieldModulusStr, 10);
    mpz_powm(result->value, a->value, exponent, modulus);
    mpz_clear(exponent);
    mpz_clear(modulus);
}

// Imprime um FieldElement
void FieldElement_print(FieldElement *element) {
    gmp_printf("%Zd\n", element->value);
}

// Estrutura AbstractFieldPoint que representa um ponto no campo
typedef struct {
    FieldElement x;
    FieldElement y;
} AbstractFieldPoint;

// Inicializa um ponto
void AbstractFieldPoint_init(AbstractFieldPoint *point, FieldElement *x, FieldElement *y) {
    FieldElement_init(&point->x, "0");
    FieldElement_init(&point->y, "0");
    mpz_set(point->x.value, x->value);
    mpz_set(point->y.value, y->value);
}

// Limpa a memória de um ponto
void AbstractFieldPoint_clear(AbstractFieldPoint *point) {
    FieldElement_clear(&point->x);
    FieldElement_clear(&point->y);
}

// Exemplo de uso
int main() {
    // Teste da classe FieldElement
    FieldElement a, b, result;
    FieldElement_init(&a, "10");
    FieldElement_init(&b, "20");

    // Soma
    FieldElement_init(&result, "0");
    FieldElement_add(&result, &a, &b);
    printf("Soma: ");
    FieldElement_print(&result);

    // Subtração
    FieldElement_sub(&result, &a, &b);
    printf("Subtração: ");
    FieldElement_print(&result);

    // Multiplicação
    FieldElement_mul(&result, &a, &b);
    printf("Multiplicação: ");
    FieldElement_print(&result);

    // Divisão
    FieldElement_div(&result, &b, &a);
    printf("Divisão: ");
    FieldElement_print(&result);

    // Exemplo de uso da classe AbstractFieldPoint
    AbstractFieldPoint point;
    AbstractFieldPoint_init(&point, &a, &b);
    printf("Ponto: (");
    FieldElement_print(&point.x);
    printf(", ");
    FieldElement_print(&point.y);
    printf(")\n");

    // Limpeza
    FieldElement_clear(&a);
    FieldElement_clear(&b);
    FieldElement_clear(&result);
    AbstractFieldPoint_clear(&point);

    return 0;
}
