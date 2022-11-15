import { Component, OnInit } from '@angular/core';
import { Form, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../models';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private fb : FormBuilder, private logSvc : LoginService, private router : Router) { }

  loginForm! : FormGroup
  createAccountForm! : FormGroup
  displayCreateAccount : boolean = false

  ngOnInit(): void {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}')
    if(user.email!= null){
      this.router.navigate(['/home'])
    }
    this.loginForm = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required])
    })
    this.createAccountForm = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  flipPage() : void{
    this.displayCreateAccount = !this.displayCreateAccount
  }

  processLogin(): void{
    const user : User = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }
    this.logSvc.userLogin(user)
  }

  processCreateAccount(): void{
    const user : User = {
      email: this.createAccountForm.value.email,
      password: this.createAccountForm.value.password
    }
    this.logSvc.createAccount(user);
  }

}
