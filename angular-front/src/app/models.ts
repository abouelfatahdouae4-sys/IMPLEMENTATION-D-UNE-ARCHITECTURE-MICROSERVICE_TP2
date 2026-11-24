export interface Customer { id: number; name: string; email: string; }
export interface Product { id: string; name: string; price: number; quantity: number; }
export interface ProductItem {
  id: number; productId: string; quantity: number; unitPrice: number;
  amount: number; product?: Product;
}
export interface Bill {
  id: number; billingDate: string; customerId: number;
  customer?: Customer; productItems: ProductItem[];
}
