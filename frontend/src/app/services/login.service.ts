import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { lastValueFrom} from "rxjs"
import { LoginResponse, User } from "../models";
import { Router } from "@angular/router";

const loginURL = '/api/login/verify'
const createAccountURL = '/api/login/create'


@Injectable()
export class LoginService{

  constructor(private http: HttpClient, private router: Router){}

  userLogin(user : User){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    return lastValueFrom(
      this.http.post<LoginResponse>(loginURL, user, {headers})
    ).then(value =>{
      if (value.status == true){
        user.admin = value.admin
        console.info(user)
        sessionStorage.setItem('user', JSON.stringify(user))
        this.router.navigate(['/home'])
      }else{
        alert("Incorrect Username / Password")
      }
    }).catch(error =>{
      console.info(error)
    })
    }

  createAccount(user : User){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    return lastValueFrom(
      this.http.post<Response>(createAccountURL, user, {headers})
    ).then(value =>{
      console.info(value)
      alert("Account created. Please login using your email and password.")
    }).catch(error =>{
      console.info(error)
    })
    }
}
