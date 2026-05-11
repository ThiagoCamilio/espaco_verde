import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import { LOCALE_ID } from '@angular/core';
import localePt from '@angular/common/locales/pt';
import { routes } from './app.routes';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';

registerLocaleData(localePt);

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes), 
    provideHttpClient(),
    provideHttpClient(withInterceptorsFromDi()),
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi:true},
    {provide: LOCALE_ID, useValue: 'pt-BR'},
    provideAnimations(),
    provideToastr({
      timeOut:10000,
      progressBar: true,
      preventDuplicates: true,
    })
  ]
};
