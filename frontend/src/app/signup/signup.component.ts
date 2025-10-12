import { Component } from '@angular/core';
import { AuthServiceService, SignupRequest } from '../service/authService/auth-service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
  standalone: true,
  imports: [CommonModule , FormsModule , RouterModule]
})
export class SignupComponent {
  userType = '';
  username = '';
  email = '';
  password = '';
  verifyPassword = '';

  // Error flags
  userTypeError = false;
  usernameError = false;
  emailError = false;
  passwordError = false;
  verifyPasswordError = false;
  UserExists = false;

  constructor(private authService: AuthServiceService) {}

  onSubmit() {
    // Reset error flags
    this.userTypeError = false;
    this.usernameError = false;
    this.emailError = false;
    this.passwordError = false;
    this.verifyPasswordError = false;

    let valid = true;

    if (!this.userType) {
      this.userTypeError = true;
      valid = false;
    }
    if (!this.username || this.username.length < 3) {
      this.usernameError = true;
      valid = false;
    }
    if (!this.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
      this.emailError = true;
      valid = false;
    }
    if (!this.password || this.password.length < 4) {
      this.passwordError = true;
      valid = false;
    }
    if (this.password !== this.verifyPassword) {
      this.verifyPasswordError = true;
      valid = false;
    }

    if (!valid) {
      return;
    }

    const signupData: SignupRequest = {
      username: this.username,
      email: this.email,
      password: this.password,
      userType: this.userType // Add this property to your SignupRequest interface and backend
    };

    this.authService.signup(signupData).subscribe({
      next: (res) => {
        alert(res.msg);
      },
      error: (err) => {
        if (err.status === 409) {
          this.UserExists = true;
        }
      }
    });
  }
}