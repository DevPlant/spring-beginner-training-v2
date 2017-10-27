import {Component, Inject, OnInit} from "@angular/core";
import {Author} from "../../model/author";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Http, RequestOptions} from "@angular/http";

@Component({
  selector: "app-edit-author",
  templateUrl: "./edit-author.component.html",
  styleUrls: ["./edit-author.component.css"]
})
export class EditAuthorComponent implements OnInit {

  author: Author = new Author();
  error: boolean;

  constructor(private dialogRef: MatDialogRef<EditAuthorComponent>,
              private http: Http, @Inject(MAT_DIALOG_DATA) private data: any) {
    this.author = this.data.author;
  }

  ngOnInit() {

  }

  saveAuthor() {
    this.http.post("/author", JSON.stringify(this.author),
      new RequestOptions()).subscribe(() => {
      this.dialogRef.close(true);
    }, () => {
      this.error = true;
    });
  }
}
