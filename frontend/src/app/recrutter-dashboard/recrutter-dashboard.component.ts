import { Component } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { RecrutterHeaderComponent } from '../recrutter-header/recrutter-header.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recrutter-dashboard',
  standalone: true,
  imports: [SidebarComponent, RecrutterHeaderComponent, RouterModule, CommonModule],
  templateUrl: './recrutter-dashboard.component.html',
  styleUrl: './recrutter-dashboard.component.css'
})
export class RecrutterDashboardComponent {
  username: string | null = localStorage.getItem('UserName');
}
