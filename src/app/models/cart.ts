import { CartItem } from "./cart-item";

export interface Cart{

    id: number;
    productCartDTOs : CartItem[];
    price : number

}