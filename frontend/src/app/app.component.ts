import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MovieService } from './services/movie.service';
import { AuthService } from './services/auth.service';
import { CommentService } from './services/comment.service';
import { SafePipe } from './pipes/safe.pipe';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SafePipe, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  movies: any[] = [];
  trendingMovies: any[] = [];
  favoriteMovies: any[] = [];
  user: any = null;
  selectedMovie: any = null;
  searchQuery: string = '';
  isDarkMode: boolean = true;
  showUserMenu: boolean = false;
  currentView: 'all' | 'favorites' = 'all';
  currentPage: number = 1;
  isLoading: boolean = false;
  hasMorePages: boolean = true;
  
  // Comments
  comments: any[] = [];
  newComment: string = '';
  editingCommentId: number | null = null;
  editingCommentText: string = '';
  
  // Notification modal
  showNotification: boolean = false;
  notificationTitle: string = '';
  notificationMessage: string = '';
  notificationIcon: string = '';

  constructor(
    private movieService: MovieService,
    private authService: AuthService,
    private commentService: CommentService,
    private router: Router
  ) {
    // Listen to route changes
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.checkAuth();
    });
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-dropdown')) {
      this.showUserMenu = false;
    }
  }

  @HostListener('window:storage', ['$event'])
  onStorageChange(event: StorageEvent) {
    if (event.key === 'user' || event.key === 'token') {
      this.checkAuth();
    }
  }

  ngOnInit() {
    // Xử lý callback từ Google OAuth
    this.handleOAuthCallback();
    
    this.checkAuth();
    this.loadTrendingMovies();
    this.loadMovies();
    this.loadTheme();
    this.loadFavorites();
  }

  handleOAuthCallback() {
    // Kiểm tra xem có phải callback từ OAuth không
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const email = urlParams.get('email');
    const name = urlParams.get('name');
    const picture = urlParams.get('picture');
    
    if (token && email && name) {
      console.log('OAuth callback detected, saving user info...');
      
      // Lưu vào localStorage
      localStorage.setItem('token', token);
      const userInfo = {
        email: email,
        name: name,
        picture: picture
      };
      localStorage.setItem('user', JSON.stringify(userInfo));
      
      // Set user ngay lập tức
      this.user = userInfo;
      
      console.log('User logged in:', this.user);
      
      // Xóa params khỏi URL và reload
      window.history.replaceState({}, document.title, '/');
      
      // Show notification
      this.showNotificationModal('✅', 'Đăng nhập thành công', 'Chào mừng ' + name + '!');
    }
  }

  checkAuth() {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    
    if (token && userStr) {
      try {
        this.user = JSON.parse(userStr);
        console.log('User already logged in:', this.user);
      } catch (e) {
        console.error('Error parsing user:', e);
        this.user = null;
      }
    } else {
      this.user = null;
    }
  }

  loadTheme() {
    const theme = localStorage.getItem('theme');
    this.isDarkMode = theme !== 'light';
  }

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
  }

  loginWithGoogle() {
    // Đăng nhập trực tiếp qua auth-service (port 8081)
    window.location.href = 'http://localhost:8081/oauth2/authorization/google';
  }

  logout() {
    const token = localStorage.getItem('token');
    
    if (token) {
      // Gọi API logout để blacklist token
      this.authService.logout(token).subscribe({
        next: () => {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          this.user = null;
          this.showUserMenu = false;
          this.showNotificationModal('👋', 'Đăng xuất thành công', 'Hẹn gặp lại bạn!');
        },
        error: (err) => {
          console.error('Logout error:', err);
          // Vẫn xóa local storage dù API lỗi
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          this.user = null;
          this.showUserMenu = false;
          this.showNotificationModal('👋', 'Đăng xuất thành công', 'Hẹn gặp lại bạn!');
        }
      });
    } else {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      this.user = null;
      this.showUserMenu = false;
      this.showNotificationModal('👋', 'Đăng xuất thành công', 'Hẹn gặp lại bạn!');
    }
  }

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  loadTrendingMovies() {
    this.movieService.getTrendingMovies().subscribe({
      next: (data) => {
        this.trendingMovies = data.results.slice(0, 5); // Top 5 phim hot
      },
      error: (err) => {
        console.error('Lỗi khi tải phim trending:', err);
      }
    });
  }

  loadMovies(page: number = 1) {
    if (this.isLoading) return;
    
    this.isLoading = true;
    this.movieService.getPopularMovies(page).subscribe({
      next: (data) => {
        if (page === 1) {
          this.movies = data.results;
        } else {
          this.movies = [...this.movies, ...data.results];
        }
        this.currentPage = page;
        this.hasMorePages = page < data.total_pages;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Lỗi khi tải phim:', err);
        this.showNotificationModal('❌', 'Lỗi', 'Không thể tải danh sách phim. Vui lòng thử lại!');
        this.isLoading = false;
      }
    });
  }

  loadMoreMovies() {
    if (this.hasMorePages && !this.isLoading) {
      this.loadMovies(this.currentPage + 1);
    }
  }

  searchMovies() {
    if (this.searchQuery.trim()) {
      this.movieService.searchMovies(this.searchQuery).subscribe({
        next: (data) => {
          this.movies = data.results;
          this.currentView = 'all';
          if (data.results.length === 0) {
            this.showNotificationModal('🔍', 'Không tìm thấy', 'Không có kết quả cho từ khóa "' + this.searchQuery + '"');
          }
        },
        error: (err) => {
          console.error('Lỗi khi tìm kiếm:', err);
          this.showNotificationModal('❌', 'Lỗi', 'Không thể tìm kiếm phim. Vui lòng thử lại!');
        }
      });
    } else {
      this.loadMovies();
    }
  }

  selectMovie(movie: any) {
    // Hiển thị thông tin phim ngay lập tức
    this.selectedMovie = {
      ...movie,
      videoKey: null,
      embedUrl: null
    };
    
    // Tạo URL xem phim từ vidsrc.xyz (nguồn phim miễn phí)
    const tmdbId = movie.id;
    const mediaType = movie.title ? 'movie' : 'tv';
    this.selectedMovie.embedUrl = `https://vidsrc.xyz/embed/${mediaType}/${tmdbId}`;
    
    // Load comments for this movie
    this.loadComments(movie.id);
    
    // Load trailer từ TMDB (backup)
    this.movieService.getMovieVideos(movie.id).subscribe({
      next: (data) => {
        const trailer = data.results.find((v: any) => 
          v.type === 'Trailer' && v.site === 'YouTube'
        );
        if (this.selectedMovie && this.selectedMovie.id === movie.id) {
          this.selectedMovie.videoKey = trailer?.key;
        }
      },
      error: (err) => {
        console.error('Lỗi khi tải video:', err);
      }
    });
  }

  closePlayer() {
    this.selectedMovie = null;
  }

  toggleFavorite(movie: any, event: Event) {
    event.stopPropagation();
    
    if (!this.user) {
      this.showNotificationModal('🔒', 'Cần đăng nhập', 'Vui lòng đăng nhập để thêm phim yêu thích!');
      return;
    }

    const favorites = this.getFavorites();
    const index = favorites.findIndex((m: any) => m.id === movie.id);
    
    if (index > -1) {
      favorites.splice(index, 1);
      this.showNotificationModal('💔', 'Đã xóa', 'Đã xóa "' + movie.title + '" khỏi danh sách yêu thích');
    } else {
      favorites.push(movie);
      this.showNotificationModal('❤️', 'Đã thêm', 'Đã thêm "' + movie.title + '" vào danh sách yêu thích');
    }
    
    localStorage.setItem('favorites', JSON.stringify(favorites));
    this.loadFavorites();
  }

  isFavorite(movieId: number): boolean {
    return this.favoriteMovies.some(m => m.id === movieId);
  }

  getFavorites(): any[] {
    const favorites = localStorage.getItem('favorites');
    return favorites ? JSON.parse(favorites) : [];
  }

  loadFavorites() {
    this.favoriteMovies = this.getFavorites();
  }

  showAllMovies() {
    this.currentView = 'all';
    if (this.searchQuery.trim()) {
      this.searchMovies();
    } else {
      this.loadMovies();
    }
  }

  showFavorites() {
    if (!this.user) {
      this.showNotificationModal('🔒', 'Cần đăng nhập', 'Vui lòng đăng nhập để xem danh sách yêu thích!');
      return;
    }
    this.currentView = 'favorites';
  }

  get displayedMovies() {
    return this.currentView === 'favorites' ? this.favoriteMovies : this.movies;
  }

  // Notification modal methods
  showNotificationModal(icon: string, title: string, message: string) {
    this.notificationIcon = icon;
    this.notificationTitle = title;
    this.notificationMessage = message;
    this.showNotification = true;
  }

  closeNotification() {
    this.showNotification = false;
  }

  // Comment methods
  loadComments(movieId: number) {
    this.commentService.getCommentsByMovie(movieId).subscribe({
      next: (data) => {
        this.comments = data;
      },
      error: (err) => {
        console.error('Lỗi khi tải bình luận:', err);
      }
    });
  }

  postComment() {
    if (!this.user) {
      this.showNotificationModal('🔒', 'Cần đăng nhập', 'Vui lòng đăng nhập để bình luận!');
      return;
    }

    if (!this.newComment.trim()) {
      this.showNotificationModal('⚠️', 'Lỗi', 'Vui lòng nhập nội dung bình luận!');
      return;
    }

    const comment = {
      movieId: this.selectedMovie.id,
      userEmail: this.user.email,
      userName: this.user.name,
      userPicture: this.user.picture,
      content: this.newComment.trim()
    };

    this.commentService.createComment(comment).subscribe({
      next: () => {
        this.loadComments(this.selectedMovie.id);
        this.newComment = '';
        this.showNotificationModal('✅', 'Thành công', 'Đã đăng bình luận!');
      },
      error: (err) => {
        console.error('Lỗi khi đăng bình luận:', err);
        if (err.status === 429) {
          const resetTime = err.error.resetTime || 60;
          this.showNotificationModal(
            '⏱️', 
            'Bình luận quá nhanh', 
            `Bạn đã bình luận quá nhiều. Vui lòng đợi ${resetTime} giây.`
          );
        } else {
          this.showNotificationModal('❌', 'Lỗi', 'Không thể đăng bình luận. Vui lòng thử lại!');
        }
      }
    });
  }

  startEditComment(comment: any) {
    this.editingCommentId = comment.id;
    this.editingCommentText = comment.content;
  }

  cancelEditComment() {
    this.editingCommentId = null;
    this.editingCommentText = '';
  }

  saveEditComment(commentId: number) {
    if (!this.editingCommentText.trim()) {
      this.showNotificationModal('⚠️', 'Lỗi', 'Vui lòng nhập nội dung bình luận!');
      return;
    }

    this.commentService.updateComment(commentId, { content: this.editingCommentText.trim() }).subscribe({
      next: () => {
        this.loadComments(this.selectedMovie.id);
        this.cancelEditComment();
        this.showNotificationModal('✅', 'Thành công', 'Đã cập nhật bình luận!');
      },
      error: (err) => {
        console.error('Lỗi khi sửa bình luận:', err);
        this.showNotificationModal('❌', 'Lỗi', 'Không thể sửa bình luận. Vui lòng thử lại!');
      }
    });
  }

  deleteComment(commentId: number) {
    if (!confirm('Bạn có chắc muốn xóa bình luận này?')) {
      return;
    }

    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.loadComments(this.selectedMovie.id);
        this.showNotificationModal('✅', 'Thành công', 'Đã xóa bình luận!');
      },
      error: (err: any) => {
        console.error('Lỗi khi xóa bình luận:', err);
        this.showNotificationModal('❌', 'Lỗi', 'Không thể xóa bình luận. Vui lòng thử lại!');
      }
    });
  }
}
