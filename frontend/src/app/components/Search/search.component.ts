import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Book, User } from '../../models';
import { LibraryService } from '../../services/library.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(private fb : FormBuilder, private libSvc : LibraryService, private router: Router) { }

  adminSearchForm! : FormGroup
  userSearchForm! : FormGroup
  user! : User
  books! : Book[]
  searchBarDisplay! : boolean

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    this.adminSearchForm = this.fb.group({
      query: this.fb.control<string>('', [Validators.required])
    })
    this.userSearchForm = this.fb.group({
      query: this.fb.control<string>('', [Validators.required])
    })
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  processAdminQuery(){
    const payload : any = {
      query: this.adminSearchForm.value.query
    }
    this.libSvc.getAdminListOfBooks(payload)
    sessionStorage.setItem("query", this.adminSearchForm.value.query)
    this.router.navigate(['/results'])
  }

  processUserQuery(){
    const queryPayload : any = {
      query: this.userSearchForm.value.query
    }
    this.libSvc.getUserListOfBooks(queryPayload);
    sessionStorage.setItem("query", this.userSearchForm.value.query)
    this.router.navigate(['/results'])
  }
}
