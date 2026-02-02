import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-callback',
  standalone: true,
  template: `
    <div style="display: flex; justify-content: center; align-items: center; height: 100vh; background: #1a1a2e; color: white;">
      <div style="text-align: center;">
        <h2>🔄 Đang xử lý đăng nhập...</h2>
        <p>Vui lòng đợi trong giây lát</p>
      </div>
    </div>
  `
})
export class CallbackComponent implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.handleCallback();
  }

  handleCallback() {
    // Lấy token và user info từ URL params
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      const email = params['email'];
      const name = params['name'];
      const picture = params['picture'];
      
      if (token && email && name) {
        // Lưu vào localStorage
        localStorage.setItem('token', token);
        const userInfo = {
          email: email,
          name: name,
          picture: picture
        };
        localStorage.setItem('user', JSON.stringify(userInfo));
        
        console.log('Login successful:', userInfo);
        
        // Force reload để component check lại auth
        window.location.href = '/';
      } else {
        // Lỗi - không có token
        console.error('Missing auth params:', params);
        alert('Đăng nhập thất bại. Vui lòng thử lại!');
        this.router.navigate(['/']);
      }
    });
  }
}
