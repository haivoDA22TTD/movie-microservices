import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private tmdbApiUrl = 'https://api.themoviedb.org/3';
  private tmdbApiKey = 'eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMzQ4YzVjZDkxYjY1YmQ1Yzc2YThkNzIxYmU2YWU5MSIsIm5iZiI6MTc2NDkyOTc2My4yNTUsInN1YiI6IjY5MzJiMGUzNDZlMjA2NzYzMTAyZDkwNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1oRFy0ZJ4jZ9rROFtlwdIAz257vtoH2mUzfRL2awoCU';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.tmdbApiKey}`,
        'Content-Type': 'application/json'
      })
    };
  }

  getPopularMovies(page: number = 1): Observable<any> {
    return this.http.get(`${this.tmdbApiUrl}/movie/popular?language=vi-VN&page=${page}`, this.getHeaders());
  }

  getTrendingMovies(): Observable<any> {
    return this.http.get(`${this.tmdbApiUrl}/trending/movie/week?language=vi-VN`, this.getHeaders());
  }

  getMovieDetails(id: number): Observable<any> {
    return this.http.get(`${this.tmdbApiUrl}/movie/${id}?language=vi-VN`, this.getHeaders());
  }

  getMovieVideos(id: number): Observable<any> {
    return this.http.get(`${this.tmdbApiUrl}/movie/${id}/videos?language=vi-VN`, this.getHeaders());
  }

  searchMovies(query: string): Observable<any> {
    return this.http.get(`${this.tmdbApiUrl}/search/movie?language=vi-VN&query=${query}`, this.getHeaders());
  }
}
