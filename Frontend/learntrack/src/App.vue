<template>
  <!-- Contêiner principal da aplicação Vue -->
  <v-app>
    <!-- Barras de Ferramentas -->
    <v-toolbar color="#f27059" dark>
      <v-toolbar-title class="text-h5" style="font-family: 'Pacifico', cursive">LearnTrack</v-toolbar-title>
      <v-spacer></v-spacer>
      <!-- Botão para adicionar novo item -->
      <v-btn color="#ff8500" dark @click="dialog = true">Adicionar Item</v-btn>
    </v-toolbar>

    <!-- Conteúdo Principal  -->
    <v-container fluid>
      <v-col cols="12" md="4">
        <!-- Cartão com informações do Item -->
        <v-card style="padding: 15px" class="mb-4" v-for="(item, index) in items" :key="index">
          <div>{{ item.nivel }}</div>
          <div>{{ item.palavra }}</div>
          <div>{{ item.data }}</div>
          <div>
            <v-icon color="#ffB600" @click="editItem(item)">mdi-pencil</v-icon>
            <v-icon color="#ff4800" @click="deleteItem(item)">mdi-delete</v-icon>
          </div>
        </v-card>
      </v-col>
    </v-container>

    <!-- Modal -->
    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ formTitle }}</span>
        </v-card-title>
        <v-card-text>
          <!-- Formulário para manipulação dos dados do Item -->
          <!-- @ (v-on) = lida com eventos -->
          <v-form @submit.prevent="saveItem">
            <!-- Entrada de Texto  -->
            <v-text-field v-model="editedItem.nivel" label="Nível"></v-text-field>
            <v-text-field v-model="editedItem.palavra" label="Categoria"></v-text-field>
            <v-text-field v-model="editedItem.data" label="Data"></v-text-field>
            <!-- agrupamento de ações/botões -->
            <v-card-actions>
              <v-btn color="error" @click="dialog = false">Cancelar</v-btn>
              <v-btn type="submit" color="primary">Salvar</v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<!-- Para definir o comportamento e a lógica do componente Vue -->
<script>
// Comunicação com a API do Backend - para enviar requisições HTTP (como GET, POST, PUT, DELETE)
import axios from 'axios';
// para criar uma referência reativa a uma variável
import { ref } from 'vue';
const itemsInicial = ref([]);

// Objeto principal:
export default {
  // Dados: retorna os itens, seu nível e data de registro
  data: () => {
    return {
      items: itemsInicial,
      dialog: false,
      // para rastrear o índice do item editado e o item em si que está sendo editado
      editedIndex: -1,
      editedItem: {
        nivel: '',
        palavra: '',
        data: '',
      }
    }
  },

  computed: {
    // Propriedade Computada
    formTitle() {
      return this.editedIndex === -1 ? 'Novo Item' : 'Editar Item';
      // se editedIndex for igual a -1 (indicando que estamos criando um novo item) retornará 'Novo Item' ou caso contrário 'Editar Item'
    }
  },
  // Para buscar os itens da API (gancho de ciclo de vida do vue)
  mounted() {
    this.fetchItems();
  },

  // Ações do componente:
  methods: {
    // para buscar os itens da API
    async fetchItems() {
      try {
        const response = await axios.get('http://localhost:8080/learntrack');
        this.items = response.data;
      } catch (error) {
        console.error('Erro ao obter os itens:', error);
      }
    },
    // para salvar um novo item ou atualizar um existente
    async saveItem() {
      try {
        if (!this.editedItem.nivel || !this.editedItem.palavra || !this.editedItem.data) {
          alert('Por favor, preencha todos os campos.');
          return;
        }
        let response;
        if (this.editedIndex > -1) {
          response = await axios.put(`http://localhost:8080/learntrack/${this.editedItem.id}`, this.editedItem);
          this.fetchItems();
        } else {
          console.log(this.editedItem.data);
          response = await axios.post('http://localhost:8080/learntrack', this.editedItem);
          this.items.push(response.data);
        }
        this.dialog = false;
        this.editedItem = { nivel: '', palavra: '', data: '' };
        this.editedIndex = -1;
      } catch (error) {
        console.error('Erro ao salvar o item:', error);
      }
    },
    // para editar um item
    editItem(item) {
      this.editedIndex = this.items.findIndex(i => i.id === item.id);
      this.editedItem = { ...item };
      this.dialog = true;
    },
    // para excluir um item
    async deleteItem(item) {
      try {
        if (confirm('Tem certeza de que deseja excluir este item?')) {
          await axios.delete(`http://localhost:8080/learntrack/${item.id}`);
            const index = this.items.findIndex(i => i.id === item.id);
          if (index !== -1) {
            this.items.splice(index, 1);
          }
        }
      } catch (error) {
        console.error('Erro ao excluir o item:', error);
      }
    },
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Pacifico&display=swap');

.v-application {
  /* background: #f7b267; */
  font-family: sans-serif;
}
.v-btn {
  color: #ffffff !important; /* Cor do texto do botão */
}

.v-card, .v-card-title, .v-list-item-title, .v-list-item-subtitle {
  color: #000000 !important; /* Cor do texto das demais informações */
}

/* .v-container {
  background-color: #f7b267;
} */

/* Estilos para ajustar o tamanho da aplicação */
.v-application {
  min-width: 700px;
  max-width: 1400px; /* Largura máxima da aplicação */
  margin: 0 auto; /* Centraliza a aplicação na tela */
}

/* Ajusta o tamanho dos elementos dentro do v-container e v-col */
.v-card {
  min-width: 300px;
  max-width: 700px; /* Largura máxima dos cards */
}

.v-card-title {
  font-size: 18px; /* Tamanho da fonte do título do card */
}

.v-list-item {
  min-width: 200px; /* Largura mínima dos itens da lista */
}
</style>