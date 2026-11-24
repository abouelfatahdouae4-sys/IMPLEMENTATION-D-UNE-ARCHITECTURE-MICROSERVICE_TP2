import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { Customer } from '../models';

@Component({
  selector: 'app-customers',
  standalone: true,
  template: `
    <div class="card">
      <div class="card-header fw-bold">Liste des clients</div>
      <div class="card-body">
        @if (error) { <div class="alert alert-danger">{{ error }}</div> }
        <table class="table table-hover">
          <thead><tr><th>ID</th><th>Nom</th><th>Email</th><th></th></tr></thead>
          <tbody>
            @for (c of customers; track c.id) {
              <tr>
                <td>{{ c.id }}</td><td>{{ c.name }}</td><td>{{ c.email }}</td>
                <td>
                  <button class="btn btn-sm btn-success" (click)="showBills(c)">
                    <i class="bi bi-receipt"></i> Factures
                  </button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.api.getCustomers().subscribe({
      next: data => this.customers = data,
      error: err => this.error = 'Erreur : ' + err.message
    });
  }

  showBills(c: Customer): void {
    this.router.navigateByUrl('/bills/' + c.id);
  }
}
