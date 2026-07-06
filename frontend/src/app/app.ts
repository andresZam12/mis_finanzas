import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { Subscription } from 'rxjs';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { AuthService } from './services/auth.service';
import { MenuService } from './services/menu.service';
import { LoadingService } from './services/loading.service';
import { Menu } from './models/menu.model';

interface MenuDisplay extends Menu {
  expandido?: boolean;
  hijos: MenuDisplay[];
}

const S = `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">`;
const E = `</svg>`;

const ICONOS: Record<string, string> = {
  'INICIO':        S+`<rect width="7" height="7" x="3" y="3" rx="1"/><rect width="7" height="7" x="14" y="3" rx="1"/><rect width="7" height="7" x="14" y="14" rx="1"/><rect width="7" height="7" x="3" y="14" rx="1"/>`+E,
  'Movimientos':      S+`<path d="m8 3-5 5 5 5"/><path d="M3 8h18"/><path d="m16 21 5-5-5-5"/><path d="M21 16H3"/>`+E,
  'Ingresos':         S+`<polyline points="22 7 13.5 15.5 8.5 10.5 2 17"/><polyline points="16 7 22 7 22 13"/>`+E,
  'Gastos':           S+`<polyline points="22 17 13.5 8.5 8.5 13.5 2 7"/><polyline points="16 17 22 17 22 11"/>`+E,
  'Nuevo ingreso':    S+`<circle cx="12" cy="12" r="10"/><path d="M8 12h8"/><path d="M12 8v8"/>`+E,
  'Nuevo gasto':      S+`<circle cx="12" cy="12" r="10"/><path d="M8 12h8"/>`+E,
  'Categorías':       S+`<path d="M12 2H2v10l9.29 9.29c.94.94 2.48.94 3.42 0l6.58-6.58c.94-.94.94-2.48 0-3.42L12 2Z"/><path d="M7 7h.01"/>`+E,
  'Ver categorías':   S+`<line x1="8" x2="21" y1="6" y2="6"/><line x1="8" x2="21" y1="12" y2="12"/><line x1="8" x2="21" y1="18" y2="18"/><line x1="3" x2="3.01" y1="6" y2="6"/><line x1="3" x2="3.01" y1="12" y2="12"/><line x1="3" x2="3.01" y1="18" y2="18"/>`+E,
  'Nueva categoría':  S+`<path d="M12 2H2v10l9.29 9.29c.94.94 2.48.94 3.42 0l6.58-6.58c.94-.94.94-2.48 0-3.42L12 2Z"/><path d="M7 7h.01"/>`+E,
  'Deudas':           S+`<rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/>`+E,
  'Mis deudas':       S+`<rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/>`+E,
  'Nueva deuda':      S+`<rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/>`+E,
  'Ahorros':          S+`<path d="M21 12V7H5a2 2 0 0 1 0-4h14v4"/><path d="M3 5v14a2 2 0 0 0 2 2h16v-5"/><path d="M18 12a2 2 0 0 0 0 4h4v-4Z"/>`+E,
  'Mis ahorros':      S+`<path d="M21 12V7H5a2 2 0 0 1 0-4h14v4"/><path d="M3 5v14a2 2 0 0 0 2 2h16v-5"/><path d="M18 12a2 2 0 0 0 0 4h4v-4Z"/>`+E,
  'Registrar ahorro': S+`<path d="M21 12V7H5a2 2 0 0 1 0-4h14v4"/><path d="M3 5v14a2 2 0 0 0 2 2h16v-5"/><path d="M18 12a2 2 0 0 0 0 4h4v-4Z"/>`+E,
  'Calendario':       S+`<rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/>`+E,
};

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
    private loadingService: LoadingService,
    private sanitizer: DomSanitizer
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
        this.menuItems = menus
          .filter(m => m.nombre !== 'Metas')
          .map(m => ({
            ...m,
            nombre: m.nombre === 'Dashboard' ? 'INICIO' : m.nombre,
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

  icono(nombre: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(ICONOS[nombre] ?? '');
  }

  get iniciales(): string {
    return this.username ? this.username.substring(0, 2).toUpperCase() : 'Z';
  }
}
