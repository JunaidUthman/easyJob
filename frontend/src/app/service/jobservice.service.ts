
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MessageResponse {
  msg: string;
}

@Injectable({
  providedIn: 'root'
})


export class JobserviceService {

  private apiUrl = `${environment.apiUrl}/api/jobs`;

  constructor(private http: HttpClient) {}

  testConnection(): Observable<MessageResponse> {
    return this.http.get<MessageResponse>(`${this.apiUrl}/test`);
  }
}
