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
  username = '';
  email = '';
  password = '';
  verifyPassword = '';

  // Error flags
  usernameError = false;
  emailError = false;
  passwordError = false;
  verifyPasswordError = false;
  UserExists = false;

  constructor(private authService: AuthServiceService) {}

  onSubmit() {
    // Reset error flags
    this.usernameError = false;
    this.emailError = false;
    this.passwordError = false;
    this.verifyPasswordError = false;

    let valid = true;

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
      password: this.password
    };

    this.authService.signup(signupData).subscribe({
      next: (res) => {
        alert(res.msg);
        console.log(res);
      },
      error: (err) => {
        if(err = 409){
          this.UserExists = true;
        }
      }
    });
  }
}