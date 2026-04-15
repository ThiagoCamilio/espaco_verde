import { TiposProdutos } from "./tipos-produtos";

export interface Product {
      id?: number;
      nome: string;
      tipo: TiposProdutos | string;
      quantidade: number;
      dataDeEntrada: string;
      precoCusto: number;
      preco: number;
      imagem: string;
}
