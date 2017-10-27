import {Component, OnInit} from "@angular/core";
import {Register} from "../../model/register";
import {Http, RequestOptions} from "@angular/http";
import {Router} from "@angular/router";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.css"]
})
export class RegisterComponent implements OnInit {

  register: Register = new Register();
  error: boolean;
  success: boolean;

  constructor(private http: Http, private router: Router) {
  }

  ngOnInit() {
  }

  registerAccount() {
    this.http.post("/account/register", JSON.stringify(this.register),
      new RequestOptions()).subscribe(() => {
      this.success = true;
    }, () => {
      this.error = true;
    });
  }
}
