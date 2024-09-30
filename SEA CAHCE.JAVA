import threading
import time
import hashlib
from datetime import datetime, timedelta

# Classe para Tokens NSC IA
class NSC_IAToken:
    def __init__(self, token_id, expiration_minutes=5):
        self.token_id = token_id
        self.expiration_time = datetime.now() + timedelta(minutes=expiration_minutes)

    def is_valid(self):
        return datetime.now() < self.expiration_time

# Geração de Token (NSC IA Token)
def generate_nsc_ia_token():
    token_id = hashlib.sha256(str(time.time()).encode()).hexdigest()
    return NSC_IAToken(token_id)

# Classe SEA (Self-Explaining Assets)
class SEAAsset:
    def __init__(self, value, origin, created_at=None):
        self.value = value
        self.origin = origin
        self.created_at = created_at if created_at else datetime.now()
        self.metadata = {
            'origin': origin,
            'created_at': str(self.created_at),
            'validity': 'indefinite'  # Placeholder, can be extended
        }

    def get_metadata(self):
        return self.metadata

# Classe Cache com suporte a NSC ABC Net e NEESE
class Cache:
    def __init__(self, max_size):
        self.keys = []      # Lista de chaves
        self.values = []    # Lista de valores
        self.max_size = max_size  # Tamanho máximo do cache
        self.current_size = 0     # Tamanho atual do cache
        self.lock = threading.Lock()  # Mutex para controle de concorrência
        self.cache_network = {}  # Simulação de NSC ABC Net (Caches distribuídos)
        self.history = []  # Armazena histórico para NEESE

    # Função para adicionar dados ao cache com SEA (Thread-safe)
    def add_to_cache(self, token, key, value, origin="local"):
        if not token.is_valid():
            print("Token inválido. Operação negada.")
            return
        
        with self.lock:
            if self.current_size >= self.max_size:
                # Usando NEESE para melhorar a decisão de remoção (Remover o menos usado)
                least_used = self.history[0] if self.history else self.keys[0]
                index = self.keys.index(least_used)
                print(f"Removido do cache (LRU): {self.keys[index]}")
                self.keys.pop(index)
                self.values.pop(index)
                self.current_size -= 1
            
            # Adiciona a nova chave e o valor com metadados SEA
            sea_asset = SEAAsset(value, origin)
            self.keys.append(key)
            self.values.append(sea_asset)
            self.history.append(key)  # Registro de uso para NEESE
            self.current_size += 1
            print(f"Novo item adicionado ao cache: {key}")

    # Função para buscar um item no cache (Thread-safe) ou em NSC ABC Net
    def get_from_cache(self, token, key):
        if not token.is_valid():
            print("Token inválido. Operação negada.")
            return None

        with self.lock:
            if key in self.keys:
                index = self.keys.index(key)
                sea_asset = self.values[index]
                self.history.append(key)  # Atualiza o uso para NEESE
                print(f"Item encontrado no cache: {key}")
                print(f"Metadados do SEA: {sea_asset.get_metadata()}")
                return sea_asset.value
            else:
                print(f"Item não encontrado no cache: {key}")
                # Tenta buscar no NSC ABC Net (Caches distribuídos)
                if key in self.cache_network:
                    print(f"Item encontrado em outro cache via NSC ABC Net: {key}")
                    return self.cache_network[key]
        return None

    # Função para remover um item do cache (Thread-safe)
    def remove_from_cache(self, token, key):
        if not token.is_valid():
            print("Token inválido. Operação negada.")
            return
        
        with self.lock:
            if key in self.keys:
                index = self.keys.index(key)
                print(f"Item removido do cache: {key}")
                self.keys.pop(index)
                self.values.pop(index)
                self.current_size -= 1
                if key in self.history:
                    self.history.remove(key)

    # Função para listar todos os itens do cache (Thread-safe)
    def list_cache(self, token):
        if not token.is_valid():
            print("Token inválido. Operação negada.")
            return

        with self.lock:
            print("Listando todos os itens no cache:")
            for i in range(self.current_size):
                sea_asset = self.values[i]
                print(f"{self.keys[i]}: {sea_asset.value} (Metadados: {sea_asset.get_metadata()})")

    # Simula a integração com NSC ABC Net (outros caches)
    def add_to_nsc_abc_net(self, key, value):
        self.cache_network[key] = value

# Função de operações de cache paralelas
def cache_operations(cache, token):
    cache.add_to_cache(token, "Chave1", "Valor1", origin="Sistema A")
    cache.add_to_cache(token, "Chave2", "Valor2", origin="Sistema B")
    value = cache.get_from_cache(token, "Chave1")
    if value:
        print(f"Valor encontrado: {value}")
    cache.remove_from_cache(token, "Chave1")

# Função principal
if __name__ == "__main__":
    # Gera um token NSC IA válido
    token = generate_nsc_ia_token()

    # Cria o cache com tamanho máximo de 3
    my_cache = Cache(3)

    # Cria caches distribuídos via NSC ABC Net
    my_cache.add_to_nsc_abc_net("ChaveExterna1", "ValorExterno1")

    # Cria duas threads para processar operações no cache simultaneamente
    thread1 = threading.Thread(target=cache_operations, args=(my_cache, token))
    thread2 = threading.Thread(target=cache_operations, args=(my_cache, token))

    # Inicia as threads
    thread1.start()
    thread2.start()

    # Aguarda a finalização das threads
    thread1.join()
    thread2.join()

    # Lista os itens restantes no cache
    my_cache.list_cache(token)
