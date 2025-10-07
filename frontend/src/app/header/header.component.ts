import { Component, HostListener } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { filter } from 'rxjs/operators';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [RouterLink , CommonModule]
})
export class HeaderComponent {

  private isLandingPage = false;

  constructor(private router: Router) {
    // Subscribe to route changes
    this.router.events
      .pipe(filter((event: Event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isLandingPage = event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/hero';

        const header = document.querySelector('header');
        if (!this.isLandingPage) {
          // If not on landing page, mark header as scrolled immediately
          header?.classList.add('scrolled');
        } else {
          // If back on landing page, remove scrolled and let scroll logic apply
          header?.classList.remove('scrolled');
        }
      });
  }


  isAuthenticated(): boolean {
    return !!localStorage.getItem('Token');
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    if (!this.isLandingPage) return; // only apply scroll logic on landing page

    const header = document.querySelector('header');
    const scrollPosition = window.scrollY;

    if (scrollPosition > window.innerHeight - 100) {
      header?.classList.add('scrolled');
    } else {
      header?.classList.remove('scrolled');
    }
  }
}
