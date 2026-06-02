import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, finalize, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { LoadingService } from '../services/loading.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth    = inject(AuthService);
  const loading = inject(LoadingService);
  const token   = auth.getToken();

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  // Muestra el overlay solo si la petición tarda más de 2.5s (cold start de Render)
  let timerActivo = true;
  const timer = setTimeout(() => {
    if (timerActivo) loading.show();
  }, 2500);

  return next(authReq).pipe(
    catchError(err => {
      if (err.status === 401 && !req.url.includes('/auth/')) {
        return auth.refresh().pipe(
          switchMap(res => {
            const retried = req.clone({
              setHeaders: { Authorization: `Bearer ${res.token}` }
            });
            return next(retried);
          }),
          catchError(() => {
            auth.logout();
            return throwError(() => err);
          })
        );
      }
      return throwError(() => err);
    }),
    finalize(() => {
      timerActivo = false;
      clearTimeout(timer);
      loading.hide();
    })
  );
};
