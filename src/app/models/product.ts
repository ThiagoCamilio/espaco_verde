import { PricingCategory } from "./pricing-category";
import { TiposProdutos } from "./tipos-produtos";

export interface Product {
      id?: string;
      nome: string;
      tipo: TiposProdutos | string;
      stockQuantity: number;
      dataDeEntrada: string;
      precoCusto: number;
      preco: number;
      suggestedPrice: number;
      useSuggestedPrice: boolean;
      pricingCategory?: PricingCategory
      imagem: string;
      descricao: string;
}
