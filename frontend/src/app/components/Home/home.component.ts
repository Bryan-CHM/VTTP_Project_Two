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

  countdownDates : number[] = []
  timers : NodeJS.Timer[] = []
  countdown : any[] = []


  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    this.libSvc.getUserListOfBorrowedBooks(this.user)
    this.sub$ = this.libSvc.borrowedBooks.subscribe(
      data =>{
        this.borrowedBooks = data
        for(let i = 0; i<this.borrowedBooks.length; i++){
          // Get Base64 uploaded image if any
          const currentByteArray = this.borrowedBooks[i].uploadImage
          if(currentByteArray != null){
            this.borrowedBooks[i].uploadImage = "data:image/png;base64," + currentByteArray
          }
          // Set each book's due date
          this.countdownDates[i] = new Date(this.borrowedBooks[i].duration || "1900-01-01 00:00:00").getTime()

          // Initialize timers for each book
          this.timers[i] = setInterval(()=>{
            var now = new Date().getTime()
            var distance = this.countdownDates[i] - now
            var days = Math.floor(distance/(1000*60*60*24))
            var hours = Math.floor((distance%(1000*60*60*24))/(1000*60*60))
            var minutes = Math.floor((distance%(1000*60*60))/(1000*60))
            var seconds = Math.floor((distance%(1000*60))/(1000))
            this.countdown[i] = days + "d " + hours + "h" + minutes + "m " + seconds + "s "
            if(distance<0){
              clearInterval(this.timers[i])
              this.countdown[i] = "Book is due"
            }
          })
        }
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
