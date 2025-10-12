import { Component, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-recrutter-dashboard-home',
  imports: [],
  templateUrl: './recrutter-dashboard-home.component.html',
  styleUrls: ['./recrutter-dashboard-home.component.css']
})
export class RecrutterDashboardHomeComponent implements AfterViewInit {
  @ViewChild('lineChart', { static: false }) lineChart!: ElementRef<HTMLCanvasElement>;
  @ViewChild('barChart', { static: false }) barChart!: ElementRef<HTMLCanvasElement>;

  ngAfterViewInit(): void {
    const lineCtx = this.lineChart?.nativeElement.getContext('2d');
    if (lineCtx) {
      new Chart(lineCtx, {
        type: 'line',
        data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [
            {
              label: 'Job Requests',
              data: [45, 52, 48, 65, 72, 78],
              borderColor: '#4f46e5',
              backgroundColor: 'rgba(79, 70, 229, 0.1)',
              tension: 0.4,
              fill: true,
            },
            {
              label: 'Internship Requests',
              data: [28, 34, 30, 42, 48, 52],
              borderColor: '#d97706',
              backgroundColor: 'rgba(217, 119, 6, 0.1)',
              tension: 0.4,
              fill: true,
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: true, position: 'top' } },
          scales: { y: { beginAtZero: true } }
        }
      });
    }

    const barCtx = this.barChart?.nativeElement.getContext('2d');
    if (barCtx) {
      new Chart(barCtx, {
        type: 'bar',
        data: {
          labels: ['Engineering', 'Marketing', 'Sales', 'Design', 'HR'],
          datasets: [{
            label: 'Requests',
            data: [85, 62, 48, 71, 34],
            backgroundColor: [
              'rgba(79, 70, 229, 0.8)',
              'rgba(37, 132, 90, 0.8)',
              'rgba(217, 119, 6, 0.8)',
              'rgba(220, 38, 38, 0.8)',
              'rgba(16, 185, 129, 0.8)',
            ],
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: { y: { beginAtZero: true } }
        }
      });
    }
  }
}
