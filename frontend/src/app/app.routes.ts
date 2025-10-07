import { Routes } from '@angular/router';
import {LandingPageComponent} from "./landing-page/landing-page.component";
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import {TrendingJobsComponent} from "./trending-jobs/trending-jobs.component";


export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'home', component: LandingPageComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'login', component: LoginComponent },
    { path: 'jobs', component: TrendingJobsComponent },
];
