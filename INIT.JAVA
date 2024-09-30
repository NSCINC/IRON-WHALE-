/*
 * Copyright (c) NSC Inc.
 * All rights reserved.
 * 
 * This software may be used and distributed according to the terms
 * of the NSC Inc License Agreement.
 */

 package com.nscinc.project

 class NSC_ABC(private var modelName: String, private var version: String) {
 
     // Método de geração
     fun generate(input: String): String {
         // Lógica para geração com base no modelo NSC_ABC
         return "Generated output based on: $input"
     }
 
     // Exemplo de diálogo baseado no modelo
     fun dialog() {
         println("Dialog with NSC_ABC model $modelName version $version")
     }
 
     // Getters e Setters (não são necessários, pois estamos usando propriedades)
 
     companion object {
         @JvmStatic
         fun main(args: Array<String>) {
             val nscModel = NSC_ABC("NSC-ABC", "1.0")
             nscModel.dialog()
             val output = nscModel.generate("Teste de entrada")
             println(output)
         }
     }
 }
 