import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book, User } from 'src/app/models';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  constructor(private router: Router) { }

  user! : User
  books! : Book[]

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.books = JSON.parse(sessionStorage.getItem(`books-${this.user.email}`) || '[]')
  }

  logout(){
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

}
