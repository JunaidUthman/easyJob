import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recrutter-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recrutter-header.component.html',
  styleUrls: ['./recrutter-header.component.css']
})
export class RecrutterHeaderComponent {
  @Input() username: string | null = null;
}
