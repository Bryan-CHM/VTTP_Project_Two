import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Book, User } from 'src/app/models';
import { LibraryService } from 'src/app/services/library.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  constructor(private router: Router, private fb : FormBuilder, private libSvc : LibraryService) { }

  @ViewChild('file')
  toUpload!: ElementRef
  user! : User
  books! : Book[]
  bookForm! : FormGroup


  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
    this.bookForm = this.fb.group({
      title: this.fb.control<string>('', [Validators.required]),
      authors: this.fb.control<string>(''),
      description: this.fb.control<string>(''),
      publisher: this.fb.control<string>(''),
      publishedDate: this.fb.control<string>(''),
      pageCount: this.fb.control<number>(1,[Validators.min(1)]),
      quantity: this.fb.control<number>(1,[Validators.min(1)]),
      uploadImage: this.fb.control<any>('')
    })
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  processBook(){
    const book : Book = this.bookForm.value as Book
    this.libSvc.uploadBookToLibrary(book, this.toUpload.nativeElement.files[0] )
    alert(`${book.title} successfully added to library`)
    this.router.navigate(['/'])
  }

}
