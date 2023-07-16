import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';
import routeConfig from './app/routes';
// import { XhrInterceptor } from './app/service/xhr.interceptor';


bootstrapApplication(AppComponent,
  {
    providers: [
      provideRouter(routeConfig),
      provideHttpClient(),
      // { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true }
    ]
  }
).catch(err => console.error(err));
