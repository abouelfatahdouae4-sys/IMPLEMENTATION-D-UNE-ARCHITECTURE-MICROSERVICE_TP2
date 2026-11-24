import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Bill, Customer, Product } from '../models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private gw = environment.gatewayUrl;

  constructor(private http: HttpClient) {}

  // Spring Data REST renvoie du HAL : { _embedded: { customers: [...] }, page: {...} }
  getCustomers(): Observable<Customer[]> {
    return this.http.get<any>(`${this.gw}/customer-service/api/customers`)
      .pipe(map(r => r._embedded.customers));
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<any>(`${this.gw}/inventory-service/api/products`)
      .pipe(map(r => r._embedded.products));
  }

  getBillsByCustomer(customerId: number): Observable<Bill[]> {
    return this.http.get<any>(
      `${this.gw}/billing-service/api/bills/search/findByCustomerId?customerId=${customerId}`)
      .pipe(map(r => r._embedded?.bills ?? []));
  }

  getFullBill(billId: number): Observable<Bill> {
    return this.http.get<Bill>(`${this.gw}/billing-service/fullBill/${billId}`);
  }
}
