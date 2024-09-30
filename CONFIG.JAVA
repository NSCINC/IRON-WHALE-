public class ModuleParams {
    // Declaração de variáveis privadas para armazenar configurações de parâmetros de módulo
    private boolean overwriteModuleParamsOnConversion = false;
    private boolean swapModuleParamsOnConversion = false;

    // Função para definir se deve sobrescrever parâmetros do módulo ao converter
    public void setOverwriteModuleParamsOnConversion(boolean value) {
        overwriteModuleParamsOnConversion = value;
    }

    // Função para obter o estado atual de sobrescrever parâmetros do módulo ao converter
    public boolean getOverwriteModuleParamsOnConversion() {
        return overwriteModuleParamsOnConversion;
    }

    // Função para definir se deve trocar parâmetros do módulo ao converter
    public void setSwapModuleParamsOnConversion(boolean value) {
        swapModuleParamsOnConversion = value;
    }

    // Função para obter o estado atual de trocar parâmetros do módulo ao converter
    public boolean getSwapModuleParamsOnConversion() {
        return swapModuleParamsOnConversion;
    }

    public static void main(String[] args) {
        // Exemplo de uso das funções
        ModuleParams moduleParams = new ModuleParams();
        
        moduleParams.setOverwriteModuleParamsOnConversion(true);
        System.out.println("overwriteModuleParamsOnConversion: " + moduleParams.getOverwriteModuleParamsOnConversion());
        
        moduleParams.setSwapModuleParamsOnConversion(true);
        System.out.println("swapModuleParamsOnConversion: " + moduleParams.getSwapModuleParamsOnConversion());
    }
}
