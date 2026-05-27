import { OrderItem } from "./order-items";
import { Payment } from "./payment";

export interface Order {
  id: number;
  createdAt: string;
  orderStatus: string;
  totalPrice: number;
  items: OrderItem[];
  deliveryMethod: String;
  deliveryAdress: String;
  payment : Payment;
}