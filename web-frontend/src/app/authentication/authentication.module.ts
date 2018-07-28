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

import {NgModule} from '@angular/core';
import {
  AuthModule,
  AUTH_SERVICE,
  PUBLIC_FALLBACK_PAGE_URI,
  PROTECTED_FALLBACK_PAGE_URI
} from 'ngx-auth';

import {AuthenticationService} from './authentication.service';
import {TokenService} from './token.service';

export function factory(authenticationService: AuthenticationService) {
  return authenticationService;
}

@NgModule({
  imports: [
    AuthModule,
  ],
  providers: [
    AuthenticationService,
    TokenService,
    {provide: PROTECTED_FALLBACK_PAGE_URI, useValue: '/'},
    {provide: PUBLIC_FALLBACK_PAGE_URI, useValue: '/login'},
    {
      provide: AUTH_SERVICE,
      deps: [AuthenticationService],
      useFactory: factory,
    },
  ],
})
export class AuthenticationModule {}
