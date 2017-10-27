import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {Http} from "@angular/http";

@Injectable()
export class AppGuard implements CanActivate {

  constructor(private http: Http, private router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    return Observable.create(obs => {
      this.http.get("/account/me").subscribe(me => {
        try {
          const roles: string[] = me.json().roles;
          obs.next(true);
          obs.complete();
        } catch (error) {
          this.router.navigateByUrl("uilogin");
          obs.next(false);
          obs.complete();
        }

      }, () => {
        this.router.navigateByUrl("uilogin");
        obs.next(false);
        obs.complete();
      })
    });


  }

}
