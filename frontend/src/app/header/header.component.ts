import { Component, HostListener, OnInit } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { filter } from 'rxjs/operators';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthServiceService } from '../service/authService/auth-service.service';
import { ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [RouterLink , CommonModule]
})
export class HeaderComponent implements OnInit {

  private isLandingPage = false;

  constructor(private router: Router,private authService:AuthServiceService) {
    // Subscribe to route changes
    this.router.events
      .pipe(filter((event: Event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isLandingPage = event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/hero' || event.urlAfterRedirects === '';

        const header = document.querySelector('header');
        if (!this.isLandingPage) {
          // If not on landing page, mark header as scrolled immediately
          window.scrollTo(0, 0);
          header?.classList.add('scrolled');
        } else {
          // If back on landing page, remove scrolled and let scroll logic apply
          header?.classList.remove('scrolled');
        }
      });
  }

  ngOnInit() {
    // Check initial state on component creation
    this.checkInitialState();
  }

  private checkInitialState() {
    const currentUrl = this.router.url;
    this.isLandingPage = currentUrl === '/' || currentUrl === '/hero' || currentUrl === '';

    const header = document.querySelector('header');
    if (this.isLandingPage) {
      // Check current scroll position and apply appropriate class
      const scrollPosition = window.scrollY;
      if (scrollPosition > window.innerHeight - 100) {
        header?.classList.add('scrolled');
      } else {
        header?.classList.remove('scrolled');
      }
    } else {
      header?.classList.add('scrolled');
    }
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('Token');
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    if (!this.isLandingPage) return;

    const header = document.querySelector('header');
    const scrollPosition = window.scrollY;

    if (scrollPosition > window.innerHeight - 100) {
      header?.classList.add('scrolled');
    } else {
      header?.classList.remove('scrolled');
    }
  }

  logout(){
    this.authService.logout();
  }
}
