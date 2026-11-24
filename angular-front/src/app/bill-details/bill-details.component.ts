import { Component, OnInit } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/api.service';
import { Bill } from '../models';

@Component({
  selector: 'app-bill-details',
  standalone: true,
  imports: [DatePipe, DecimalPipe],
  template: `
    @if (error) { <div class="alert alert-danger">{{ error }}</div> }
    @if (bill) {
      <div class="card">
        <div class="card-header fw-bold">Facture #{{ bill.id }}</div>
        <div class="card-body">
          <p><b>Date :</b> {{ bill.billingDate | date:'dd/MM/yyyy HH:mm' }}</p>
          <p><b>Client :</b> {{ bill.customer?.name }} ({{ bill.customer?.email }})</p>
          <table class="table">
            <thead><tr><th>Produit</th><th>Prix unitaire</th><th>Quantite</th><th>Montant</th></tr></thead>
            <tbody>
              @for (pi of bill.productItems; track pi.id) {
                <tr>
                  <td>{{ pi.product?.name }}</td>
                  <td>{{ pi.unitPrice | number:'1.2-2' }}</td>
                  <td>{{ pi.quantity }}</td>
                  <td>{{ pi.amount | number:'1.2-2' }}</td>
                </tr>
              }
            </tbody>
            <tfoot>
              <tr class="fw-bold"><td colspan="3">Total</td><td>{{ total | number:'1.2-2' }} DH</td></tr>
            </tfoot>
          </table>
        </div>
      </div>
    }
  `
})
export class BillDetailsComponent implements OnInit {
  bill?: Bill;
  error = '';

  constructor(private api: ApiService, private route: ActivatedRoute) {}

  get total(): number {
    return this.bill?.productItems.reduce((s, pi) => s + pi.amount, 0) ?? 0;
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['billId']);
    this.api.getFullBill(id).subscribe({
      next: data => this.bill = data,
      error: err => this.error = 'Erreur : ' + err.message
    });
  }
}
