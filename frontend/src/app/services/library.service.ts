import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom, Subject } from "rxjs";
import { Book, CartResponse, User } from "../models";

const adminBooksURL = '/api/books/list/admin'
const userBooksURL = '/api/books/list/user'

@Injectable()
export class LibraryService{

  onNewSearch = new Subject<Book[]>()
  borrowedBooks = new Subject<Book[]>()

  constructor(private http: HttpClient){}

  getUserListOfBooks(payload : String){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    return lastValueFrom(
      this.http.post<Book[]>(userBooksURL, payload, {headers})
    ).then(data=>{
      this.onNewSearch.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  getUserNextListOfBooks(payload : String, index : number){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const userNextURL = `/api/books/list/user/${index}`

    return lastValueFrom(
      this.http.post<Book[]>(userNextURL, payload, {headers})
    ).then(data=>{
      this.onNewSearch.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  addBorrowedBooks(user : User, cartBooks: Book[]){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const addURL = `/api/books/add/user`

    const payload = {
      books : cartBooks,
      email : user.email
    }

    return lastValueFrom(
      this.http.post<CartResponse>(addURL, payload, {headers})
    ).then(data=>{
      console.info(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  returnBorrowedBook(user : User, book : Book){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const userReturnURL = '/api/books/return'

    const userPayload = {
      email : user.email,
      book : book
    }

    return lastValueFrom(
      this.http.post<Book[]>(userReturnURL, userPayload, {headers})
    ).then(data=>{
      console.log(data)
      this.borrowedBooks.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  getUserListOfBorrowedBooks(user : User){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const userBorrowURL = '/api/books/list/user/borrowed'

    const userPayload = {
      email : user.email
    }

    return lastValueFrom(
      this.http.post<Book[]>(userBorrowURL, userPayload, {headers})
    ).then(data=>{
      this.borrowedBooks.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  getAdminListOfBooks(payload : String){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    return lastValueFrom(
      this.http.post<Book[]>(adminBooksURL, payload, {headers})
    ).then(data=>{
      this.onNewSearch.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  getAdminNextListOfBooks(payload : String, index: number){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const nextURL = `/api/books/list/admin/${index}`

    return lastValueFrom(
      this.http.post<Book[]>(nextURL, payload, {headers})
    ).then(data=>{
      this.onNewSearch.next(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  addBooksToLibrary(cartBooks: Book[]){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const addURL = `/api/books/add/`


    return lastValueFrom(
      this.http.post<CartResponse>(addURL, cartBooks, {headers})
    ).then(data=>{
      console.info(data)
    }).catch(error=>{
      console.info(error)
    })
  }

  uploadBookToLibrary(book: Book, uploadImage: File | Blob){
    const headers = new HttpHeaders()
    .set('Accept', 'application/json')

    const uploadURL = '/api/books/upload'

    const data = new FormData()
    data.set('title', book.title || " ")
    data.set('authors', book.authors || " ")
    data.set('description', book.description || " ")
    data.set('publisher', book.publisher || " ")
    data.set('publishedDate', book.publishedDate || " ")
    data.set('pageCount', book.pageCount || " ")
    data.set('quantity', book.quantity?.toString() || " ")
    data.set('uploadImage', uploadImage)

    return lastValueFrom(
      this.http.post<any>(uploadURL, data)
      ).then(data=>{
        console.info(data)
      }).catch(error=>{
        console.info(error)
      })
  }
}
