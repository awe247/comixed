/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, The ComiXed Project
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
import {Routes, RouterModule} from '@angular/router';
import {PublicGuard, ProtectedGuard} from 'ngx-auth';
import {MainPageComponent} from './main-page/main-page.component';
import {LoginComponent} from './login/login.component';
import {AccountComponent} from './account/account.component';
import {DuplicatePageListComponent} from './comic/duplicate-page-list/duplicate-page-list.component';
import {ComicListComponent} from './comic/comic-list/comic-list.component';
import {ImportComicListComponent} from './comic/import-comic-list/import-comic-list.component';
import {ComicDetailsComponent} from './comic/comic-details/comic-details.component';

const publicRoutes: Routes = [
  {path: 'home', component: MainPageComponent, canActivate: [PublicGuard]},
  {path: 'login', component: LoginComponent, canActivate: [PublicGuard]},
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '**', redirectTo: 'home'},
];

const protectedRoutes: Routes = [
  {path: 'account', component: AccountComponent, canActivate: [ProtectedGuard]},
  {path: 'duplicates', component: DuplicatePageListComponent, canActivate: [ProtectedGuard]},
  {path: 'comics', component: ComicListComponent, canActivate: [ProtectedGuard]},
  {path: 'comics/:id', component: ComicDetailsComponent, canActivate: [ProtectedGuard]},
  {path: 'import', component: ImportComicListComponent, canActivate: [ProtectedGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(publicRoutes), RouterModule.forRoot(protectedRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
