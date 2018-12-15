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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../app.state';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { User } from '../../../../models/user/user';
import { Role } from '../../../../models/user/role';
import { Preference } from '../../../../models/user/preference';
import { Library } from '../../../../models/library';
import { SingleComicScraping } from '../../../../models/scraping/single-comic-scraping';
import { AlertService } from '../../../../services/alert.service';
import { ComicService } from '../../../../services/comic.service';
import { Comic } from '../../../../models/comics/comic';

export const PAGE_SIZE_PARAMETER = 'pagesize';
export const CURRENT_PAGE_PARAMETER = 'page';

@Component({
  selector: 'app-comic-details',
  templateUrl: './comic-details.component.html',
  styleUrls: ['./comic-details.component.css']
})
export class ComicDetailsComponent implements OnInit, OnDestroy {
  readonly TAB_PARAMETER = 'tab';

  private library$: Observable<Library>;
  private library_subscription: Subscription;
  public library: Library;

  single_comic_scraping$: Observable<SingleComicScraping>;
  single_comic_scraping_subscription: Subscription;
  single_comic_scraping: SingleComicScraping;

  private comic_id = -1;
  public comic = null;
  protected current_tab: number;
  protected title: string;
  protected page_size: number;
  protected current_page: number;

  user$: Observable<User>;
  user_subscription: Subscription;
  user: User;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private alert_service: AlertService,
    private comic_service: ComicService,
    private store: Store<AppState>,
  ) {
    this.library$ = store.select('library');
    this.single_comic_scraping$ = store.select('single_comic_scraping');
    this.activatedRoute.params.subscribe(params => {
      this.comic_id = +params['id'];
    });
    this.user$ = store.select('user');
    activatedRoute.queryParams.subscribe(params => {
      this.current_tab = this.load_parameter(params[this.TAB_PARAMETER], 0);
    });
  }

  ngOnInit() {
    this.library_subscription = this.library$.subscribe(
      (library: Library) => {
        this.library = library;

        if (this.comic === null) {
          this.comic = library.comics.find((comic: Comic) => {
            return comic.id === this.comic_id;
          }) || null;
        }
      });
    this.single_comic_scraping_subscription = this.single_comic_scraping$.subscribe(
      (library_scrape: SingleComicScraping) => {
        this.single_comic_scraping = library_scrape;

        this.comic = this.single_comic_scraping.comic;
      });
    this.user_subscription = this.user$.subscribe(
      (user: User) => {
        this.user = user;
      });
    this.activatedRoute.params.subscribe(params => {
      const id = +params['id'];
      this.comic_service.load_comic_from_remote(id).subscribe(
        (comic: Comic) => {
          if (comic) {
            this.comic = comic;
          } else {
            this.alert_service.show_error_message(`No such comic: id=${id}`, null);
            this.router.navigateByUrl('/');
          }
        },
        error => {
          this.alert_service.show_error_message('Error while retrieving comic...', error);
        },
        () => {
          this.load_comic_details();
        });
    });
    this.activatedRoute.queryParams.subscribe(params => {
      this.set_page_size(parseInt(this.load_parameter(params[PAGE_SIZE_PARAMETER], '100'), 10));
      this.set_current_page(parseInt(this.load_parameter(params[CURRENT_PAGE_PARAMETER], '0'), 10));
      this.current_tab = this.load_parameter(params[this.TAB_PARAMETER], 0);
    });
  }

  ngOnDestroy() {
    this.library_subscription.unsubscribe();
    this.single_comic_scraping_subscription.unsubscribe();
    this.user_subscription.unsubscribe();
  }

  set_page_size(page_size: number): void {
    this.page_size = page_size;
    this.update_params(PAGE_SIZE_PARAMETER, `${this.page_size}`);
  }

  set_current_page(current_page: number): void {
    this.current_page = current_page;
    this.update_params(CURRENT_PAGE_PARAMETER, `${this.current_page}`);
  }

  get_cover_url(): string {
    return this.comic_service.get_cover_url_for_comic(this.comic);
  }

  set_current_tab(current_tab: number): void {
    this.current_tab = current_tab;
    this.update_params(this.TAB_PARAMETER, `${this.current_tab}`);
  }

  private update_params(name: string, value: string): void {
    const queryParams: Params = Object.assign({}, this.activatedRoute.snapshot.queryParams);
    if (value && value.length) {
      queryParams[name] = value;
    } else {
      queryParams[name] = null;
    }
    this.router.navigate([], { relativeTo: this.activatedRoute, queryParams: queryParams });
  }

  private load_parameter(value: string, defvalue: any): any {
    if (value && value.length) {
      return parseInt(value, 10);
    }
    return defvalue;
  }

  load_comic_details(): void {
    this.title = `${this.comic.series || 'Unknown'} ` +
      `(v${this.comic.volume || this.comic.volume || '????'}) ` +
      `#${this.comic.issue_number || '??'}`;
  }
}

