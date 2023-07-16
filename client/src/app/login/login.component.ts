import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthService
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  authService: AuthService = inject(AuthService)

  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  submitLogin() {
    const credentials = {
      username: this.loginForm.value.username ?? '',
      password: this.loginForm.value.password ?? ''
    }
    this.authService.submitLogin(credentials).subscribe(res => {
      console.log(res)
    })
  }


}
