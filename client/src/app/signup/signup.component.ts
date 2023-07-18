import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../service/auth.service';
import { FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import { Roles } from '../model/roles';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [AuthService],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  authService: AuthService = inject(AuthService);

  constructor() {}

  signupForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
    email: new FormControl(''),
    role: new FormControl('')
  });

  public checklist = [
    { id: 1, value: Roles.ROLE_USER, isSelected: false },
    { id: 2, value: Roles.ROLE_MODERATOR, isSelected: false }
  ];

  role_str:any;
  isAllSelected(item:any) {
    this.checklist.forEach(val => {
      if (val.id == item.id) {
        val.isSelected = !val.isSelected;
        this.role_str = val.value
      } else {
        val.isSelected = false;
      }
    })
  }

  submitSignup() {
    const credentials = {
      username: this.signupForm.value.username ?? '',
      password: this.signupForm.value.password ?? '',
      email: this.signupForm.value.email ?? '',
      role: this.role_str.toString()
    }
    console.log(credentials)
    this.authService.submitSignup(credentials).subscribe(res => {
      console.log(res)
    })
  }

}
