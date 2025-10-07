import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    ...(appConfig?.providers || []) // merge your appConfig providers if any
  ]
})
.catch(err => console.error(err));
