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

import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';

@Injectable()
export class TokenService {
  private ACCESS_TOKEN_NAME = 'accessToken';
  private REFRESH_TOKEN_NAME = 'refreshToken';
  constructor() {}

  public clear(): void {
    localStorage.removeItem(this.ACCESS_TOKEN_NAME);
    localStorage.removeItem(this.REFRESH_TOKEN_NAME);
  }

  public setAccessToken(token: string): TokenService {
    localStorage.setItem(this.ACCESS_TOKEN_NAME, token);
    return this;
  }

  public getAccessToken(): Observable<string> {
    const token: string = <string>localStorage.getItem(this.ACCESS_TOKEN_NAME);
    return of(token);
  }

  public setRefreshToken(token: string): TokenService {
    localStorage.setItem(this.REFRESH_TOKEN_NAME, token);
    return this;
  }

  public getRefreshToken(): Observable<string> {
    const token: string = <string>localStorage.getItem(this.REFRESH_TOKEN_NAME);
    return of(token);
  }
}
