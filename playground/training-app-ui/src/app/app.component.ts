import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Http} from "@angular/http";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {

  constructor(private http: Http, private router: Router) {
  }


  ngOnInit(): void {
    this.http.get("/login").subscribe(() => {
      // just to assure that CSRF COOKIE IS SET
    });
  }

}
