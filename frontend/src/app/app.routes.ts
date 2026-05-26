import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./components/login/login').then(m => m.LoginComponent) },
  { path: 'registro', loadComponent: () => import('./components/registro/registro').then(m => m.RegistroComponent) },
  { path: 'dashboard', canActivate: [authGuard], loadComponent: () => import('./components/dashboard/dashboard').then(m => m.DashboardComponent) },

  // Categorías
  { path: 'categorias', canActivate: [authGuard], loadComponent: () => import('./components/categorias/list-categorias/list-categorias').then(m => m.ListCategoriasComponent) },
  { path: 'categorias/nueva', canActivate: [authGuard], loadComponent: () => import('./components/categorias/create-categoria/create-categoria').then(m => m.CreateCategoriaComponent) },
  { path: 'categorias/:id/editar', canActivate: [authGuard], loadComponent: () => import('./components/categorias/edit-categoria/edit-categoria').then(m => m.EditCategoriaComponent) },

  // Ingresos
  { path: 'movimientos/ingresos', canActivate: [authGuard], loadComponent: () => import('./components/movimientos/list-ingresos/list-ingresos').then(m => m.ListIngresosComponent) },
  { path: 'movimientos/nuevo-ingreso', canActivate: [authGuard], data: { tipo: 'ingreso' }, loadComponent: () => import('./components/movimientos/create-movimiento/create-movimiento').then(m => m.CreateMovimientoComponent) },
  { path: 'movimientos/ingresos/:id/editar', canActivate: [authGuard], data: { tipo: 'ingreso' }, loadComponent: () => import('./components/movimientos/edit-movimiento/edit-movimiento').then(m => m.EditMovimientoComponent) },

  // Gastos
  { path: 'movimientos/gastos', canActivate: [authGuard], loadComponent: () => import('./components/movimientos/list-gastos/list-gastos').then(m => m.ListGastosComponent) },
  { path: 'movimientos/nuevo-gasto', canActivate: [authGuard], data: { tipo: 'gasto' }, loadComponent: () => import('./components/movimientos/create-movimiento/create-movimiento').then(m => m.CreateMovimientoComponent) },
  { path: 'movimientos/gastos/:id/editar', canActivate: [authGuard], data: { tipo: 'gasto' }, loadComponent: () => import('./components/movimientos/edit-movimiento/edit-movimiento').then(m => m.EditMovimientoComponent) },

  // Deudas
  { path: 'deudas', canActivate: [authGuard], loadComponent: () => import('./components/deudas/list-deudas/list-deudas').then(m => m.ListDeudasComponent) },
  { path: 'deudas/nueva', canActivate: [authGuard], loadComponent: () => import('./components/deudas/create-deuda/create-deuda').then(m => m.CreateDeudaComponent) },
  { path: 'deudas/:id/editar', canActivate: [authGuard], loadComponent: () => import('./components/deudas/edit-deuda/edit-deuda').then(m => m.EditDeudaComponent) },

  // Metas
  { path: 'metas', canActivate: [authGuard], loadComponent: () => import('./components/metas/list-metas/list-metas').then(m => m.ListMetasComponent) },
  { path: 'metas/mes', canActivate: [authGuard], loadComponent: () => import('./components/metas/meta-mes/meta-mes').then(m => m.MetaMesComponent) },
  { path: 'metas/nueva', canActivate: [authGuard], loadComponent: () => import('./components/metas/create-meta/create-meta').then(m => m.CreateMetaComponent) },

  // Ahorros
  { path: 'ahorros', canActivate: [authGuard], loadComponent: () => import('./components/ahorros/list-ahorros/list-ahorros').then(m => m.ListAhorrosComponent) },
  { path: 'ahorros/nuevo', canActivate: [authGuard], loadComponent: () => import('./components/ahorros/create-ahorro/create-ahorro').then(m => m.CreateAhorroComponent) },

  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];
