/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, Darryl L. Pierce
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.package
 * org.comixed;
 */

import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {tap, map, switchMap, catchError} from 'rxjs/operators';
import {AuthService} from 'ngx-auth';
import {Observable} from 'rxjs/Observable';
import {TokenService} from './token.service';
import {AlertService} from '../alert.service';

interface AccessData {
  accessToken: string;
  refreshToken: string;
}

@Injectable()
export class AuthenticationService implements AuthService {
  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private alert_service: AlertService,
  ) {}

  isAuthorized(): Observable<boolean> {
    return this.tokenService.getAccessToken().pipe(map(token => !!token));
  }

  getAccessToken(): Observable<string> {
    return this.tokenService.getAccessToken();
  }

  refreshToken(): Observable<any> {
    return this.tokenService
      .getRefreshToken()
      .pipe(switchMap((refreshToken: string) => this.http.post('refresh', {refreshToken})),
      tap((tokens: AccessData) => this.saveAccessData(tokens)),
      catchError((error: Error) => {
        this.alert_service.show_error_message('Unable to refresh authentication token...', error);
        this.logout();
        return Observable.throw(error);
      })
      );
  }

  private saveAccessData({accessToken, refreshToken}: AccessData) {
    this.tokenService
      .setAccessToken(accessToken)
      .setRefreshToken(refreshToken);
  }

  refreshShouldHappen(response: HttpErrorResponse): boolean {
    return response.status === 401;
  }

  verifyTokenRequest(url: string): boolean {
    return url.endsWith('refresh-token');
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post('login', {email: username, password: password})
      .pipe(
      tap((tokens: AccessData) => {
        console.log('login tokens:', tokens);
        this.saveAccessData(tokens);
      }));
  }

  logout(): void {
    this.tokenService.clear();
    location.reload(true);
  }
}
