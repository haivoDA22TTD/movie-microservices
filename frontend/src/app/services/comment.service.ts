import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8083/comments';

  constructor(private http: HttpClient) {}

  getCommentsByMovie(movieId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/movie/${movieId}`);
  }

  createComment(comment: any): Observable<any> {
    return this.http.post(this.apiUrl, comment);
  }

  updateComment(id: number, comment: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, comment);
  }

  getRateLimitInfo(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/rate-limit/${email}`);
  }

  deleteComment(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
