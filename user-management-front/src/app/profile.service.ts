import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Profile} from "./data/profile";
import {User} from "./data/user";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  API_URL = 'http://localhost:8080/profiles';

  constructor(private http: HttpClient) {
  }

  all(): Observable<Profile[]> {
    return this.http.get<Profile[]>(`${this.API_URL}`);
  }

  get(id: string): Observable<Profile> {
    return this.http.get<Profile>(`${this.API_URL}/${id}`);
  }

  addUser(profile: string, user: string): Observable<Profile> {
    return this.http.put<Profile>(`${this.API_URL}/${profile}/add-user/${user}`, {});
  }

  create(profile: { name: string, description: string}): Observable<Profile> {
    return this.http.post<Profile>(`${this.API_URL}`, profile);
  }

}
