import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from './services/auth.service';
import { MenuService } from './services/menu.service';
import { LoadingService } from './services/loading.service';
import { Menu } from './models/menu.model';

interface MenuDisplay extends Menu {
  expandido?: boolean;
  hijos: MenuDisplay[];
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit, OnDestroy {
  isAuthenticated = false;
  username = '';
  menuItems: MenuDisplay[] = [];
  sidebarAbierto = false;
  cargando = false;

  private sub!: Subscription;
  private loadingSub!: Subscription;

  constructor(
    private authService: AuthService,
    private menuService: MenuService,
    private loadingService: LoadingService
  ) {}

  ngOnInit() {
    this.sub = this.authService.authState$.subscribe(auth => {
      this.isAuthenticated = auth;
      this.username = this.authService.getUsername();
      if (auth) this.cargarMenus();
      else this.menuItems = [];
    });

    this.loadingSub = this.loadingService.loading$.subscribe(v => {
      this.cargando = v;
    });
  }

  cargarMenus() {
    this.menuService.listar().subscribe({
      next: menus => {
        this.menuItems = menus.map(m => ({
          ...m,
          expandido: true,
          hijos: m.hijos as MenuDisplay[]
        }));
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.loadingSub.unsubscribe();
  }

  toggle(item: MenuDisplay) { item.expandido = !item.expandido; }
  toggleSidebar() { this.sidebarAbierto = !this.sidebarAbierto; }
  cerrarSidebar() { this.sidebarAbierto = false; }
  logout() { this.authService.logout(); }
}
