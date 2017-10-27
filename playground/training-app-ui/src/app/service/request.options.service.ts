import {Injectable} from "@angular/core";
import {BaseRequestOptions, RequestOptions, RequestOptionsArgs} from "@angular/http";
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class RequestOptionsService extends BaseRequestOptions {

  constructor(private cookieService: CookieService) {
    super();
    this.headers.set('Content-Type', 'application/json');
  }

  merge(options?: RequestOptionsArgs): RequestOptions {
    const newOptions = super.merge(options);
    newOptions.headers.set("X-XSRF-TOKEN", this.cookieService.get("XSRF-TOKEN"));
    return newOptions;
  }
}
