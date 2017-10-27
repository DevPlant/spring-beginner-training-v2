import {Component, OnInit} from "@angular/core";
import {Headers, Http, RequestOptions} from "@angular/http";
import {Router} from "@angular/router";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {

  error: boolean = false;

  username: string;
  password: string;
  private options = new RequestOptions({
    headers: new Headers({"Content-Type": "application/x-www-form-urlencoded"})
  });

  constructor(private http: Http, private router: Router) {
  }

  ngOnInit() {
  }

  doLogin() {
    this.http.post("/login", "username=" + this.username
      + "&password=" + this.password, this.options).subscribe(() => {
      this.http.get("/account/me").subscribe(me => {
        try {
          const roles: string[] = me.json().roles;
          this.router.navigateByUrl("")
        } catch (error) {
          this.error = true;
        }

      }, () => {
        this.error = true;
      })

    }, () => {
      this.error = true;
    })
  }
}
