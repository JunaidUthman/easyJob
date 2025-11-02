import { Component } from '@angular/core';
import { JobserviceService } from '../service/jobServices/jobservice.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
interface JobResponse {
  id: number;
  title: string;
  description: string;
  location: string;
  type: string;
  image?: string; 
  creator?: any;
}

interface Job {
  id: number;
  title: string;
  description: string;
  location: string;
  type: string;
  image?: string;
  imageUrl?: string; // Add separate property for blob URL
  creator?: any;
}
@Component({
  selector: 'app-my-applications',
  imports: [CommonModule],
  templateUrl: './my-applications.component.html',
  styleUrl: './my-applications.component.css'
})
export class MyApplicationsComponent {
  constructor(private jobService: JobserviceService,private toastr:ToastrService) {}
  
    jobs: Job[] = [];
    internships: Job[] = [];
    savedJobs: Job[] = [];
  
    ngOnInit(): void {
      this.jobService.getMyApplications().subscribe({
      next: (jobs: JobResponse[]) => {
  
          // console.log('Fetched jobs from API:', jobs);
  
          // Map each job and append to the appropriate array
          jobs.forEach(jobResponse => {
            const job = this.mapToJob(jobResponse);

            this.jobService.getJobImage(job.image?? '').subscribe({
              next: (imageUrl) => {
                job.imageUrl = imageUrl; // Set blob URL when loaded
              },
              error: (err) => console.error('Image fetch error', err)
          });
  
          if (job.type === 'JOB') {
            this.jobs.push(job);
          } else if (job.type === 'INTERNSHIP') {
            this.internships.push(job);
          }
        });
      },
      error: (err) => {
        console.error('Error fetching jobs:', err);
      }
  
      
    });
    }
  
    private mapToJob(jobResponse: JobResponse): Job {
      const typeUpper = jobResponse.type?.toUpperCase();
      const normalizedType: 'JOB' | 'INTERNSHIP' =
        typeUpper === 'INTERNSHIP' ? 'INTERNSHIP' : 'JOB';
  
      return {
        ...jobResponse,
        type: normalizedType
      } as Job;
    }
    activeTab: string = 'trending-jobs';
    
    
  
    get displayedJobs(): Job[] {
      switch(this.activeTab) {
        case 'trending-jobs':
          return this.jobs;
        case 'trending-internships':
          return this.internships;
        case 'saved-jobs':
          return this.savedJobs;
        default:
          return this.jobs;
      }
    }
  
    setActiveTab(tab: string) {
      this.activeTab = tab;
    }
  
    saveJob(jobId: number) {
      console.log('Job saved:', jobId);
      // Implement save functionality
    }
  
    cancelApplication(jobId: number) {
      this.jobService.cancelApplication(jobId).subscribe({
        next : (res) => {
          console.log('canceled Application Successfully',res);
          this.showSuccess();
        },
        error: (err) => {
          console.log('Error applying for job',err);
          alert('Error cancelling application');
        }
      });
    }
    
    showSuccess() {
      this.toastr.success('Job Applied Successfully, You can see it in My Applications', 'Success');
    }
}
