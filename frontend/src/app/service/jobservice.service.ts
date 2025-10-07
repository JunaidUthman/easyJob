
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MessageResponse {
  msg: string;
}

@Injectable({
  providedIn: 'root'
})


export class JobserviceService {

  private apiUrl = 'http://localhost:8080/api/jobs';

  constructor(private http: HttpClient) {}

  testConnection(): Observable<MessageResponse> {
    return this.http.get<MessageResponse>(`${this.apiUrl}/test`);
  }
}
