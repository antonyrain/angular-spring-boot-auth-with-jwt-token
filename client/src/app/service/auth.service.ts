import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authenticated = false;

  api_url = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  submitLogin(data: any): Observable<any> {
    return this.http
    .post<any>(`${this.api_url}/login`, data)
  }

  submitSignup(data: any): Observable<any> {
    return this.http
    .post<any>(`${this.api_url}/signup`, data)
  }
}
