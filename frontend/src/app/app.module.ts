import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/Login/login.component';
import { HomeComponent } from './components/Home/home.component';
import { BookComponent } from './components/Book/book.component';
import { CartComponent } from './components/Cart/cart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoginService } from './services/login.service';
import { LibraryService } from './services/library.service';
import { SearchComponent } from './components/Search/search.component';
import { ResultsComponent } from './components/Results/results.component';
import { UploadComponent } from './components/Upload/upload.component';

const appRoutes : Routes = [
  {path: '',component: LoginComponent},
  {path: 'home',component: HomeComponent},
  {path: 'search',component: SearchComponent},
  {path: 'results',component: ResultsComponent},
  {path: 'book',component: BookComponent},
  {path: 'cart',component: CartComponent},
  {path: 'upload',component: UploadComponent},
  {path: '**', redirectTo:'/',pathMatch:'full'},
  // {},
  // {}
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    BookComponent,
    CartComponent,
    SearchComponent,
    ResultsComponent,
    UploadComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [LoginService, LibraryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
