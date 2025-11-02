import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateJobFormComponent } from '../create-job-form/create-job-form.component';
import { JobserviceService } from '../service/jobServices/jobservice.service';

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
  creator?: any;
}

@Component({
  selector: 'app-recrutter-jobs',
  imports: [CommonModule, CreateJobFormComponent],
  templateUrl: './recrutter-jobs.component.html',
  styleUrls: ['./recrutter-jobs.component.css']
})
export class RecrutterJobsComponent implements OnInit {
  activeTab: string = 'trending-jobs';
  showCreateJobForm: boolean = false;
  showMenu: { [key: number]: boolean } = {};

  jobs: Job[] = [];
  internships: Job[] = [];
  savedJobs: Job[] = [];

  constructor(private jobService: JobserviceService) {}

  ngOnInit() {
  this.jobService.getJobs().subscribe({
    next: (jobs: JobResponse[]) => {

        // console.log('Fetched jobs from API:', jobs);

        // Map each job and append to the appropriate array
        jobs.forEach(jobResponse => {
          const job = this.mapToJob(jobResponse);
          
          this.jobService.getJobImage(job.image?? '').subscribe({
            next: (imageUrl) => {
              job.image = imageUrl;
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

/** Converts a JobResponse (from API) into a typed Job object, preserving redundancy */
private mapToJob(jobResponse: JobResponse): Job {
  const typeUpper = jobResponse.type?.toUpperCase();
  const normalizedType: 'JOB' | 'INTERNSHIP' =
    typeUpper === 'INTERNSHIP' ? 'INTERNSHIP' : 'JOB';

  return {
    ...jobResponse,
    type: normalizedType
  } as Job;
}



  get displayedJobs(): Job[] {
    switch (this.activeTab) {
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
  }

  seeCandidates(jobId: number) {
    console.log('See candidates for job:', jobId);
  }

  toggleMenu(jobId: number) {
    // Close all other menus
    Object.keys(this.showMenu).forEach(key => {
      if (+key !== jobId) {
        this.showMenu[+key] = false;
      }
    });
    this.showMenu[jobId] = !this.showMenu[jobId];
  }

  closeMenu(jobId: number) {
    this.showMenu[jobId] = false;
  }

  updateJob(jobId: number) {
    console.log('Update job:', jobId);
  }

  deleteJob(jobId: number) {
    console.log('Delete job:', jobId);
  }

  openCreateJobForm() {
    this.showCreateJobForm = true;
  }

  closeCreateJobForm() {
    this.showCreateJobForm = false;
  }
}
