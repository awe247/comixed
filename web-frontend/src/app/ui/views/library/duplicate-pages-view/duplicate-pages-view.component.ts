/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2018, The ComiXed Project
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

import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute, Params } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../app.state';
import * as DuplicatesActions from '../../../../actions/duplicate-pages.actions';
import { SelectItem } from 'primeng/api';
import { ComicService } from '../../../../services/comic.service';
import { UserService } from '../../../../services/user.service';
import { DuplicatePage } from '../../../../models/comics/duplicate-page';
import { Duplicates } from '../../../../models/duplicates';

@Component({
  selector: 'app-duplicate-pages-view',
  templateUrl: './duplicate-pages-view.component.html',
  styleUrls: ['./duplicate-pages-view.component.css']
})
export class DuplicatePagesViewComponent implements OnInit {
  readonly ROWS_PARAMETER = 'rows';
  readonly COVER_PARAMETER = 'coversize';

  @Input() duplicates: Duplicates;

  rows_options: Array<SelectItem>;
  rows: number;

  cover_size: number;

  constructor(
    private store: Store<AppState>,
    private comic_service: ComicService,
    private user_service: UserService,
    private activated_route: ActivatedRoute,
    private router: Router,
  ) {
    this.rows_options = [
      { label: '10 comics', value: 10 },
      { label: '25 comics', value: 25 },
      { label: '50 comics', value: 50 },
      { label: '100 comics', value: 100 },
    ];
  }

  ngOnInit() {
    this.activated_route.queryParams.subscribe(params => {
      this.rows = this.load_parameter(params[this.ROWS_PARAMETER], 10);
      this.cover_size = this.load_parameter(params[this.COVER_PARAMETER],
        parseInt(this.user_service.get_user_preference('cover_size', '200'), 10));
    });
  }

  private update_params(name: string, value: string): void {
    const queryParams: Params = Object.assign({}, this.activated_route.snapshot.queryParams);
    if (value && value.length) {
      queryParams[name] = value;
    } else {
      queryParams[name] = null;
    }
    this.router.navigate([], { relativeTo: this.activated_route, queryParams: queryParams });
  }

  private load_parameter(value: string, defvalue: any): any {
    if (value && value.length) {
      return parseInt(value, 10);
    }
    return defvalue;
  }

  set_rows(rows: number): void {
    this.rows = rows;
    this.update_params(this.ROWS_PARAMETER, `${this.rows}`);
  }

  set_cover_size(cover_size: number): void {
    this.cover_size = cover_size;
    this.update_params(this.COVER_PARAMETER, `${this.cover_size}`);
  }

  get_url_for_hash(hash: string): string {
    const page = this.duplicates.pages_by_hash.get(hash)[0];
    return this.comic_service.get_url_for_page_by_id(page.id);
  }

  all_deleted(hash: string): boolean {
    return this.duplicates.pages_by_hash.get(hash).every((page: DuplicatePage) => {
      return page.deleted;
    });
  }

  any_pages_deleted(hash: string): boolean {
    return this.duplicates.pages_by_hash.get(hash).some((page: DuplicatePage) => {
      return page.deleted;
    });
  }

  delete_all_pages(hash: string): void {
    this.store.dispatch(new DuplicatesActions.DuplicatePagesDeleteAll(hash));
  }

  undelete_all_pages(hash: string): void {
    this.store.dispatch(new DuplicatesActions.DuplicatePagesUndeleteAll(hash));
  }

  is_blocked(hash: string): boolean {
    return this.duplicates.pages_by_hash.get(hash).every((page: DuplicatePage) => {
      return page.blocked;
    });
  }

  block_page_hash(hash: string): void {
    this.store.dispatch(new DuplicatesActions.DuplicatePagesBlockHash(hash));
  }

  unblock_page_hash(hash: string): void {
    this.store.dispatch(new DuplicatesActions.DuplicatePagesUnblockHash(hash));
  }
}
