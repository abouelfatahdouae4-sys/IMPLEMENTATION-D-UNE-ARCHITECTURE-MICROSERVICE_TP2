import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { Bill } from '../models';

@Component({
  selector: 'app-bills',
  standalone: true,
  imports: [DatePipe],
  template: `
    <div class="card">
      <div class="card-header fw-bold">Factures du client #{{ customerId }}</div>
      <div class="card-body">
        @if (error) { <div class="alert alert-danger">{{ error }}</div> }
        <table class="table table-hover">
          <thead><tr><th>ID</th><th>Date</th><th>Client</th><th></th></tr></thead>
          <tbody>
            @for (b of bills; track b.id) {
              <tr>
                <td>{{ b.id }}</td>
                <td>{{ b.billingDate | date:'dd/MM/yyyy HH:mm' }}</td>
                <td>{{ b.customerId }}</td>
                <td>
                  <button class="btn btn-sm btn-primary" (click)="details(b)">
                    <i class="bi bi-eye"></i> Details
                  </button>
                </td>
              </tr>
            } @empty {
              <tr><td colspan="4" class="text-muted">Aucune facture</td></tr>
            }
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class BillsComponent implements OnInit {
  bills: Bill[] = [];
  customerId!: number;
  error = '';

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.customerId = Number(this.route.snapshot.params['customerId']);
    this.api.getBillsByCustomer(this.customerId).subscribe({
      next: data => this.bills = data,
      error: err => this.error = 'Erreur : ' + err.message
    });
  }

  details(b: Bill): void {
    this.router.navigateByUrl('/bill-details/' + b.id);
  }
}
