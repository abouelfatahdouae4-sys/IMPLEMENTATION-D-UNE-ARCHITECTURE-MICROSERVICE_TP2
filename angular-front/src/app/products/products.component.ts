import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { ApiService } from '../services/api.service';
import { Product } from '../models';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [DecimalPipe],
  template: `
    <div class="card">
      <div class="card-header fw-bold">Liste des produits</div>
      <div class="card-body">
        @if (error) { <div class="alert alert-danger">{{ error }}</div> }
        <table class="table table-hover">
          <thead><tr><th>ID</th><th>Nom</th><th>Prix</th><th>Quantite</th></tr></thead>
          <tbody>
            @for (p of products; track p.id) {
              <tr>
                <td class="small text-muted">{{ p.id }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.price | number:'1.2-2' }} DH</td>
                <td>{{ p.quantity }}</td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  error = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getProducts().subscribe({
      next: data => this.products = data,
      error: err => this.error = 'Erreur : ' + err.message
    });
  }
}
