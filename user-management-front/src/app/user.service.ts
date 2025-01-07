import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {User} from "./data/user";
import {Profile} from "./data/profile";
import { ApiResponse } from './data/apiResponse';
import { Page } from './data/page';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  API_URL = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) {
  }

  login(credentials: { username: string, secret: string }): Observable<ApiResponse<User>> {
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${credentials.username}:${credentials.secret}`),
    });
    return this.http.get<ApiResponse<User>>(`${this.API_URL}/find/username/${credentials.username}`, {headers});
  }

  get(userId: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.API_URL}/${userId}`);
  }

  create(user: { username: string, secret: string, name: string, birthDate: Date }): Observable<ApiResponse<String>> {
    return this.http.post<ApiResponse<String>>(`${this.API_URL}`, user);
  }

  update(userId: string, user: User): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.API_URL}/${userId}`, user);
  }

  delete(userId: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${userId}`).pipe(
      catchError((error) => {
        console.error('Error deleting user in service:', error);
        return throwError(() => error);
      })
    );
  }

  all(page: number, size: number): Observable<ApiResponse<Page<User>>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<ApiResponse<Page<User>>>(`${this.API_URL}`, {params});
  }
}
