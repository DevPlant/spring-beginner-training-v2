import {Component, Inject, OnInit} from "@angular/core";
import {Book} from "../../model/book";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Http, RequestOptions} from "@angular/http";
import {Author} from "../../model/author";

@Component({
  selector: "app-edit-book",
  templateUrl: "./edit-book.component.html",
  styleUrls: ["./edit-book.component.css"]
})
export class EditBookComponent implements OnInit {

  book: Book = new Book();
  authors: Author[] = [];
  error: boolean;

  constructor(private dialogRef: MatDialogRef<EditBookComponent>,
              private http: Http, @Inject(MAT_DIALOG_DATA) private data: any) {
    console.log(data);
    this.book = this.data.book;
    this.authors = this.data.authors;
  }

  ngOnInit() {

  }

  saveBook() {
    this.http.post("/book", JSON.stringify(this.book),
      new RequestOptions()).subscribe(() => {
      this.dialogRef.close(true);
    }, () => {
      this.error = true;
    });
  }

}
