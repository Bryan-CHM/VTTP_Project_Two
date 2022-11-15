import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Book, User } from 'src/app/models';
import { LibraryService } from 'src/app/services/library.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})


export class CartComponent implements OnInit {

  constructor(private router: Router, private fb: FormBuilder,private libSvc : LibraryService) { }

  user! : User
  books! : Book[]
  bookCartForm! : FormGroup
  bookCartArray! : FormArray
  cartBooks : Book[] = []

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]');
    this.bookCartArray = this.fb.array([])
    if(this.user.admin==true){
      this.bookCartForm = this.fb.group({
        cartInfo: this.bookCartArray
      })
      for(let i =0; i<this.books.length;i++){
        this.bookCartArray.push(this.fb.group({
          quantity: this.fb.control<number>(1)
        }))
      }
    }else{
      this.bookCartForm = this.fb.group({
        cartInfo: this.bookCartArray
      })
      for(let i =0; i<this.books.length;i++){
        this.bookCartArray.push(this.fb.group({
          duration: this.fb.control<string>('24h')
        }))
      }
    }
  }

  processCart(){
    if(this.user.admin == true){
      for(let i = 0; i < this.books.length; i++){
        this.cartBooks.push(this.books[i])
        this.cartBooks[i].quantity = Number.parseInt(this.bookCartForm.value.cartInfo[i].quantity)
      }
      this.libSvc.addBooksToLibrary(this.cartBooks)
      if(this.books.length >1){
        alert('Books successfully added')
      }else if(this.books.length == 1){
        alert('Book successfully added')
      }
      sessionStorage.removeItem(`books-${this.user.email}`)
      this.router.navigate(['/'])
    }else{
      for(let i = 0; i < this.books.length; i++){
        this.cartBooks.push(this.books[i])
        this.cartBooks[i].duration = this.bookCartForm.value.cartInfo[i].duration
      }
      this.libSvc.addBorrowedBooks(this.user, this.cartBooks)
      if(this.books.length >1){
        alert('Books successfully borrowed')
      }else if(this.books.length == 1){
        alert('Book successfully borrowed')
      }
      sessionStorage.removeItem(`books-${this.user.email}`)
      this.router.navigate(['/'])
    }
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  deleteFromCart(book: Book){
    this.books.splice(this.books.indexOf(book),1)
    sessionStorage.setItem(`books-${this.user.email}`, JSON.stringify(this.books))
    alert(`${book.title} removed from cart.`)
  }

}
