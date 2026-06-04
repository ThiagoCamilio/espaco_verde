import { Product } from "./product";

export interface CartItem {

    id: string;
    productDTO: Product,
    quantity: number;
    sellPrice: number;

}
