import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book, User } from 'src/app/models';
import { LibraryService } from 'src/app/services/library.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  constructor(private router: Router, private libSvc : LibraryService) { }

  user! : User
  book! : Book
  books : Book[] = []
  // likes : number = 0

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.book = JSON.parse(sessionStorage.getItem('book') || '{}')
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    sessionStorage.removeItem('book')
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  goBack(){
    const queryPayload : any = {
      query: sessionStorage.getItem("query")
    }
    if(this.user.admin == true){
      this.libSvc.getAdminListOfBooks(queryPayload);
    }else{
      this.libSvc.getUserListOfBooks(queryPayload);
    }
    this.router.navigate(['/results'])
  }

  adminAddBook(){
    if(!this.books.includes(this.book)){
      this.books.push(this.book)
      sessionStorage.setItem(`books-${this.user.email}`, JSON.stringify(this.books))
      alert(`${this.book.title} added to Cart`)
    }else{
      alert(`${this.book.title} already added to Cart`)
    }
  }

  userAddBook(){
    if(!this.books.includes(this.book)){
      this.books.push(this.book)
      sessionStorage.setItem(`books-${this.user.email}`, JSON.stringify(this.books))
      alert(`${this.book.title} added to Cart`)
    }else{
      alert(`${this.book.title} already added to Cart`)
    }
  }

  // addLikes(){
  //   this.likes+=1
  // }

}
