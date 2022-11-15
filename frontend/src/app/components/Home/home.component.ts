import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LibraryService } from 'src/app/services/library.service';
import { Book, User } from '../../models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  constructor(private libSvc : LibraryService, private router: Router) {}

  user! : User
  books! : Book[]
  sub$! : Subscription
  borrowedBooks : Book[] = []
  countDownDate = new Date("2022-11-15 21:04:10.0").getTime()
  countdown : any
  x = setInterval(()=>{
    var now = new Date().getTime()
    var distance = this.countDownDate - now
    var days = Math.floor(distance/(1000*60*60*24))
    var hours = Math.floor((distance%(1000*60*60*24))/(1000*60*60))
    var minutes = Math.floor((distance%(1000*60*60))/(1000*60))
    var seconds = Math.floor((distance%(1000*60))/(1000))
    this.countdown = days + "d " + hours + "h" + minutes + "m " + seconds + "s "
    if(distance<0){
      clearInterval(this.x)
      this.countdown = "Book is due"
    }
  })


  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    this.libSvc.getUserListOfBorrowedBooks(this.user)
    this.sub$ = this.libSvc.borrowedBooks.subscribe(
      data =>{
        this.borrowedBooks = data
    })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  returnBook(book : Book){
    this.libSvc.returnBorrowedBook(this.user, book)
    this.router.navigate(['/'])
  }

}
