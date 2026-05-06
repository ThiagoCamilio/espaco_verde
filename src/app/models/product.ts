import { TiposProdutos } from "./tipos-produtos";

export interface Product {
      id?: string;
      nome: string;
      tipo: TiposProdutos | string;
      stockQuantity: number;
      dataDeEntrada: string;
      precoCusto: number;
      preco: number;
      imagem: string;
      descricao: string;
}
