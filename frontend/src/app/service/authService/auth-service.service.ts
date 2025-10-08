import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface SignupResponse {
  msg: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token?: string;
  msg?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private signupUrl = `${environment.apiUrl}/auth/signup`;
  private loginUrl = `${environment.apiUrl}/auth/login`;

  constructor(private http: HttpClient) { }

  signup(data: SignupRequest): Observable<SignupResponse> {
    return this.http.post<SignupResponse>(this.signupUrl, data);
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, data);
  }
}