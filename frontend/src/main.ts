import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './app/interceptors/auth.interceptor';
import { appConfig } from './app/app.config';
import { importProvidersFrom } from '@angular/core';

bootstrapApplication(AppComponent, {
  providers: [
    // ✅ Register HttpClient + Interceptors
    provideHttpClient(withInterceptorsFromDi()),

    // ✅ Register your interceptor globally
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },

    // ✅ Keep your other providers
    ...(appConfig?.providers || [])
  ]
})
.catch(err => console.error(err));
