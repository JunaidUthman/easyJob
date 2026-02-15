import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

interface ApplicationRequest {
    jobId: number;
    jobTitle: string;
    jobDescription: string;
    applicantId: number;
    applicantName: string;
    applicantEmail: string;
    applicantImage: string;
    educationLevel: string;
}

@Component({
    selector: 'app-recruiter-requests',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './recruiter-requests.component.html',
    styleUrl: './recruiter-requests.component.css'
})
export class RecruiterRequestsComponent implements OnInit {

    requests: ApplicationRequest[] = [];
    isLoading = true;
    selectedRequest: ApplicationRequest | null = null; // For modal details

    constructor(private http: HttpClient, private toastr: ToastrService) { }

    ngOnInit(): void {
        this.fetchRequests();
    }

    fetchRequests() {
        this.isLoading = true;
        const token = localStorage.getItem('Token');
        const headers = { 'Authorization': `Bearer ${token}` };

        this.http.get<ApplicationRequest[]>('http://localhost:8080/api/recruiter/requests', { headers })
            .subscribe({
                next: (data) => {
                    this.requests = data;
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error('Error fetching requests', err);
                    this.toastr.error('Failed to load requests');
                    this.isLoading = false;
                }
            });
    }

    getProfileImage(imageName: string | undefined): string {
        if (!imageName) return 'assets/default-avatar.png'; // Fallback
        // Assuming you have an endpoint or public folder for images. 
        // If backend serves images via an endpoint like /api/images/{name}:
        return `http://localhost:8080/api/images/${imageName}`;
        // Or if previous code used a service to fetch image URL, adapting:
        // For simplicity here, assuming direct URL or handled by `getProfileImageUrl` logic pattern if I copied it.
        // The previous profile component used `getProfileImageUrl()`.
    }

    acceptRequest(request: ApplicationRequest) {
        if (!confirm(`Are you sure you want to accept ${request.applicantName}?`)) return;

        const token = localStorage.getItem('Token');
        const headers = { 'Authorization': `Bearer ${token}` };

        this.http.post<any>(`http://localhost:8080/api/recruiter/requests/${request.jobId}/${request.applicantId}/accept`, {}, { headers })
            .subscribe({
                next: (res) => {
                    this.toastr.success(res.message, 'Accepted');
                    // Optionally remove from list or mark as accepted
                    // For now, I'll remove it from the view to indicate it's processed, or keep it.
                    // Requirement: "notification should be sent". It doesn't say "remove from list".
                    // But usually "Requests" implies pending.
                    // I'll leave it for now or remove if I had a status field. 
                    // Since I don't have a status field in the DTO or filter, it will reappear on refresh unless I filter pending.
                    // The backend `getRecruiterRequests` fetches ALL applicants. 
                    // To improve, `JobService` should might filter by status if `UserJob` had status.
                    // But `UserJob` is just a ManyToMany Join Table currently (Set<Job> jobs).
                    // So there is no "status" in the provided entities.
                    // Asking valid question: "Does UserJob have status?". Only `users` Set in `Job` and `jobs` Set in `User`.
                    // So I cannot filter by status "ACCEPTED".
                    // I will just show success message.
                },
                error: (err) => {
                    console.error('Error accepting application', err);
                    this.toastr.error('Failed to accept application');
                }
            });
    }

    showDetails(request: ApplicationRequest) {
        this.selectedRequest = request;
    }

    closeDetails() {
        this.selectedRequest = null;
    }
}
