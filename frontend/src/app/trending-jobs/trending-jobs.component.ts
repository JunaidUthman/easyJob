import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Job {
    id: number;
    title: string;
    company: string;
    field: string;
    description: string;
    date: string;
    image: string;
    type: 'job' | 'internship';
  }


@Component({
  selector: 'app-trending-jobs',
  imports: [CommonModule],
  templateUrl: './trending-jobs.component.html',
  styleUrl: './trending-jobs.component.css'
})



export class TrendingJobsComponent {
  activeTab: string = 'trending-jobs';
  
  // Hardcoded job data
  trendingJobs: Job[] = [
    {
      id: 1,
      title: 'Senior Frontend Developer',
      company: 'TechCorp Solutions',
      field: 'Software Development',
      description: 'We are looking for an experienced Frontend Developer to join our dynamic team. You will be responsible for building responsive web applications using modern frameworks.',
      date: '2 days ago',
      image: '/images/tech-company.jpg',
      type: 'job'
    },
    {
      id: 2,
      title: 'UX/UI Designer',
      company: 'Creative Studios',
      field: 'Design',
      description: 'Join our creative team to design intuitive and beautiful user experiences. You will work closely with developers and product managers to bring ideas to life.',
      date: '5 days ago',
      image: '/images/design-company.jpg',
      type: 'job'
    },
    {
      id: 3,
      title: 'Full Stack Developer',
      company: 'StartupHub Inc',
      field: 'Software Development',
      description: 'Exciting opportunity to work with cutting-edge technologies. Build scalable applications from frontend to backend in a fast-paced startup environment.',
      date: '1 week ago',
      image: '/images/startup.jpg',
      type: 'job'
    },
    {
      id: 4,
      title: 'Marketing Manager',
      company: 'BrandBoost Agency',
      field: 'Marketing',
      description: 'Lead marketing campaigns and strategies for diverse clients. Develop creative solutions to increase brand awareness and drive business growth.',
      date: '1 week ago',
      image: '/images/marketing.jpg',
      type: 'job'
    },
    {
      id: 5,
      title: 'Data Scientist',
      company: 'DataTech Analytics',
      field: 'Data Science',
      description: 'Analyze complex datasets and build machine learning models. Help businesses make data-driven decisions with your insights and expertise.',
      date: '2 weeks ago',
      image: '/images/data-science.jpg',
      type: 'job'
    }
  ];

  trendingInternships: Job[] = [
    {
      id: 6,
      title: 'Software Engineering Intern',
      company: 'Google',
      field: 'Software Development',
      description: 'Summer internship opportunity to work on real-world projects with experienced engineers. Learn and grow your technical skills in a supportive environment.',
      date: '3 days ago',
      image: '/images/google.jpg',
      type: 'internship'
    },
    {
      id: 7,
      title: 'Marketing Intern',
      company: 'Nike',
      field: 'Marketing',
      description: 'Join our marketing team and gain hands-on experience in brand strategy, digital marketing, and campaign management.',
      date: '1 week ago',
      image: '/images/nike.jpg',
      type: 'internship'
    }
  ];

  savedJobs: Job[] = [
    {
      id: 8,
      title: 'Product Manager',
      company: 'Amazon',
      field: 'Product Management',
      description: 'Drive product vision and strategy. Work with cross-functional teams to deliver innovative solutions that delight customers.',
      date: '4 days ago',
      image: '/images/amazon.jpg',
      type: 'job'
    }
  ];

  get displayedJobs(): Job[] {
    switch(this.activeTab) {
      case 'trending-jobs':
        return this.trendingJobs;
      case 'trending-internships':
        return this.trendingInternships;
      case 'saved-jobs':
        return this.savedJobs;
      default:
        return this.trendingJobs;
    }
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  saveJob(jobId: number) {
    console.log('Job saved:', jobId);
    // Implement save functionality
  }

  applyJob(jobId: number) {
    console.log('Apply to job:', jobId);
    // Implement apply functionality
  }
  

}
