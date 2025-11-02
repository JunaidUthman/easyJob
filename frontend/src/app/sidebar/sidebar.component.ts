import { Component } from '@angular/core';
import { RouterModule ,Router  } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

  constructor(private router: Router) {}
  logout() {
    localStorage.removeItem('Token');
    localStorage.removeItem('ExpirationTime');
    localStorage.removeItem('UserName');
    localStorage.removeItem('Roles');

    this.router.navigate(['/login']);
  }
}
