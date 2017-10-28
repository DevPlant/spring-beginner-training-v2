import {Component, OnInit} from "@angular/core";
import {Book} from "../../model/book";
import {Author} from "../../model/author";
import {Http, RequestOptions} from "@angular/http";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {EditAuthorComponent} from "../edit-author/edit-author.component";
import {EditBookComponent} from "../edit-book/edit-book.component";

@Component({
  selector: "app-main",
  templateUrl: "./main.component.html",
  styleUrls: ["./main.component.css"]
})
export class MainComponent implements OnInit {

  books: Book[];
  authors: Author[];
  isAdmin: boolean = false;

  private options = new RequestOptions();

  constructor(private http: Http, private router: Router, private dialog: MatDialog) {
  }

  ngOnInit() {

    this.http.get("/account/me").subscribe(me => {
      try {
        const roles: string[] = me.json().roles;
        if (roles.indexOf("ROLE_ADMIN") >= 0) {
          this.isAdmin = true;
        } else {
          this.isAdmin = false;
        }
      } catch (error) {
      }
    }, () => {
    });

    this.http.get("/author").subscribe(response => {
      this.authors = response.json() as Author[];
    });

    this.http.get("/book").subscribe(response => {
      this.books = response.json() as Book[];
    });

  }


  logout() {
    this.http.post("/logout", this.options).subscribe(() => {
      this.router.navigateByUrl("uilogin");
    });
  }

  editBook(book: Book) {
    if (!book) {
      book = new Book();
    }
    const dialogRef = this.dialog.open(EditBookComponent, {
      data: {book: Object.assign({}, book), authors: this.authors}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.get("/book").subscribe(response => {
          this.books = response.json() as Book[];
        });
      }
    });
  }

  editAuthor(author: Author) {
    if (!author) {
      author = new Author();
    }
    const dialogRef = this.dialog.open(EditAuthorComponent, {
      data: {author: Object.assign({}, author)}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.get("/author").subscribe(response => {
          this.authors = response.json() as Author[];
        });
        this.http.get("/book").subscribe(response => {
          this.books = response.json() as Book[];
        });
      }
    });

  }

  deleteAuthor(id: Number) {
    this.http.delete(`/author/${id}`, this.options).subscribe(() => {
      this.http.get("/author").subscribe(response => {
        this.authors = response.json() as Author[];
      });
      this.http.get("/book").subscribe(response => {
        this.books = response.json() as Book[];
      });
    });
  }

  deleteBook(id: Number) {
    this.http.delete(`/book/${id}`, this.options).subscribe(() => {
      this.http.get("/book").subscribe(response => {
        this.books = response.json() as Book[];
      });
    });
  }
}
