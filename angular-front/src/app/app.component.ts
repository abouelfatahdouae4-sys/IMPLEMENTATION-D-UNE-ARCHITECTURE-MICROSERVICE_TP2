import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand navbar-dark bg-primary mb-4">
      <div class="container">
        <span class="navbar-brand"><i class="bi bi-receipt"></i> Billing App</span>
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" routerLink="/customers" routerLinkActive="active">Clients</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" routerLink="/products" routerLinkActive="active">Produits</a>
          </li>
        </ul>
      </div>
    </nav>
    <div class="container">
      <router-outlet />
    </div>
  `
})
export class AppComponent {}
