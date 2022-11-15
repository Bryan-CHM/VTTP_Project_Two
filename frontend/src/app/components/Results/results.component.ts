import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book, User } from '../../models';
import { LibraryService } from '../../services/library.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit, OnDestroy {

  constructor(private libSvc : LibraryService, private router: Router) { }

  user! : User
  sub$! : Subscription
  books! : Book[]
  queriedBooks! : Book[]
  query! : string
  index : number = 0

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}')
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    this.query = sessionStorage.getItem("query") || ''
    this.sub$ = this.libSvc.onNewSearch.subscribe(
      data =>{
        this.queriedBooks = data
    })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  selectBook(book: Book){
    sessionStorage.setItem('book', JSON.stringify(book))
    this.router.navigate(['book'])
  }

  adminNextPage(){
    this.index = this.index + 20
    const payload : any = {
      query: this.query
    }
    this.libSvc.getAdminNextListOfBooks(payload, this.index)
  }

  adminPrevPage(){
    this.index = this.index - 20
    const payload : any = {
      query: this.query
    }
    this.libSvc.getAdminNextListOfBooks(payload, this.index)
  }

  userNextPage(){
    this.index = this.index + 20
    const payload : any = {
      query: this.query
    }
    this.libSvc.getUserNextListOfBooks(payload, this.index)
  }

  userPrevPage(){
    this.index = this.index - 20
    const payload : any = {
      query: this.query
    }
    this.libSvc.getUserNextListOfBooks(payload, this.index)
  }
}
